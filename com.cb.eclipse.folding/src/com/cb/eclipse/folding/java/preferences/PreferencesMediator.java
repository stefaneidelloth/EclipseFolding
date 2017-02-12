/*
 * Created on Aug 20, 2004
 */
package com.cb.eclipse.folding.java.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.cb.eclipse.folding.FoldingPlugin;

/**
 * Implementation of mediator pattern that handles intermediate discussion 
 * between various panel creators to manage global preference change side effects.
 * @author R.J. Lorimer
 */
public class PreferencesMediator {

	private static final String USER_DEFINED_KEY = "userDefined"; 
	private IPreferenceStore preferences;
	private Map foldCheckBoxes;
	private Map initialCheckBoxes;
	
	private Text minimumLinesField;
	private Text startUserDefineField;
	private Text endUserDefineField;
	
	private Button methodLastLine;
	private Button controlLastLine;
	private Button typeLastLine;
	private Button commentLastLine;
	
	private Combo imageSelector;
	
	public PreferencesMediator(IPreferenceStore preferences) {
		foldCheckBoxes = new HashMap();
		initialCheckBoxes = new HashMap();
		this.preferences = preferences;
	}
	
	public void addFoldCheckBox(String key, Button checkbox) {
		foldCheckBoxes.put(key, checkbox);
		if(key.equals(USER_DEFINED_KEY)) {
			checkbox.addListener(SWT.Selection, new UserDefinedListener());
		}
	}
	public void addInitialCollapseCheckBox(String key, Button checkbox) {
		initialCheckBoxes.put(key, checkbox);	
	}
	
	public void setMinimumLinesField(Text field) {
		minimumLinesField = field;
	}
	
	public void setStartUserDefineField(Text field) {
		startUserDefineField = field;
	}
	
	public void setEndUserDefineField(Text field) {
		endUserDefineField = field;
	}
	
	
	
	public void initialize() {
		Iterator it = initialCheckBoxes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Button b = (Button) entry.getValue();
			b.setSelection(preferences.getBoolean("collapse." + key));
		}

		it = foldCheckBoxes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Button b = (Button) entry.getValue();
			b.setSelection(preferences.getBoolean("fold." + key));
			Button counterpart = (Button) initialCheckBoxes.get(key);
			counterpart.setEnabled(b.getSelection());

		}
		

		minimumLinesField.setText(String.valueOf(preferences.getInt(PreferenceKeys.MINIMUM_SIZE)));
		startUserDefineField.setText(String.valueOf(preferences.getString(PreferenceKeys.USER_DEFINED_START)));
		endUserDefineField.setText(String.valueOf(preferences.getString(PreferenceKeys.USER_DEFINED_END)));
		
		typeLastLine.setSelection(preferences.getBoolean(PreferenceKeys.LAST_LINE_TYPES));
		methodLastLine.setSelection(preferences.getBoolean(PreferenceKeys.LAST_LINE_METHODS));
		controlLastLine.setSelection(preferences.getBoolean(PreferenceKeys.LAST_LINE_CONTROLS));
		commentLastLine.setSelection(preferences.getBoolean(PreferenceKeys.LAST_LINE_COMMENTS));
		
		imageSelector.select(imageSelector.indexOf(preferences.getString(PreferenceKeys.FOLDING_ICONS)));
		
		
		updateUserDefinedTable();
		
	}
	
	public void save() {
		Iterator it = initialCheckBoxes.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Button b = (Button) initialCheckBoxes.get(key);
			preferences.setValue("collapse." + key, b.getSelection());
		}

		it = foldCheckBoxes.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Button b = (Button) foldCheckBoxes.get(key);
			preferences.setValue("fold." + key, b.getSelection());
		}

		int minLines = Integer.parseInt(minimumLinesField.getText());
		preferences.setValue(PreferenceKeys.MINIMUM_SIZE, minLines);
		preferences.setValue(PreferenceKeys.USER_DEFINED_START, startUserDefineField.getText());
		preferences.setValue(PreferenceKeys.USER_DEFINED_END, endUserDefineField.getText());
		preferences.setValue(PreferenceKeys.LAST_LINE_COMMENTS, commentLastLine.getSelection());
		preferences.setValue(PreferenceKeys.LAST_LINE_TYPES, typeLastLine.getSelection());
		preferences.setValue(PreferenceKeys.LAST_LINE_CONTROLS, controlLastLine.getSelection());
		preferences.setValue(PreferenceKeys.LAST_LINE_METHODS, methodLastLine.getSelection());
		preferences.setValue(PreferenceKeys.FOLDING_ICONS, imageSelector.getItem(imageSelector.getSelectionIndex()));
	}
	
	public void reset() {
		FoldingPlugin.restoreToDefaults();
	}
	
	private void updateUserDefinedTable() {
		boolean isChecked = ((Button)foldCheckBoxes.get("userDefined")).getSelection();
		startUserDefineField.setEnabled(isChecked);
		endUserDefineField.setEnabled(isChecked);
	}
	
	
	private class UserDefinedListener implements Listener {
		public void handleEvent(Event event) {
			
			updateUserDefinedTable();
		}			
	}
	/**
	 * @param commentLastLine The commentLastLine to set.
	 */
	public void setCommentLastLine(Button commentLastLine) {
		this.commentLastLine = commentLastLine;
	}
	/**
	 * @param controlLastLine The controlLastLine to set.
	 */
	public void setControlLastLine(Button controlLastLine) {
		this.controlLastLine = controlLastLine;
	}
	/**
	 * @param initialCheckBoxes The initialCheckBoxes to set.
	 */
	public void setInitialCheckBoxes(Map initialCheckBoxes) {
		this.initialCheckBoxes = initialCheckBoxes;
	}
	/**
	 * @param typeLastLine The typeLastLine to set.
	 */
	public void setTypeLastLine(Button typeLastLine) {
		this.typeLastLine = typeLastLine;
	}
	/**
	 * @param methodLastLine The methodLastLine to set.
	 */
	public void setMethodLastLine(Button methodLastLine) {
		this.methodLastLine = methodLastLine;
	}
	/**
	 * @param imageSelector The imageSelector to set.
	 */
	public void setImageSelector(Combo imageSelector) {
		this.imageSelector = imageSelector;
	}
}
