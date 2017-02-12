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

import java.lang.reflect.Field;

import org.eclipse.jdt.core.compiler.ITerminalSymbols;

/**
 * Uses reflection to print out the name of a token from the ITerminalSymbols 
 * class given the token value.
 * 
 * This is NOT intended for production use.
 * @author R.J. Lorimer
 */
public class TerminalSymbolsDictator {

	public static String dictate(int token) {
		try {
			String tokenName;

			Field[] allTokens = ITerminalSymbols.class.getFields();
			for (int i = 0; i < allTokens.length; i++) {
				Field aField = allTokens[i];
				int fieldValue = ((Integer) aField.get(null)).intValue();
				if (token == fieldValue) { return aField.getName(); }
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "<NO TOKEN!>";
	}
}