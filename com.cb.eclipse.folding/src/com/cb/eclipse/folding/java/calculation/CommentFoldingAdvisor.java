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

/**
 * API for a class that advises the CommentHelper on 
 * what types of comments should be collapsed and folded based 
 * on the context of the CommentFoldingAdvisor itself.
 * 
 * @author R.J. Lorimer
 */
public interface CommentFoldingAdvisor {

	boolean shouldCollapseBlockComment();

	boolean shouldCollapseLineComment();

	boolean shouldCollapseJavadoc();

	boolean shouldFoldBlockComment();

	boolean shouldFoldLineComment();

	boolean shouldFoldJavadoc();

}