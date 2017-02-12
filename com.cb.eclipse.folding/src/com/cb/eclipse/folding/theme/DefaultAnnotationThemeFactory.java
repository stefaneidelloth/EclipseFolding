/*
 * Created on Sep 7, 2004
 */
package com.cb.eclipse.folding.theme;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * Implementation of the AnnotationThemeFactory API that simply uses a user preference 
 * to toggle globally what images will be used.
 * @author R.J. Lorimer
 */
public class DefaultAnnotationThemeFactory extends AnnotationThemeFactory {

	DefaultAnnotationThemeFactory() {
		
	}
	
	public Image getCollapsedImage(Display disp, ProjectionAnnotation annotation) {
		
		return getImage(getCurrentImageKey(), true);
	}
	
	public Image getExpandedImage(Display disp, ProjectionAnnotation annotation) {
		return getImage(getCurrentImageKey(), false);
	}
	
	public ProjectionAnnotationRangeIndicator getRangeIndicator(ProjectionAnnotation annotation) {
		return getDefaultRangeIndicator();
	}
	
	private String getCurrentImageKey() {
		String key = FoldingPlugin.getPrefs().getString(PreferenceKeys.FOLDING_ICONS);
		return key;
	}
	
}
