/*
 * Created on Sep 7, 2004
 */
package com.cb.eclipse.folding.action;

import java.util.ResourceBundle;

import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class FoldingAction extends TextEditorAction {

	/**
	 * @param bundle
	 * @param prefix
	 * @param editor
	 */
	public FoldingAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
		super(bundle, prefix, editor);		
	}
	/**
	 * @param bundle
	 * @param prefix
	 * @param editor
	 * @param style
	 */
	public FoldingAction(ResourceBundle bundle, String prefix, ITextEditor editor, int style) {
		super(bundle, prefix, editor, style);
	}
	
	
}
