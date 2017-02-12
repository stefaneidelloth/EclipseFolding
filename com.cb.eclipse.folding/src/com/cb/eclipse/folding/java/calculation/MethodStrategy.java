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
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;
import com.cb.eclipse.folding.util.JDTUtil;

/**
 * This strategy deals specifically with Methods.
 * 
 * This class is heavily reliant on the GenericBlockStrategy super class.
 * 
 * @author RJ
 */
public class MethodStrategy extends AbstractBlockStrategy {

	private static final int COLLAPSE = 0;
	private static final int FOLD = 99;
	
	

	public MethodStrategy() {
		super(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.GenericBlockStrategy#shouldCollapse(int)
	 */
	public boolean shouldCollapse(IJavaElement owner, int token) throws JavaModelException {
		return shouldDo(isEmpty(), COLLAPSE, owner, token);
	}

	public boolean shouldFold(IJavaElement owner, int token) throws JavaModelException {
		return shouldDo(isEmpty(), FOLD, owner, token);
	}
	
	public boolean shouldFilterLastLine(IJavaElement owner, int token) throws JavaModelException {
		if(isEmpty()) {
			return FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_METHODS);
		}
		else {
			return FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_CONTROLS);
		}
	}

	private boolean shouldDo(boolean isEmpty, int type, IJavaElement owner, int token) throws JavaModelException {

		boolean shouldDo = false;
		if (owner instanceof IMethod) {
			IMethod method = (IMethod) owner;
			if (isEmpty) {
				if (isConstructor(method)) { // constructor
					if (type == COLLAPSE) {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_CONSTRUCTORS);
					}
					else {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_CONSTRUCTORS);
					}
				}
				else if (isMainMethod(method)) { // main method
					if (type == COLLAPSE) {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_MAIN_METHODS);
					}
					else {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_MAIN_METHODS);
					}
				}
				else if (isGetterOrSetter(method)) {					
					if (type == COLLAPSE) {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_GETTERS_SETTERS);
					}
					else {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_GETTERS_SETTERS);
					}
				}
				else {
					if (type == COLLAPSE) {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_METHODS);
					}
					else {
						shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_METHODS);
					}
				}
			}
			else {
				// inner types OR
				// TODO try/catch/switch/if-else, etc...
				if (type == COLLAPSE) {
					// never auto-collapse the children until we
					// implement this feature.
					shouldDo = false;
				}
				else {
					// for now call recursively to see what we'd do if we WERE
					// empty
					shouldDo = shouldDo(true, type, owner, token);
				}
			}
		}
		// not a method? why are we being consulted.
		return shouldDo;
	}
	
	private boolean isMainMethod(IMethod elem) throws JavaModelException {
	//	StopWatch watch = new StopWatch("isMainMethod");
//		watch.start();
		boolean result = elem.isMainMethod();
	//	watch.endAndReport();
		return result;
	}
	
	private boolean isConstructor(IMethod elem) throws JavaModelException {
//		StopWatch watch = new StopWatch("isConstructor");
//		watch.start();
		boolean result = elem.isConstructor();
//		watch.endAndReport();
		return result;
	}
	private boolean isGetterOrSetter(IMethod elem) throws JavaModelException {
//		StopWatch watch = new StopWatch("isGetterOrSetter");
//		watch.start();
		boolean result = JDTUtil.isGetterOrSetter(elem);
//		watch.endAndReport();
		return result;		
	}

}