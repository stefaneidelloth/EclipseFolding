/*
 * Created on Sep 8, 2004
 */
package com.cb.eclipse.folding.java;

import com.cb.eclipse.folding.PositionMetadata;

/**
 * The JavaPositionMetadata class is an implementation of the PositionMetadata 
 * interface with Java-specific metadata.
 * @author R.J. Lorimer
 */
public class JavaPositionMetadata implements PositionMetadata {
	
	private boolean userDefined;
	private boolean filterLastLine;
	private boolean collapse;
	private boolean overlap;
	private String owner;

	
	public JavaPositionMetadata(boolean userDefined, boolean filterLastLine, boolean collapse, boolean overlaps, String owner) {
		this.userDefined = userDefined;
		this.filterLastLine = filterLastLine;
		this.collapse = collapse;
		this.overlap = overlaps;
		this.owner = owner;
		
	}
	
	/**
	 * @return Returns the collapse.
	 */
	public boolean isCollapse() {
		return collapse;
	}
	/**
	 * @param collapse The collapse to set.
	 */
	public void setCollapse(boolean collapse) {
		this.collapse = collapse;
	}
	/**
	 * @return Returns the filterLastLine.
	 */
	public boolean isFilterLastLine() {
		return filterLastLine;
	}
	/**
	 * @param filterLastLine The filterLastLine to set.
	 */
	public void setFilterLastLine(boolean filterLastLine) {
		this.filterLastLine = filterLastLine;
	}
	/**
	 * @return Returns the overlap.
	 */
	public boolean isOverlap() {
		return overlap;
	}
	/**
	 * @param overlap The overlap to set.
	 */
	public void setOverlap(boolean overlap) {
		this.overlap = overlap;
	}
	/**
	 * @return Returns the userDefined.
	 */
	public boolean isUserDefined() {
		return userDefined;
	}
	/**
	 * @param userDefined The userDefined to set.
	 */
	public void setUserDefined(boolean userDefined) {
		this.userDefined = userDefined;
	}
	
		
	/**
	 * @return Returns the owner.
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
