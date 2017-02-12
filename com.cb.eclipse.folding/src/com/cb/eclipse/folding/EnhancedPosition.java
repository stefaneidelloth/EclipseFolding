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
package com.cb.eclipse.folding;

import org.eclipse.jface.text.Position;

public class EnhancedPosition extends Position {
	private PositionMetadata metadata; 

	
	public EnhancedPosition(int start, int length, PositionMetadata metadata) {
		super(start, length);
		this.metadata = metadata;
	}

	

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("[").append(getOffset()).append(",").append(getLength()).append("] - Metadata: ").append(metadata);
		return result.toString();
	}

	public int getStart() {
		return getOffset();
	}

	public int getEnd() {
		return getOffset() + getLength();
	}
	
	
	/**
	 * Determines if the passed in region intersects this region.
	 * 
	 * Technically speaking, if the passed in region starts after this region,
	 * and ENDS after this region, or vice versa; these two regions are
	 * considered to collide.
	 * 
	 * For example:
	 * 
	 * <pre>
	 * 
	 *      | A-start ...... B-start ...... A-end ...... B-end ...... |
	 *  
	 * </pre>
	 * 
	 * @param other
	 * @return
	 */
	public boolean collidesWith(EnhancedPosition other) {
		boolean collides = false;
		if (other != null) {

			int start = getStart();
			int end = getEnd();

			int otherStart = other.getStart();
			int otherEnd = other.getEnd();

			if (start >= otherStart && start <= otherEnd && end >= otherEnd) {
				collides = true;
			}
			else if (start <= otherStart && end >= otherStart && end <= otherEnd) {
				collides = true;
			}
		}
		return collides;
	}

	public boolean isAdjacent(EnhancedPosition other) {
		
		return getStart() == other.getEnd() || getEnd() == other.getStart() || getEnd() == other.getEnd();
		
	}
	
		
	/**
	 * Determines if the passed in region is contained by this region.
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains(EnhancedPosition other) {
		boolean contains = false;
		if (other != null) {
			int start = getStart();
			int end = getEnd();

			int otherStart = other.getStart();
			int otherEnd = other.getEnd();

			contains = (start < otherStart && end > otherEnd);
		}
		return contains;
	}

	
	/**
	 * @return Returns the metadata.
	 */
	public PositionMetadata getMetadata() {
		return metadata;
	}
	/**
	 * @param metadata The metadata to set.
	 */
	public void setMetadata(PositionMetadata metadata) {
		this.metadata = metadata;
	}
}