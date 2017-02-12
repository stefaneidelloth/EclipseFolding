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

// TODO - Make as many strategies as possible stateless.

/**
 * The StrategyFactory as defined here maps an IJavaElement to a
 * CalculationStrategy. This allows the projection calculator to focus on the
 * algorithm of processing a tree of IJavaElement instances exclusively.
 * 
 * @author R.J. Lorimer
 */
public class StrategyFactory {

	// NO-OP singleton.
	private static RegionCalculationStrategy NO_OP_STRATEGY = new NoOpStrategy();

	/**
	 * Get a CalculationStrategy instance that is capable of handling the region
	 * building for <code>elem</code>
	 * 
	 * @param elem
	 *            The java element to find a calculation strategy for.
	 * @return The Calculation strategy meant for processing elem.
	 */
	public static RegionCalculationStrategy instance(JavaProjectionCalculator calculator, IJavaElement elem) {
		int type = elem.getElementType();
		switch (type) {
			case IJavaElement.TYPE:
				return new CommentPrefixStrategy(new TypeStrategy());
			case IJavaElement.FIELD:
			case IJavaElement.METHOD:
				return new CommentPrefixStrategy(new MethodStrategy());
			case IJavaElement.IMPORT_CONTAINER:
				return new ImportContainerStrategy();
			case IJavaElement.COMPILATION_UNIT:
			case IJavaElement.CLASS_FILE:
				return new UserDefinedStrategy(new RootStrategy());
			case IJavaElement.INITIALIZER:
				return new CommentPrefixStrategy(new StaticInitializerStrategy());
			default:
				return NO_OP_STRATEGY;
		}
	}

}