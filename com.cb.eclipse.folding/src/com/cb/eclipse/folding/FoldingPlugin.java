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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cb.eclipse.folding.java.JavaSettings;
import com.cb.eclipse.folding.java.preferences.Defaults;
import com.cb.eclipse.folding.theme.Images;

/**
 * The main plugin class for the FoldingPlugin.
 */
public class FoldingPlugin extends AbstractUIPlugin {

	// The shared instance.
	private static FoldingPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	private boolean resourceBundleBuilt;
	
	
	private Images images;
	
	private JavaSettings javaDomain;

	/**
	 * The constructor.
	 */
	public FoldingPlugin(IPluginDescriptor desc) {
		super(desc);
		try {
			
			plugin = this;
			

			
			images = new Images();
			Defaults.applyDefaults(getPrefs());
			javaDomain = new JavaSettings(getPrefs());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();		
		}
		

	}

	public static void restoreToDefaults() {
		Defaults.restoreDefaults(getPrefs());
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static FoldingPlugin getDefault() {
		return plugin;
	}
	
	public static Images getImages() {
		return getDefault().images;
	}
	
	public static JavaSettings getJavaDomain() {
		return getDefault().javaDomain;
	}

	public static IPreferenceStore getPrefs() {
		return getDefault().getPreferenceStore();
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getMessage(String key) {
		ResourceBundle bundle = FoldingPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		}
		catch (MissingResourceException e) {
			return key;
		}
	}

	public static boolean getBoolean(String key) {
		boolean result = FoldingPlugin.getPrefs().getBoolean(key);
		return result;
	}
	
	
	

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		if(!resourceBundleBuilt) {
			try {
				resourceBundle = ResourceBundle.getBundle("com.cb.eclipse.folding.FoldingResources");
			}
			catch (MissingResourceException x) {
				resourceBundle = null;
			}
			
			resourceBundleBuilt=true;
		}
		
		return resourceBundle;
	}
}