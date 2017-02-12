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

import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import com.cb.eclipse.folding.java.calculation.RegionCalculationStrategy;

/**
 * A strategy object for profiling strategy performance.
 * 
 * This is NOT intended for production use.
 * @author R.J. Lorimer
 */
public class ProfilingStrategy implements RegionCalculationStrategy {
	
	private RegionCalculationStrategy childStrategy;
	private String name;
	private long totalTime;

	public ProfilingStrategy(RegionCalculationStrategy child) {
		childStrategy = child;
	}
	public ProfilingStrategy(RegionCalculationStrategy child, String nme) {
		childStrategy = child;
		name = nme;
	}
	
	public void handle(int nextToken, int start, int end, IJavaElement owner) throws JavaModelException {
		long startTime = captureTime();
		childStrategy.handle(nextToken, start, end, owner);
		long endTime = captureTime();
		notify("handle", startTime, endTime);
		
	}
	
	public boolean keepProcessing(int nextToken) {
		long startTime = captureTime();
		boolean keepProcessing = childStrategy.keepProcessing(nextToken);
		long endTime = captureTime();
		
		notify("keepProcessing", startTime, endTime);
		return keepProcessing;
	}

	public void postScan(int position, IJavaElement elem) throws JavaModelException {
		long startTime = captureTime();
		childStrategy.postScan(position, elem);
		long endTime = captureTime();
		
		notify("postScan", startTime, endTime);

	}

	public Set result() throws JavaModelException {
		long startTime = captureTime();
		Set result = childStrategy.result();
		long endTime = captureTime();
		notify("result", startTime, endTime);
		return result;
	}
	
	public boolean shouldScan(IJavaElement elem) throws JavaModelException {
		long startTime = captureTime();
		boolean shouldScan = childStrategy.shouldScan(elem);
		long endTime = captureTime();
		notify("shouldScan", startTime, endTime);
		return shouldScan;
	}
	
	private long captureTime() {
		return System.nanoTime();
	}
	private void notify(String method, long start, long end) {
		totalTime += (end-start);
		
		StringBuffer bfr = new StringBuffer();
		if(name != null)
			bfr.append(name).append(' ');
		
		bfr.append(method).append(": ").append(end-start).append(" nanos.");
		
		System.out.println(bfr);
	}
	
	
	
	public void dispose() {
		notify("overall", 0, totalTime);
	}
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#initialize()
	 */
	public void initialize() {
		totalTime = 0;
	}
}
