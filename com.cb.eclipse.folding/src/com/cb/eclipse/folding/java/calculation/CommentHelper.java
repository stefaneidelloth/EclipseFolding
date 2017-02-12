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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.java.JavaPositionMetadata;

/**
 * The CommentHelper class is just a common location for the core logic of
 * processing comments and producing comment folding regions.
 * 
 * One of the key components of the comment helper class is the comment folding
 * advisor, which is the client provided class the provides the knowledge of
 * whether certain regions should be foldable and/or collapsed initially.
 * Because the comment helper class is generic in nature, the advisor allows for
 * pluggable scenarios.
 * 
 * @author R.J. Lorimer
 */
public class CommentHelper {

	private Set regions = new HashSet();

	private int lineCommentCount;

	private int lineCommentStart;

	private int lineCommentEnd;
	
	private CommentFoldingAdvisor advisor;
	private UserDefinedRegionHelper userDefinedRecognizer;

	public CommentHelper() {
		this(DefaultCommentFoldingAdvisor.getInstance());
	}

	public CommentHelper(CommentFoldingAdvisor advisor) {
		this.advisor = advisor;
		userDefinedRecognizer = new UserDefinedRegionHelper();
	}

	public void handle(int token, int start, int end, IJavaElement owner) throws JavaModelException {
		switch(token) {
			
			case ITerminalSymbols.TokenNameCOMMENT_BLOCK:
				handleCommentBlock(start, end);
				break;
			case ITerminalSymbols.TokenNameCOMMENT_JAVADOC:
				handleJavadoc(start, end);
				break;
			default:
				if(ITerminalSymbols.TokenNameCOMMENT_LINE == token && !isUserDefinedSentinel(token, start, end, owner)) {
					handleCommentLine(start, end);
				}
				else {
					handleNonCommentToken(token);
				}
				break;
		}
	}

	public void end() {
		closeOpenComments();
	}

	public Set result() {
		return regions;
	}

	public void reset() {
		regions.clear();
	}
	
	private boolean isUserDefinedSentinel(int token, int start, int end, IJavaElement owner) throws JavaModelException {
		return userDefinedRecognizer.isOpeningSentinel(start, end, owner) || userDefinedRecognizer.isClosingSentinel(start, end, owner);
	}

	private void handleCommentLine(int startPos, int endPos) {

		if (lineCommentCount == 0) {
			lineCommentStart = startPos;

		}
		lineCommentEnd = endPos;
		lineCommentCount++;

	}

	private void handleCommentBlock(int start, int end) {

		if (advisor.shouldFoldBlockComment()) {
			boolean collapse = advisor.shouldCollapseBlockComment();
			regions.add(new EnhancedPosition(start, end - start, getMetadata(collapse)));
		}
	}

	private void handleJavadoc(int start, int end) {
		if (advisor.shouldFoldJavadoc()) {
			boolean collapse = advisor.shouldCollapseJavadoc();
			regions.add(new EnhancedPosition(start, end - start, getMetadata(collapse)));
		}
	}

	private void handleNonCommentToken(int token) {

		closeOpenComments();
	}

	private void closeOpenComments() {
		if (lineCommentCount > 1) {

			if (advisor.shouldFoldLineComment()) {
				boolean collapse = advisor.shouldCollapseLineComment();
				regions.add(new EnhancedPosition(lineCommentStart, lineCommentEnd - lineCommentStart - 1, getMetadata(collapse)));
			}
			
		}
		lineCommentCount = 0;		
		
	}

	public String toString() {
		return "Comment Helper - regions: " + result().size();
	}

	private JavaPositionMetadata getMetadata(boolean collapse) {
		return new JavaPositionMetadata(false, false, collapse, false, null);
	}
	
}