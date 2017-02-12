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

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * This strategy deals specifically with static initializers.
 * 
 * This class is heavily reliant on the logic in the GenericBlockStrategy
 * superclass.
 * 
 * @author RJ
 */
public class StaticInitializerStrategy extends AbstractBlockStrategy {

	public StaticInitializerStrategy() {
		super(true);
	}

	
	public boolean shouldFilterLastLine(IJavaElement owner, int token) throws JavaModelException {
		if(isEmpty()) {
			return FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_METHODS);
		}
		else {
			return FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_CONTROLS);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.GenericBlockStrategy#shouldCollapse(int)
	 */
	public boolean shouldCollapse(IJavaElement owner, int token) {
		boolean shouldCollapse = true;
		if (isEmpty()) {
			shouldCollapse = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_STATICS);
		}
		else {
			shouldCollapse = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_STATICS); // TODO:
			// try/catch/switch/if-else/etc.
		}

		return shouldCollapse;
	}

	public boolean shouldFold(IJavaElement owner, int token) {
		boolean shouldFold = false;
		if (isEmpty()) {
			shouldFold = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_STATICS);
		}
		return shouldFold;
	}
}