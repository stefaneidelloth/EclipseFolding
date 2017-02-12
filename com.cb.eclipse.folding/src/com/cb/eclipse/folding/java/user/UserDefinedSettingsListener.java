/*
 * Created on Sep 21, 2004
 */
package com.cb.eclipse.folding.java.user;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public interface UserDefinedSettingsListener {

	void entryAdded(UserDefinedSettingsEvent evt);
	void entryUpdated(UserDefinedSettingsEvent evt);
	void entryDeleted(UserDefinedSettingsEvent evt);
}
