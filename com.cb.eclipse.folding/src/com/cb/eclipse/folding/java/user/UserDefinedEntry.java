/*
 * Created on Sep 9, 2004
 */
package com.cb.eclipse.folding.java.user;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class UserDefinedEntry {

	private String name;
	private boolean fold;
	private boolean collapse;
	
	public UserDefinedEntry() {
		
	}
	
	public UserDefinedEntry(String name, boolean fold, boolean collapse) {
		setName(name);
		setFold(fold);
		setCollapse(collapse);
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
	 * @return Returns the fold.
	 */
	public boolean isFold() {
		return fold;
	}
	/**
	 * @param fold The fold to set.
	 */
	public void setFold(boolean fold) {
		this.fold = fold;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
