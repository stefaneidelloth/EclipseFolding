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

/**
 * A CalculationStrategy is an approach taken for a particular IJavaElement to
 * process providing the regions that will be folded by the editor.
 * 
 * The CalculationStrategy is closely linked to the IScanner and
 * ITerminationSymbols APIs (these classes provide the token info).
 * 
 * RegionCalculationStrategies are applied to the JavaProjectionCalculator class given 
 * the StrategyFactory class (which filters 
 * 
 * The algorithm applied to region calculation strategies is as follows:
 * <ol>
 * <li>Ask the RegionCalculationStrategy if scanning should occur via <code>shouldScan</code></li>
 * <li>Begin scanning the document... for each token:
 *     <ol>
 *     <li>Ask the strategy if scanning should continue via <code>keepProcessing</code></li>
 *     <li>Tell the strategy to handle the token via <code>handle</code></li>
 *     </ol>
 * <li>Call postScan on the strategy allowing it to clean up any mid-scan items</li>
 * <li>Calls result on the strategy to get the regions collected during the scan.</li>
 * </ol> 
 * 
 * @author R.J. Lorimer
 */
public interface RegionCalculationStrategy {

	/**
	 * Determine if the next token in the list is appropriate for this
	 * calculation strategy, or if processing should halt.
	 * 
	 * @param nextToken
	 * @return true if the token applies to this CalculationStrategy
	 */
	boolean keepProcessing(int nextToken);

	/**
	 * Determine if the scanning process should be performed at all for the
	 * given Java element. If not, please note that <code>result()</code> will
	 * still be called!
	 * 
	 * @param elem
	 * @return true if the scanning process should commence for the provided
	 *         elem.
	 * @throws JavaModelException
	 */
	boolean shouldScan(IJavaElement elem) throws JavaModelException;

	/**
	 * The handle method is called mid-scan and represents a token in sequence
	 * in the scanning process.
	 * 
	 * The token is a constant from the ITerminalSymbols class.
	 * 
	 * @param nextToken
	 *            a token from the ITerminalSymbols class.
	 * @param start
	 *            The start of the token (measured in editor characters)
	 * @param end
	 *            The end of the token (measured in editor characters)
	 * @param owner
	 *            The IJavaElement the tokens are being provided by.
	 * @throws JavaModelException
	 */
	void handle(int nextToken, int start, int end, IJavaElement owner) throws JavaModelException;

	/**
	 * postScan() is called immediately after the scanning process completes.
	 * 
	 * Completion of the scanning process can be defined by an EOF token being
	 * reached, or by this CalculationStrategy explicitly stopping processing
	 * via <code>keepProcessing()</code> returning <code>false</code>.
	 * 
	 * @param position
	 * @param elem
	 * @throws JavaModelException
	 */
	void postScan(int position, IJavaElement elem) throws JavaModelException;

	/**
	 * After all scanning and processing is done, this method returns the result
	 * in terms of multiple regions.
	 * 
	 * @return A Region for each folding structure provided by this strategy.
	 * @throws JavaModelException
	 */
	Set result() throws JavaModelException;
	
	void initialize();
	void dispose();

}