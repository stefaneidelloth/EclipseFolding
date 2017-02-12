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
package com.cb.eclipse.folding.java;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;

import com.cb.eclipse.folding.CBProjectionAnnotation;
import com.cb.eclipse.folding.EnhancedPosition;


/**
 * A Java projection object.
 * 
 * @author RJ
 */
public class JavaProjectionAnnotation extends CBProjectionAnnotation {

	
	private EnhancedPosition position;
	private IJavaElement owner;
	
	public JavaProjectionAnnotation(int lineCount, EnhancedPosition pos, IJavaElement elem, boolean isCollapse) {
		super(isCollapse);
		position = pos;
		owner = elem;
	}

	public String toString() {
		String source = computeSource();
		String type = owner.getClass().getName();
		return type.substring(type.lastIndexOf('.') + 1);
	}
	
	
	private String computeSource() {
		StringBuffer result = new StringBuffer();
		try {
			ISourceReference ref = (ISourceReference) owner;
			ISourceRange range = ref.getSourceRange();
			int sourceOffset = range.getOffset();
			int sourceLen = range.getLength();
			int offset = position.getOffset();
			int len = position.getLength();

			offset -= sourceOffset;
			if (offset < 0) {
				offset = 0;
				result.append("Position precedes element!\n");
			}
			if (len > sourceLen) {
				len = sourceLen;
				result.append("Position extends past element!\n");
			}
			if (len < 0) {
				len = 0;
				result.append("Length was less than 0!\n");
			}
			result.append(ref.getSource().substring(offset, offset + len));

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();

	}
	
	
	
	
}