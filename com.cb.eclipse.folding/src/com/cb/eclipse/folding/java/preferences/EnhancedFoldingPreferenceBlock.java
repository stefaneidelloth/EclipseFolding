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

import org.eclipse.jdt.ui.text.folding.IJavaFoldingPreferenceBlock;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.preferences.PreferencesTab;

/**
 * Plug-in Preferences Page.
 * 
 * @author RJ
 */
public class EnhancedFoldingPreferenceBlock implements IJavaFoldingPreferenceBlock {

	
	private IPreferenceStore preferences;
	private PreferencesMediator mediator;
	
	
	private Text minimumLines;
	private Text startUserDefine;
	private Text endUserDefine;

	public EnhancedFoldingPreferenceBlock() {
		preferences = FoldingPlugin.getDefault().getPreferenceStore();
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.folding.IJavaFoldingPreferenceBlock#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {

		mediator = new PreferencesMediator(preferences);
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
				
		TabItem item = new TabItem(tabFolder, SWT.NONE);
		PreferencesTab generalTab = new GeneralTab(mediator);
		item.setText(generalTab.getTabName());
		item.setControl(generalTab.getTabControl(tabFolder));

		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		PreferencesTab advancedTab = new AdvancedTab(mediator);
		item2.setText(advancedTab.getTabName());
		item2.setControl(advancedTab.getTabControl(tabFolder));		
		
		TabItem item3 = new TabItem(tabFolder, SWT.NONE);
		PreferencesTab userDefTab = new UserDefinedTab(mediator);
		item3.setText(userDefTab.getTabName());
		item3.setControl(userDefTab.getTabControl(tabFolder));		
		
		tabFolder.pack();
		
		parent.pack();
		return tabFolder;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.folding.IJavaFoldingPreferenceBlock#initialize()
	 */
	public void initialize() {

		mediator.initialize();
		
	}
	

	public void performOk() {
		FoldingPlugin.getJavaDomain().save();
		mediator.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.folding.IJavaFoldingPreferenceBlock#performDefaults()
	 */
	public void performDefaults() {
		mediator.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.folding.IJavaFoldingPreferenceBlock#dispose()
	 */
	public void dispose() {
		mediator = null;
	}

	
	
	
	

	
	
	

	
}