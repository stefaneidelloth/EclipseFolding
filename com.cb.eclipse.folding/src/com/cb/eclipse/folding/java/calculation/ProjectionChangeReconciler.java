/*******************************************************************************
 * Copyright (c) 2004 Coffee-Bytes.com and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/cpl.php
 * 
 * Contributors:
 *     Coffee-Bytes.com - initial API and implementation
 *******************************************************************************/
package com.cb.eclipse.folding.java.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.JavaPositionMetadata;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * The ProjectionChangeReconciler processes the old projection annotations in
 * combination with the new projection annotations.
 * 
 * There are four goals to the reconcilation process. 1.) Add any new
 * ProjectionAnnotations that were created by the change. This involves finding
 * no counterpart in the original annotation set, and therefore adding the new
 * annotation. 2.) Update any existing ProjectionAnnotations that were changed.
 * This involves finding a counterpart, but also recognizing that it was
 * different from the original. Therefore the position of the annotation needs
 * to be altered. (We use the existing annotation to keep track of the user's
 * fold state). 3.) Delete any missing ProjectionAnnotations. 4.) Leave
 * untouched any annotations that haven't changed.
 * 
 * @author R.J. Lorimer
 */
public class ProjectionChangeReconciler {

	private JavaProjectionCalculator calculator;
	private List filters;

	private IDocument document;

	public ProjectionChangeReconciler() {
		calculator = new JavaProjectionCalculator();
		filters = new ArrayList();
		filters.add(new MinimumLineCountFilter());
		filters.add(new UserDefinedCollisionFilter());
		filters.add(new UserDefinedOverrideFilter());
		
	}

	public void setCurrentDocument(IDocument doc) {
		document = doc;
	}

