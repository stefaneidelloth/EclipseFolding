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

package com.cb.eclipse.folding.java.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import com.cb.eclipse.folding.FoldingPlugin;

/**
 * This class manages the default property values for the plug-in.
 * 
 * @author R.J. Lorimer
 */
public final class Defaults {

	public static final int MINIMUM_SIZE = 3;
	public static final Boolean COLLAPSE_INNER_TYPES = Boolean.valueOf(true);
	public static final Boolean FOLD_INNER_TYPES = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_TOP_TYPES = Boolean.valueOf(false);
	public static final Boolean FOLD_TOP_TYPES = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_COMMENT_BLOCKS = Boolean.valueOf(true);
	public static final Boolean FOLD_COMMENT_BLOCKS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_JAVADOCS = Boolean.valueOf(true);
	public static final Boolean FOLD_JAVADOCS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_LINE_COMMENTS = Boolean.valueOf(false);
	public static final Boolean FOLD_LINE_COMMENTS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_HEADER = Boolean.valueOf(true);
	public static final Boolean FOLD_HEADER = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_IMPORTS = Boolean.valueOf(true);
	public static final Boolean FOLD_IMPORTS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_STATICS = Boolean.valueOf(true);
	public static final Boolean FOLD_STATICS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_METHODS = Boolean.valueOf(false);
	public static final Boolean FOLD_METHODS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_CONSTRUCTORS = Boolean.valueOf(false);
	public static final Boolean FOLD_CONSTRUCTORS = Boolean.valueOf(true);
	public static final Boolean FOLD_MAIN_METHODS = Boolean.valueOf(true);
	public static final Boolean COLLAPSE_MAIN_METHODS = Boolean.valueOf(false);
	public static final Boolean COLLAPSE_GETTER_SETTER = Boolean.valueOf(true);
	public static final Boolean FOLD_GETTER_SETTER = Boolean.valueOf(true);
	public static final Boolean FOLD_USER_DEFINED = Boolean.valueOf(false);
	public static final Boolean COLLAPSE_USER_DEFINED = Boolean.valueOf(true);

	public static final Boolean LAST_LINE_METHOD = Boolean.valueOf(false);
	public static final Boolean LAST_LINE_TYPE = Boolean.valueOf(false);
	public static final Boolean LAST_LINE_CONTROL = Boolean.valueOf(false);
	public static final Boolean LAST_LINE_COMMENT = Boolean.valueOf(false);
	
	public static final String USER_DEFINED_START = "[start]";
	public static final String USER_DEFINED_END = "[end]";
	
	
	
	private static int APPLY = 0;
	private static int RESTORE = 99;

	private Defaults() {

	}

	public static void applyDefaults(IPreferenceStore store) {
		doLoop(store, APPLY);
	}

	public static void restoreDefaults(IPreferenceStore store) {
		doLoop(store, RESTORE);
	}

	private static void doLoop(IPreferenceStore store, int mode) {

		
		Map values = new HashMap();
		values.put(PreferenceKeys.COLLAPSE_COMMENT_BLOCKS, COLLAPSE_COMMENT_BLOCKS);
		values.put(PreferenceKeys.FOLD_COMMENT_BLOCKS, FOLD_COMMENT_BLOCKS);
		values.put(PreferenceKeys.COLLAPSE_LINE_COMMENTS, COLLAPSE_LINE_COMMENTS);
		values.put(PreferenceKeys.FOLD_LINE_COMMENTS, FOLD_LINE_COMMENTS);
		values.put(PreferenceKeys.COLLAPSE_JAVADOCS, COLLAPSE_JAVADOCS);
		values.put(PreferenceKeys.FOLD_JAVADOCS, FOLD_JAVADOCS);
		values.put(PreferenceKeys.COLLAPSE_HEADER, COLLAPSE_HEADER);
		values.put(PreferenceKeys.FOLD_HEADER, FOLD_HEADER);
		values.put(PreferenceKeys.COLLAPSE_IMPORTS, COLLAPSE_IMPORTS);
		values.put(PreferenceKeys.FOLD_IMPORTS, FOLD_IMPORTS);
		values.put(PreferenceKeys.COLLAPSE_INNER_TYPES, COLLAPSE_INNER_TYPES);
		values.put(PreferenceKeys.FOLD_INNER_TYPES, FOLD_INNER_TYPES);
		values.put(PreferenceKeys.COLLAPSE_STATICS, COLLAPSE_STATICS);
		values.put(PreferenceKeys.FOLD_STATICS, FOLD_STATICS);
		values.put(PreferenceKeys.COLLAPSE_METHODS, COLLAPSE_METHODS);
		values.put(PreferenceKeys.FOLD_METHODS, FOLD_METHODS);
		values.put(PreferenceKeys.COLLAPSE_TOP_TYPES, COLLAPSE_TOP_TYPES);
		values.put(PreferenceKeys.FOLD_TOP_TYPES, FOLD_TOP_TYPES);

		values.put(PreferenceKeys.FOLD_MAIN_METHODS, FOLD_MAIN_METHODS);
		values.put(PreferenceKeys.COLLAPSE_MAIN_METHODS, COLLAPSE_MAIN_METHODS);
		values.put(PreferenceKeys.COLLAPSE_CONSTRUCTORS, COLLAPSE_CONSTRUCTORS);
		values.put(PreferenceKeys.FOLD_CONSTRUCTORS, FOLD_CONSTRUCTORS);
		values.put(PreferenceKeys.FOLD_USER_DEFINED, FOLD_USER_DEFINED);
		values.put(PreferenceKeys.COLLAPSE_USER_DEFINED, COLLAPSE_USER_DEFINED);
		
		
		

		Iterator entries = values.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			Boolean val = (Boolean) entry.getValue();

			
			
			if (mode == RESTORE) {
				store.setToDefault(key);
			}
			
			
			else {
				store.setDefault(key, val.booleanValue());
			}
		}

		if (mode == RESTORE) {
			store.setToDefault(PreferenceKeys.MINIMUM_SIZE);
			store.setToDefault(PreferenceKeys.USER_DEFINED_START);
			store.setToDefault(PreferenceKeys.USER_DEFINED_END);
			store.setToDefault(PreferenceKeys.LAST_LINE_COMMENTS);
			store.setToDefault(PreferenceKeys.LAST_LINE_CONTROLS);
			store.setToDefault(PreferenceKeys.LAST_LINE_TYPES);
			store.setToDefault(PreferenceKeys.LAST_LINE_METHODS);
			store.setToDefault(PreferenceKeys.FOLDING_ICONS);
			
		}
		
		
		else {
			store.setDefault(PreferenceKeys.MINIMUM_SIZE, MINIMUM_SIZE);
			store.setDefault(PreferenceKeys.USER_DEFINED_START, USER_DEFINED_START);
			store.setDefault(PreferenceKeys.USER_DEFINED_END, USER_DEFINED_END);
			store.setDefault(PreferenceKeys.LAST_LINE_COMMENTS, LAST_LINE_COMMENT.booleanValue());
			store.setDefault(PreferenceKeys.LAST_LINE_CONTROLS, LAST_LINE_CONTROL.booleanValue());
			store.setDefault(PreferenceKeys.LAST_LINE_TYPES, LAST_LINE_TYPE.booleanValue());
			store.setDefault(PreferenceKeys.LAST_LINE_METHODS, LAST_LINE_METHOD.booleanValue());
			store.setDefault(PreferenceKeys.FOLDING_ICONS, FoldingPlugin.getMessage("default.icons.title"));
		}

	}

}

