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
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import com.cb.eclipse.folding.EnhancedPosition;

/**
 * A simple 'do nothing' Strategy.
 * 
 * @author R.J. Lorimer
 */
public class NoOpStrategy extends AbstractCalculationStrategy {

	private static final EnhancedPosition[] EMPTY = new EnhancedPosition[0];

	public NoOpStrategy() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#shouldScan(org.eclipse.jdt.core.IJavaElement)
	 */
	public boolean shouldScan(IJavaElement elem) throws JavaModelException {
		// Do not scan.
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#handle(int,
	 *      int, int, org.eclipse.jdt.core.IJavaElement)
	 */
	public void handle(int nextToken, int start, int end, IJavaElement owner) {
		// NO-OP

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.calculation.CalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		// NO-OP
		return false;
	}

	public Set result() {
		return Collections.EMPTY_SET;
	}
	
	public void initialize() {
		
	}
	
	public void destroy() {
		
	}
}