/*
 * Created on Sep 21, 2004
 */
package com.cb.eclipse.folding.java.user;


/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class UserDefinedSettingsEvent {

	public static final int ADDED = 0;
	public static final int UPDATED = 1;
	public static final int DELETED = 2;
	
	private UserDefinedEntry entry;
	private int code;
	public UserDefinedSettingsEvent(UserDefinedEntry entry, int code) {
		this.entry = entry;
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	public UserDefinedEntry getEntry() {
		return entry;
	}
}