	/**
	 * Performs an initialization on the projection annotation model. This is
	 * essentially the process of putting the result from the
	 * JavaProjectionCalculator on to annotation model.
	 * 
	 * @param model
	 * @param input
	 */
	public void initialize(ProjectionAnnotationModel model, IJavaElement input) {
		
		try {
			
			if (input instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) input;
				synchronized (unit) {
					try {
						unit.reconcile(ICompilationUnit.NO_AST, false, null, null);
					}
					catch (JavaModelException x) {
						x.printStackTrace();
					}

				}
			}

			// enable collapsing so new structures that are created can be 
			// collapsed. Initialize should only be invoked on editor startup.
			calculator.setCollapsing(true);
			if (model != null) {
				Map additions = reconstructAnnotations(input);
				model.removeAllAnnotations();
				model.replaceAnnotations(null, additions);
			}
		}
		finally {
			// ensure we disable collapsing on the calculator so no further 
			// projections are collapsed at construction.
			calculator.setCollapsing(false);
		}
	}

	/**
	 * Using the existing model and a JavaProjectionCalculator instance,
	 * determines the new/old annotations and updates the annotation model
	 * accordingly.
	 * 
	 * This method must categorize positions 3 ways: 1.) Updates: The foldable
	 * regions which haven't changed from scan to scan. This is a loose term,
	 * because as the document changes, so do the position objects. However,
	 * theoretically a fresh scan and construction will yield very similar
	 * results on the positions that do represent the same region. As such,
	 * these annotations are matched carefully and left alone. 2.) Additions:
	 * This is any position found in the new scan that is not matched (as per
	 * category 1) with any position in the current annotation model. 3.)
	 * Deletions: This is any position found in the current annotation model
	 * that is not matched (as per category 1) with any position in the new
	 * scan.
	 * 
	 * It is important to recognize the difference between the three categories,
	 * because we must maintain the collapsed/expanded state of each annotation,
	 * and that means matching EXISTING annotations, which contain the user
	 * preference (what the user has collapsed/expanded), with the re-scanned
	 * results, primarily so we don't alter/override the user changes.
	 * 
	 * @param model
	 *            The projection annotation model containing the current user
	 *            selections.
	 * @param input
	 *            The element to re-scan.
	 */
	public void reconcile(ProjectionAnnotationModel model, IJavaElement input) {

		try {
			// disable collapsing on execution to ensure that no 
			// newly created structures (methods, types, etc) are collapsed 
			// while the user is typing.
			calculator.setCollapsing(false);
			Map currentAnnotations = getCurrentAnnotations(model);
			Map rebuiltAnnotations = reconstructAnnotations(input);

			List updates = trimMatches(currentAnnotations, rebuiltAnnotations);

			List deletions = new ArrayList(currentAnnotations.keySet());

			model.modifyAnnotations((ProjectionAnnotation[]) deletions.toArray(new ProjectionAnnotation[0]), rebuiltAnnotations,
					(ProjectionAnnotation[]) updates.toArray(new ProjectionAnnotation[0]));
		}
		finally {
			// is this neccessary? matched from default Eclipse installation.
			calculator.setCollapsing(true);
		}

	}

	/**
	 * Performs a fresh scan of the IJavaElement and performs a filter and
	 * normalization of the annotation set returned.
	 * 
	 * @param input
	 *            The IJavaElement to scan.
	 * @return The Annotation->Position map provided by the scan.
	 */
	private Map reconstructAnnotations(IJavaElement input) {
		Map rebuiltAnnotations = calculator.findAnnotations(input);
		try {
			filterAndNormalizePositions(rebuiltAnnotations);
		}
		catch (BadLocationException e) {			
			throw new ReconciliationException("Unable to process positions", e);
		}
		return rebuiltAnnotations;
	}

	

	/**
	 * Iterates over the old/new maps of annotations/positions, and removes any
	 * entries (from both) that reside IN both. Those annotation keys that are
	 * removed are then stored in a List and returned as the final result.
	 * 
	 * The definition of a match is determined by the isMatch method.
	 * 
	 * If a match IS found, the position currently in the annotation model is
	 * updated to match the annotation provided by the new scan to ensure that
	 * the annotations match what is expected.
	 * 
	 * @param currentAnnotations
	 *            The annotations currently in the model (as seen prior the
	 *            reconciliation process)
	 * @param rebuiltAnnotations
	 *            The annotations found after rescanning the compilation unit.
	 * @return A list of matched annotations.
	 */
	private List trimMatches(Map currentAnnotations, Map rebuiltAnnotations) {

		List removed = new ArrayList();
		Iterator currentEntries = currentAnnotations.entrySet().iterator();
		while (currentEntries.hasNext()) {
			Map.Entry keyValue = (Map.Entry) currentEntries.next();
			Position aPosition = (Position) keyValue.getValue();
			Iterator rebuiltEntries = rebuiltAnnotations.values().iterator();
			
			
			while (rebuiltEntries.hasNext()) {
				Position bPosition = (Position) rebuiltEntries.next();

				if (aPosition.getOffset() == bPosition.getOffset()) {
					rebuiltEntries.remove();
					currentEntries.remove();
					removed.add(keyValue.getKey());
					aPosition.setOffset(bPosition.getOffset());
					aPosition.setLength(bPosition.getLength());

				}

			}

		}
		return removed;
	}

	

	/**
	 * Produces a map off of the current annotation model of annotations to
	 * positions.
	 * 
	 * The map consists of annotations and their corresponding position objects.
	 * 
	 * @param model
	 *            The annotation model (where-in the annotations and positions
	 *            reside).
	 * @return A simplified map data-structure of the data in the annotation
	 *         model ( ProjectionAnnotation->Position key->value).
	 */
	private Map getCurrentAnnotations(ProjectionAnnotationModel model) {
		Map positions = new HashMap();
		Iterator annotations = model.getAnnotationIterator();
		while (annotations.hasNext()) {
			Annotation nextAnnotation = (Annotation) annotations.next();
			positions.put(nextAnnotation, model.getPosition(nextAnnotation));
		}
		return positions;
	}

	/**
	 * Filters each position in the map of positions into a foldable region (if
	 * applicable). Some regions may not be foldable due to their size/position
	 * (as determined by user preferences), and as such those positions are
	 * removed from the map.
	 * 
	 * @param positionMap
	 *            The map containing the positions (as values) to filter and
	 *            normalize.
	 */
	private void filterAndNormalizePositions(Map positionMap) throws BadLocationException {

		Iterator positions = positionMap.entrySet().iterator();
		while (positions.hasNext()) {
			Map.Entry entry = (Map.Entry) positions.next();
			EnhancedPosition pos = (EnhancedPosition) entry.getValue();

			normalizePosition(pos);
			
		}
		applyFilters(positionMap);
		

	}

	private void applyFilters(Map positionMap) throws BadLocationException {
		Iterator allFilters = filters.iterator();
		while(allFilters.hasNext()) {
			PositionFilter aFilter = (PositionFilter)allFilters.next();
			Iterator allPositions = positionMap.values().iterator();
			while(allPositions.hasNext()) {
				EnhancedPosition aPosition = (EnhancedPosition)allPositions.next();
				if(aFilter.shouldFilter(positionMap, aPosition)) {
					
					allPositions.remove();
				}
			}
		}
	}
	
	
	/**
	 * Attempts to correct the position to the 'line' based rules of the current
	 * projection model (currently only supports line by line folding).
	 * 
	 * @param position
	 *            The position to have its text coordinates normalized
	 * @throws BadLocationException
	 *             thrown by IDocument queries.
	 */
	private void normalizePosition(EnhancedPosition position) throws BadLocationException {
		JavaPositionMetadata metadata = (JavaPositionMetadata)position.getMetadata();
		int start = document.getLineOfOffset(position.getOffset());
		int end = document.getLineOfOffset(position.getOffset() + position.getLength());

		int offset = document.getLineOffset(start);

		if (!metadata.isFilterLastLine()) {
			end++;
		}

		int numLines = document.getNumberOfLines();
		int safeEnd = Math.min(numLines-1, end);
		int endOffset = document.getLineOffset(safeEnd);
		int length = endOffset - offset;

		position.setOffset(offset);
		position.setLength(length);

	}

	
	
	/**
	 * Position filters can be executed after position normalization to remove 
	 * positions that are invalid for 'whatever' reason.
	 * 
	 * Invalid positions commonly occur because the strategies are not cross-communicative.
	 * @author R.J. Lorimer
	 */
	private abstract class PositionFilter {
		abstract boolean shouldFilter(Map allPositions, EnhancedPosition thePosition) throws BadLocationException;
	}
	
	/**
	 * Filters out positions that are not significant enough based on their line count.
	 * 
	 * @author R.J. Lorimer
	 */
	private class MinimumLineCountFilter extends PositionFilter {
		
		boolean shouldFilter(Map allPositions, EnhancedPosition position) throws BadLocationException {
			int start = 0;
			int end = 0;
			int userDefinedMinHeight = 0;
			int minimumHeight = 0;
			try {
				start = document.getLineOfOffset(position.getOffset());
				end = document.getLineOfOffset(position.getOffset() + position.getLength());
	
				// if end and start are less than 'minimum height' lines
				// apart, do something!
				userDefinedMinHeight = FoldingPlugin.getPrefs().getInt(PreferenceKeys.MINIMUM_SIZE);
				minimumHeight = Math.max(0, userDefinedMinHeight);
				if((end - start) < minimumHeight) {
					return true;
				}
				return false;
			}
			catch(BadLocationException e) {
				throw new ReconciliationException("Minimum Line Count Bad Location Exception: Start: " + start + " End: " + end + " User Defined Minimum Height: " + userDefinedMinHeight + " True Minimum Height: " + minimumHeight, e);
			}
		}
		
	}
	
	/**
	 * Filters out any user-defined positions that collide with system-defined positions.
	 * 
	 * This filter is neccessary to prevent the user producing system crossing folds that 
	 * confuse the projection viewer API.
	 * @author R.J. Lorimer
	 */
	private class UserDefinedCollisionFilter extends PositionFilter {
		boolean shouldFilter(Map allPositions, EnhancedPosition thePosition) throws BadLocationException {
			Iterator positions = allPositions.values().iterator();
			while(positions.hasNext()) {
				EnhancedPosition aPosition = (EnhancedPosition)positions.next();
				JavaPositionMetadata metadata = (JavaPositionMetadata)aPosition.getMetadata();
				if(aPosition == thePosition) continue;
				if( metadata.isUserDefined() && thePosition.collidesWith(aPosition) && !(thePosition.isAdjacent(aPosition))) {					
					return true;
				}
			}
			
			return false;
		}
		
	}
	
	/**
	 * Filters out any positions that may be overridden due to user defined regions.
	 * 
	 * Essentially, due to user-defined regions, two positions may have the same starting location.
	 * This filter attempts to select the best (biggest) position of the two.
	 * 
	 * @author R.J. Lorimer
	 */
	private class UserDefinedOverrideFilter extends PositionFilter {
		boolean shouldFilter(Map allPositions, EnhancedPosition thePosition) throws BadLocationException {
			Iterator positions = allPositions.values().iterator();
			while(positions.hasNext()) {
				EnhancedPosition aPosition = (EnhancedPosition)positions.next();
				if(aPosition == thePosition) { continue; }
				if(aPosition.getStart() == thePosition.getStart() && aPosition.getLength() >= thePosition.getLength()) {
					return true;
				}
			}
			
			return false;
		}
	}
}