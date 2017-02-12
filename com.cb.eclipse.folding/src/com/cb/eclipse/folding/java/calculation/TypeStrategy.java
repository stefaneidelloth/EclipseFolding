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
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.JavaPositionMetadata;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * The Type strategy handles top-level and inner classes.
 * 
 * Because a Type is not a leaf java element, this particular instance performs
 * no scanning operations (everything within the type is handled via child java
 * elements). This implementation does, however, let the scanning occur to
 * ensure the ending position.
 * 
 * @author R.J. Lorimer
 */
public class TypeStrategy extends AbstractCalculationStrategy {

	public TypeStrategy() {
		super();
	}

	/*
	 * TODO investigate using shouldScan and sourceRef.getContents()...
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#shouldScan(org.eclipse.jdt.core.IJavaElement)
	 */
	public void postScan(int position, IJavaElement elem) throws JavaModelException {
		ISourceRange range = getNaturalRange(elem);
		IType type = (IType) elem;

		boolean collapse;
		boolean fold;
		
		boolean negateLine = FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_TYPES);
		if (type.getDeclaringType() != null) { // inner class
			collapse = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_INNER_TYPES);
			fold = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_INNER_TYPES);
		}
		else { // top level type
			collapse = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_TOP_TYPES);
			fold = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_TOP_TYPES);
		}

		if (fold) {
			
			addRegion(new EnhancedPosition(position, range.getOffset() + (range.getLength() - position), new JavaPositionMetadata(false, negateLine, collapse, false, null)));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		return false;
	}
}