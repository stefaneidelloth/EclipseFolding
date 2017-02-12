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
 * An Exception that occurs during the reconciliation process (matching foldable
 * regions up after user changes).
 * 
 * @author R.J. Lorimer
 */
public class ReconciliationException extends RuntimeException {

	// {{ Default constructors
	// {{ sub constructors
	public ReconciliationException(String message) {
		super(message);
	}

	public ReconciliationException(String message, Throwable cause) {
		super(message, cause);

	}
	// }}
	// }}
	// {{ Another constructor
	
	// }}
}