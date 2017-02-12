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
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.JavaPositionMetadata;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * A Simple strategy for wrapping the import container Java Element with a
 * single folding structure.
 * 
 * @author R.J. Lorimer
 */
public class ImportContainerStrategy extends AbstractCalculationStrategy {

	public ImportContainerStrategy() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#shouldScan(org.eclipse.jdt.core.IJavaElement)
	 */
	public boolean shouldScan(IJavaElement elem) throws JavaModelException {
		ISourceRange range = getNaturalRange(elem);

		if (FoldingPlugin.getBoolean(PreferenceKeys.FOLD_IMPORTS)) {
			boolean doCollapse = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_IMPORTS);
			// get a region for the entire range of the IJavaElement
			addRegion(new EnhancedPosition(range.getOffset(), range.getLength(), new JavaPositionMetadata(false, false, doCollapse, false, null)));
		}
		// don't scan.
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#handle(int,
	 *      int, int, org.eclipse.jdt.core.IJavaElement)
	 */
	public void handle(int nextToken, int start, int end, IJavaElement owner) throws JavaModelException {
		// No Op
		// There is no scanning to occur.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		// NO-OP
		// Import containers are a single token.
		return false;
	}
}