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

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * The Default implementation of the CommentFoldingAdvisor API; this class
 * simply uses the default property types for each comment type.
 * 
 * @author R.J. Lorimer
 */
public class DefaultCommentFoldingAdvisor implements CommentFoldingAdvisor {

	private static DefaultCommentFoldingAdvisor instance = new DefaultCommentFoldingAdvisor();

	private DefaultCommentFoldingAdvisor() {

	}

	public static DefaultCommentFoldingAdvisor getInstance() {
		return instance;
	}

	public boolean shouldCollapseBlockComment() {
		return FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_COMMENT_BLOCKS);
	}

	public boolean shouldCollapseJavadoc() {
		return FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_JAVADOCS);
	}

	public boolean shouldCollapseLineComment() {
		return FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_LINE_COMMENTS);
	}

	public boolean shouldFoldBlockComment() {
		return FoldingPlugin.getBoolean(PreferenceKeys.FOLD_COMMENT_BLOCKS);
	}

	public boolean shouldFoldJavadoc() {
		return FoldingPlugin.getBoolean(PreferenceKeys.FOLD_JAVADOCS);
	}

	public boolean shouldFoldLineComment() {
		return FoldingPlugin.getBoolean(PreferenceKeys.FOLD_LINE_COMMENTS);
	}
}