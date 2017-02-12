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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * The RootStrategy is a special-case scenario for dealing with top-level
 * 'compilation unit' style java elements.
 * 
 * This class is essentially responsible for finding all comments not tied to
 * any other strategy (e.g. type, method, etc)
 * 
 * @author R.J. Lorimer
 */
public class RootStrategy extends AbstractCalculationStrategy implements CommentFoldingAdvisor {

	private CommentHelper helper = new CommentHelper(this);
	private CommentFoldingAdvisor delegate = DefaultCommentFoldingAdvisor.getInstance();

	public RootStrategy() {
		super();
	}

	/**
	 * Process until we hit the 'class' identifier. That is the sign that we've
	 * actually found the beginning of the 'type'. We throw away any remaining
	 * regions that weren't captured by previous identifier collisions, because
	 * they will be assigned to the type by Eclipse.
	 * 
	 * @see com.cb.eclipse.folding.java.calculation.RegionCalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		
		if (nextToken == ITerminalSymbols.TokenNameclass || nextToken == ITerminalSymbols.TokenNameinterface) {			
			helper.reset();
			return false;
		}
		return true;
	}

	/**
	 * Process all of the tokens searching for javadocs and comment blocks. Each
	 * region discovered is held on to temporarily.
	 * 
	 * If a package statement or import statement is found we flush the
	 * temporary regions into the permanant region collection, because
	 * everything above those identifiers belongs only to the RootStrategy.
	 * 
	 * @see com.cb.eclipse.folding.java.calculation.RegionCalculationStrategy#handle(int,
	 *      int, int, org.eclipse.jdt.core.IJavaElement)
	 */
	public void handle(int token, int start, int end, IJavaElement owner) throws JavaModelException {

		
		helper.handle(token, start, end, owner);
		
		switch (token) {

			// purge the temp regions - we hit a element break.
			case ITerminalSymbols.TokenNamepackage:
			case ITerminalSymbols.TokenNameimport: {
				addAllRegions(helper.result());
				return;
			}

		}

	}

	/** 'override' the default collapse lookup to handle source headers */
	public boolean shouldCollapseBlockComment() {
		return FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_HEADER);
	}

	/** 'override' the default fold lookup to handle source headers */
	public boolean shouldFoldBlockComment() {
		return FoldingPlugin.getBoolean(PreferenceKeys.FOLD_HEADER);
	}

	/* the other collapse/fold advice is standard */
	public boolean shouldCollapseJavadoc() {
		return delegate.shouldCollapseJavadoc();
	}

	public boolean shouldCollapseLineComment() {
		return delegate.shouldCollapseLineComment();
	}

	public boolean shouldFoldJavadoc() {
		return delegate.shouldFoldJavadoc();
	}

	public boolean shouldFoldLineComment() {
		return delegate.shouldFoldLineComment();
	}
}