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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jface.text.BadLocationException;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.java.JavaPositionMetadata;
import com.cb.eclipse.folding.java.JavaProjectionAnnotation;

/**
 * The JavaProjectionCalculator implements the algorithm for recursively
 * processing an IJavaElement tree and providing a group of positions that
 * represent the foldable structures of that tree.
 * 
 * @author R.J. Lorimer
 */
public class JavaProjectionCalculator {

	private boolean enableCollapsing;

	public JavaProjectionCalculator() {

	}

	/**
	 * This ensures that collapsing is disabled temporarily for all elements.
	 * 
	 * @param collapseOn
	 */
	public void setCollapsing(boolean collapseOn) {
		enableCollapsing = collapseOn;
	}

	/**
	 * Produces an map of annotations to positions for a Java editor.
	 * 
	 * @see com.cb.eclipse.folding.ProjectionAnnotationFactory#findAnnotations(org.eclipse.jdt.core.IParent)
	 */
	public Map findAnnotations(IJavaElement parentElement) {

		try {
			Map result = new HashMap();

			findAnnotations((IJavaElement) parentElement, result);
			return result;
		}
		catch (JavaModelException e) {
		}
		catch (InvalidInputException e) {
		}
		catch (BadLocationException e) {
		}
		return null;
	}

	/**
	 * Finds all JavaProjectionAnnotation positions for <code>elem</code> and
	 * places them into the result map.
	 * 
	 * @param elem
	 * @param result
	 * @throws JavaModelException
	 * @throws InvalidInputException
	 */
	private Set findAnnotations(IJavaElement elem, Map result) throws JavaModelException, InvalidInputException, BadLocationException {

		boolean doCollapse = false;
		int elemType = elem.getElementType();

		Set regions = null;
		try {
			regions = computeProjections(elem);
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		
		// Recursively process
		if (elem instanceof IParent) {
			IJavaElement[] children = ((IParent) elem).getChildren();
			for (int i = 0; i < children.length; i++) {
				IJavaElement aChild = children[i];
				Set childRegions = findAnnotations(aChild, result);
				removeCollisions(regions, childRegions);
			}

		}

		

		constructAnnotations(elem, result, regions);

		return regions;

	}

	/**
	 * Determines what regions in the parent list collide with regions in the
	 * child list and removes them.
	 * 
	 * The nature of the java elements are to contain other elements. If, for
	 * some reason, the parent element's calculation strategy returned regions
	 * that are conflicting with the child element's calculation strategy's
	 * returned regions, we throw away the parent region's results for that
	 * area.
	 * 
	 * The assumption is that if the child region had visibility to a particular
	 * region, it is up to the child region to determine what to do with it.
	 * 
	 * @param parentSet
	 * @param childSet
	 */
	private void removeCollisions(Set parentSet, Set childSet) {
		if (parentSet == null || childSet == null) return;

		Iterator parents = parentSet.iterator();
		while (parents.hasNext()) {
			EnhancedPosition parentRegion = (EnhancedPosition) parents.next();
			if ( ((JavaPositionMetadata)parentRegion.getMetadata()).isOverlap() && hasCollision(parentRegion, childSet)) {
				parents.remove();
			}
		}
	}

	private boolean hasCollision(EnhancedPosition parentRegion, Set childSet) {

		Iterator children = childSet.iterator();
		while (children.hasNext()) {
			EnhancedPosition childRegion = (EnhancedPosition) children.next();
			if (parentRegion.collidesWith(childRegion) || childRegion.contains(parentRegion) && !((JavaPositionMetadata)parentRegion.getMetadata()).isUserDefined()) { return true; }
		}
		return false;
	}

	/**
	 * Associates JavaProjectionAnnotations with Positions in the result map.
	 * 
	 * @param elem
	 * @param result
	 * @param positions
	 * @throws BadLocationException
	 */
	private void constructAnnotations(IJavaElement elem, Map result, Set positions) throws BadLocationException, JavaModelException {
		ISourceReference reference = (ISourceReference) elem;

		if (positions != null) {

			for (Iterator iter = positions.iterator(); iter.hasNext();) {
				EnhancedPosition aPosition = (EnhancedPosition) iter.next();
				result.put(new JavaProjectionAnnotation(0, aPosition, elem, enableCollapsing && ((JavaPositionMetadata)aPosition.getMetadata()).isCollapse()), aPosition);
			}
		}
	}

	protected boolean enableCollapsing() {
		return enableCollapsing;
	}

	/**
	 * The core logic of this class.
	 * 
	 * Computes using the strategy objects what regions are available as
	 * projections for this particular element.
	 * 
	 * @param elem
	 * @return @throws
	 *         JavaModelException
	 * @throws InvalidInputException
	 */
	private Set computeProjections(IJavaElement elem) throws JavaModelException, InvalidInputException {
		if (!(elem instanceof ISourceReference)) return null;

		
		RegionCalculationStrategy strategy = StrategyFactory.instance(this, elem);
		strategy.initialize();
		Set regionSet;
		if (!strategy.shouldScan(elem)) {
			regionSet = strategy.result(); // call immediately...
		}
		else {
			ISourceReference reference = (ISourceReference) elem;
			ISourceRange range = reference.getSourceRange();

			// All other element types require some parsing...
			String contents = reference.getSource();

			if (contents == null) return null;

			IScanner scanner = ToolFactory.createScanner(true, false, false, false);
			scanner.setSource(contents.toCharArray());

			int shift = range.getOffset();
			int start = shift;

			while (true) {
				int token = scanner.getNextToken();

				start = shift + scanner.getCurrentTokenStartPosition();
				int end = shift + scanner.getCurrentTokenEndPosition() + 1;

				if (!strategy.keepProcessing(token) || token == ITerminalSymbols.TokenNameEOF) {
					break; // end case.
				}

				strategy.handle(token, start, end, elem);

			}

			strategy.postScan(start, elem);

			regionSet = strategy.result();

		}

		if (regionSet == null) {
			regionSet = Collections.EMPTY_SET;
		}
		
		
		
		
		strategy.dispose();

		return regionSet;

	}

}