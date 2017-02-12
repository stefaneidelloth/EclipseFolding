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
package com.cb.eclipse.folding.util;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

/**
 * JDTUtil fills in the blanks for convenience methods regarding Eclipse Java
 * elements.
 * 
 * @author R.J. Lorimer
 */
public final class JDTUtil {

	public static boolean isGetter(IMethod method) throws JavaModelException {
		
		String methodName = method.getElementName();
		return methodName.startsWith("get") || methodName.startsWith("is");		
	}

	public static boolean isSetter(IMethod method) throws JavaModelException {
		String methodName = method.getElementName();
		boolean isSetter = methodName.startsWith("set");
		
		return isSetter;
	}

	public static boolean isGetterOrSetter(IMethod method) throws JavaModelException {
		return isGetter(method) || isSetter(method);
	}

}