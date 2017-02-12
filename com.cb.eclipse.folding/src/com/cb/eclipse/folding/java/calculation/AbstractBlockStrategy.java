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

import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.JavaPositionMetadata;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;
import com.cb.eclipse.folding.util.IntStack;

/**
 * A generic implementation of CalculationStrategy that processes based on block
 * identifiers (e.g. { and } ).
 * 
 * This class provides nested block handling capabilities by using stack
 * mathematics.
 * 
 * @author RJ
 */
public abstract class AbstractBlockStrategy extends AbstractCalculationStrategy {

	
	private IntStack braceStack = new IntStack(); 
	private boolean digestComments = false;

	private int lastValidOpener = -1;

	private CommentHelper helper;

	public AbstractBlockStrategy(boolean digestComments) {
		super();

		this.helper = new CommentHelper();

		this.digestComments = digestComments;
		
		

	}

	/**
	 * Push left braces, pop right braces and calculate the distance in between
	 * as a region.
	 * 
	 * @see com.cb.eclipse.folding.java.calculation.RegionCalculationStrategy#handle(int,
	 *      int, int, org.eclipse.jdt.core.IJavaElement)
	 */
	public void handle(int token, int start, int end, IJavaElement owner) throws JavaModelException {

		boolean collapseComments = FoldingPlugin.getPrefs().getBoolean(PreferenceKeys.COLLAPSE_COMMENT_BLOCKS);
		boolean collapseJavadoc = FoldingPlugin.getPrefs().getBoolean(PreferenceKeys.COLLAPSE_JAVADOCS);
		
		switch (token) {

			case ITerminalSymbols.TokenNameLBRACE: {
				pushBrace(start);
				helper.end();
				break;
			}
			case ITerminalSymbols.TokenNameRBRACE: {
				if(hasUsablePeer()) {
					
					int oldStart = popBrace();
					helper.end();
					if (shouldFold(owner, token)) {
						boolean doCollapse = shouldCollapse(owner, token);
						boolean shouldNegate = shouldFilterLastLine(owner, token);
						addRegion(new EnhancedPosition(oldStart, end - oldStart, new JavaPositionMetadata(false, shouldNegate, doCollapse, true, getClass().getName())));
					}
				}
				else {
					System.out.println("Bad brace found... omitting!");
				}
				
				break;
			}

			default: {
				if (digestComments) helper.handle(token, start, end, owner);

			}

		}

	}

	

	/**
	 * Currently designed to process until EOF is hit.
	 * 
	 * @see com.cb.eclipse.folding.java.calculation.CalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		return true;
	}

	/**
	 * Push another left brace location on to the stack, and grow the left-brace
	 * location stack if neccessary.
	 * 
	 * @param location
	 */
	private void pushBrace(int location) {

		braceStack.push(location);
	}

	/**
	 * Pop a left-brace location off of the stack.
	 * 
	 * @return a left brace location.
	 */
	private int popBrace() {
		int location;
		if(isEmpty()) {
			location = lastValidOpener;
		}
		else {
			location = braceStack.pop();
			if(isEmpty()) {
				lastValidOpener = location;
			}
		}
		return location;
					
		
	}
	
	/**
	 * Ensures that one way or another the brace being matched has a usable peer.
	 * 
	 * This may mean that in the case of mismatched braces we are looking for the broadest 
	 * set of braces, or it may simply mean that it is looking for a matching brace.
	 * @return
	 */
	private boolean hasUsablePeer() {
		return !isEmpty() || lastValidOpener >= 0;
	}

	/**
	 * Determine if we have any braces pushed on the stack.
	 * 
	 * @return
	 */
	protected boolean isEmpty() {
		return braceStack.isEmpty();
	}

	protected int getLevel() {
		return braceStack.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#result()
	 */
	public Set result() throws JavaModelException {
		super.addAllRegions(helper.result());
		return super.result();
	}

	public abstract boolean shouldFold(IJavaElement owner, int token) throws JavaModelException;
	public abstract boolean shouldFilterLastLine(IJavaElement owner, int token) throws JavaModelException;
	public abstract boolean shouldCollapse(IJavaElement owner, int token) throws JavaModelException;
}