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

import java.util.Stack;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;

import com.cb.eclipse.folding.EnhancedPosition;
import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.JavaPositionMetadata;
import com.cb.eclipse.folding.java.user.UserDefinedSettings;

/**
 * A strategy for dealing with user defined folding regions.
 * @author R.J. Lorimer
 */
public class UserDefinedStrategy extends CompositeCalculationStrategy {

	private boolean keepProcessingChild;
	private Stack trackers;
	private String[] matchingTags;
	private UserDefinedSettings settings;
	private UserDefinedRegionHelper helper;
	
	
	public UserDefinedStrategy(RegionCalculationStrategy wrapped) {
		super(wrapped);
		
		helper = new UserDefinedRegionHelper();
		settings = FoldingPlugin.getJavaDomain().getUserDefinedSettings();
		matchingTags = settings.getNames();
		trackers = new Stack();
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#handle(int, int, int, org.eclipse.jdt.core.IJavaElement)
	 */
	public void handle(int nextToken, int start, int end, IJavaElement owner) throws JavaModelException {
		if(settings.isDefaultEnabled()) {
			if(nextToken == ITerminalSymbols.TokenNameCOMMENT_LINE) {
				if(helper.isOpeningSentinel(start, end, owner)) {
					trackers.push(new RegionTracker(null, start));
				}
				else if(helper.isClosingSentinel(start, end, owner)) {
					int matchedStart;
					if(!trackers.isEmpty()) {
						RegionTracker tracker = (RegionTracker)trackers.pop();
						matchedStart = tracker.start;
						boolean doCollapse = settings.isDefaultCollapsed();
						EnhancedPosition newPos = new EnhancedPosition(matchedStart, end-matchedStart, new JavaPositionMetadata(true, true, doCollapse, true, null));					
						super.addRegion(newPos);
					}
						
				}
			}
		}
		if(keepProcessingChild) {			
			super.handle(nextToken, start, end, owner);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#keepProcessing(int)
	 */
	public boolean keepProcessing(int nextToken) {
		if(keepProcessingChild)
			keepProcessingChild = super.keepProcessing(nextToken);
		
		return true;
	}
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#shouldScan(org.eclipse.jdt.core.IJavaElement)
	 */
	public boolean shouldScan(IJavaElement elem) throws JavaModelException {
		keepProcessingChild = super.shouldScan(elem);
		return true;
	}
	
		
	private static class RegionTracker {
		
		String name;
		int start;
		
		RegionTracker(String newName, int newStart) {
			name = newName;
			start = newStart;
		}
		
		
	}
	
	
}
