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

/**
 * A convenience class for profiling code.
 * 
 * This is NOT intended for production use.
 * @author R.J. Lorimer
 */
public class StopWatch {
	private long start;
	
	
	private String name;	
	
	public StopWatch() {
		
	}
	
	public StopWatch(String msg) {
		name = msg;
	}
	
	public void start() {
		start = System.currentTimeMillis();
	}
	public long end() {
		long result;
		if(start == 0) {
			result = 0;
		}
		else {
			result = System.currentTimeMillis() - start;
		}
		reset();
		return result;
	}
	public long endAndReport() {
		long result = end();
		StringBuffer msg = new StringBuffer();
		if(name != null) {
			msg.append(name).append(": ");
		}
		msg.append(result + "ms - ");
		msg.append((result / 1000) + " seconds");
		System.out.println(msg);
		return result;		
	}
	
	
	
	public void reset() {		
		start = 0;
	}

}
