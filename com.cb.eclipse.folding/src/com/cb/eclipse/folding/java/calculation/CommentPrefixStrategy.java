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

/**
 * A Composite strategy that filters off comments that are preceding the core of
 * any IJavaElement type.
 * 
 * A common occurance in the IJavaElement API is for a particular IJavaElement
 * (types, methods, and fields in particular) to inherit all comments that
 * precede them.
 * 
 * This class allows for the logic bound to those comment-laden types to focus
 * on the type itself, and let the comments be managed by this outside
 * intermediary.
 * 
 * Once processing completes, the CommentPrefixStrategy will combine all comment
 * regions and all regions provided by the underlying composite into a single
 * array.
 * 
 * @author R.J. Lorimer
 */
public class CommentPrefixStrategy extends CompositeCalculationStrategy {

	private CommentHelper helper;

	private boolean endReached;

	private boolean justEnded;

	public CommentPrefixStrategy(RegionCalculationStrategy composite) {
		super(composite);
		helper = new CommentHelper();
	}

	/**
	 * Process all comments, or, if a type is found, delegate the request to the
	 * composite.
	 * 
	 * @see com.cb.eclipse.folding.java.calculation.CalculationStrategy#handle(int)
	 */
	public void handle(int token, int start, int end, IJavaElement owner) throws JavaModelException {
		if (endReached) {

			// Give the helper a chance to tie up loose ends.
			if (justEnded) {
				justEnded = false;
				helper.end();
			}
			super.handle(token, start, end, owner);
		}
		else {

			helper.handle(token, start, end, owner);
		}

	}

	/**
	 * Make a determination if the token is a comment or some type information.
	 * If the token belongs to a type, disable the comment strategy, and enable
	 * the underlying composite.
	 * 
	 * @see com.cb.eclipse.folding.java.calculation.CalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		boolean keepGoing = false;

		if (!endReached) {
			// end IS NOW reached
			if (!representsComment(nextToken)) {
				endReached = true;
				justEnded = true;
				keepGoing = super.keepProcessing(nextToken);

				// Give the helper a chance to clean up loose ends
				// handle may not be called again...
				if (!keepGoing) {
					helper.end();
				}
			}
			// end is STILL NOT reached
			else {
				keepGoing = true;
			}
		}
		// end has ALREADY BEEN reached
		else {
			keepGoing = super.keepProcessing(nextToken);
		}

		return keepGoing;

	}

	private boolean representsComment(int token) {
		return (token == ITerminalSymbols.TokenNameCOMMENT_BLOCK) || (token == ITerminalSymbols.TokenNameCOMMENT_JAVADOC)
				|| (token == ITerminalSymbols.TokenNameCOMMENT_LINE);
	}

	/**
	 * Combines comment positions with positions from the composite. 
	 * @see com.cb.eclipse.folding.java.calculation.CalculationStrategy#result()
	 */
	public Set result() throws JavaModelException {
		Set comments = helper.result();
		Set wrapped = super.result();
		comments.addAll(wrapped);
		return comments;

	}

	


	

}