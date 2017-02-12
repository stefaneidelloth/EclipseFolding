/*
 * Created on Sep 9, 2004
 */
package com.cb.eclipse.folding.java.user;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;



/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class UserDefinedSettings {

	private IPreferenceStore preferences;
	private Map userDefinedEntries;
	private UserDefinedReaderWriter readerWriter;
	
	private Set listeners;
	
	private int regionCount;
	
	
	public UserDefinedSettings(IPreferenceStore prefs) {
		preferences = prefs;
		readerWriter = new UserDefinedReaderWriter();
		userDefinedEntries = new HashMap();
		listeners = new LinkedHashSet();
		load();
	}
		
	public UserDefinedEntry addType(String name, boolean isEnabled, boolean isCollapsed) {
		if(containsEntry(name)) {
			throw new IllegalArgumentException("Type already exists!");
		}
		UserDefinedEntry entry = new UserDefinedEntry();
		entry.setName(name);
		entry.setFold(isEnabled);
		entry.setCollapse(isCollapsed);
		userDefinedEntries.put(name, entry);
		
		fireEvent(entry, UserDefinedSettingsEvent.ADDED);
		return entry;
	}
	
	public boolean containsEntry(String name) {
		return userDefinedEntries.containsKey(name);
	}
	
	public UserDefinedEntry updateType(String name, boolean isEnabled, boolean isCollapsed) {
		if(!containsEntry(name)) {
			throw new IllegalArgumentException("Type doesn't exist");
		}
		UserDefinedEntry entry = getEntry(name);
		entry.setFold(isEnabled);
		entry.setCollapse(isCollapsed);
		
		fireEvent(entry, UserDefinedSettingsEvent.UPDATED);
		
		return entry;
		
		
	}
	
	public void entryChanged(UserDefinedEntry entry) {
		fireEvent(entry, UserDefinedSettingsEvent.UPDATED);
	}
	
	public void deleteType(String name) {
		UserDefinedEntry elGoneO = (UserDefinedEntry)userDefinedEntries.remove(name);
		fireEvent(elGoneO, UserDefinedSettingsEvent.DELETED);
	}
	
	public String[] getNames() {
		String[] result = new String[userDefinedEntries.size()];
		userDefinedEntries.keySet().toArray(result);
		return result;
	}
	
	private void fireEvent(UserDefinedEntry entry, int code) {
		UserDefinedSettingsEvent event = new UserDefinedSettingsEvent(entry, code);		
		for (Iterator shmiterator = listeners.iterator(); shmiterator.hasNext();) {
			UserDefinedSettingsListener listener = (UserDefinedSettingsListener) shmiterator.next();
			switch(code) {
				case UserDefinedSettingsEvent.ADDED:
					listener.entryAdded(event); break;
				case UserDefinedSettingsEvent.UPDATED:
					listener.entryUpdated(event); break;
				case UserDefinedSettingsEvent.DELETED:
					listener.entryDeleted(event); break;
			}
			
		}
	}
	public void addListener(UserDefinedSettingsListener listener) {
		listeners.add(listener);
	}
	public void removeListener(UserDefinedSettingsListener listener) {
		listeners.remove(listener);
	}
		
	public List getEntries() {
		return new ArrayList(userDefinedEntries.values());
	}
	
	public boolean isDefaultEnabled() {
		return FoldingPlugin.getBoolean(PreferenceKeys.FOLD_USER_DEFINED);
	}
	
	
	public boolean isDefaultCollapsed() {
		return FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_USER_DEFINED);
	}
	
	private UserDefinedEntry getEntry(String name) {
		return (UserDefinedEntry)userDefinedEntries.get(name);
	}
	
	public void save() {
		try {
			StringWriter writer = new StringWriter();
			readerWriter.write(writer, userDefinedEntries.values());
			String result = writer.toString();
			preferences.setValue(PreferenceKeys.USER_DEFINED_ENTRIES, result);
			
		}
		catch(IOException e) {
			throw new RuntimeException("Can't save preferences", e);
		}
	}
	
	public void load() {
		
		try {
			userDefinedEntries.clear();
			
			String xml = preferences.getString(PreferenceKeys.USER_DEFINED_ENTRIES);
			if(xml != null && xml.trim().length() != 0) {
				StringReader reader = new StringReader(xml);
				Collection entries = readerWriter.read(reader);
				for (Iterator iter = entries.iterator(); iter.hasNext();) {
					UserDefinedEntry entry = (UserDefinedEntry) iter.next();
					userDefinedEntries.put(entry.getName(), entry);
				}
			}
			 
		}
		catch(IOException e) {
			throw new RuntimeException("Can't load preferences", e);
		}
	
	}
	
	
}
