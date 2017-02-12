/*
 * Created on Aug 19, 2004
 */
package com.cb.eclipse.folding.theme;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.cb.eclipse.folding.FoldingPlugin;


/**
 * Selects theme controls based on the format of the annotation provided.
 * @author R.J. Lorimer
 */
public abstract class AnnotationThemeFactory {

	private static final String COLLAPSED_PATH = "images/collapsed.gif";
	private static final String EXPANDED_PATH = "images/expanded.gif";
	private static final DefaultAnnotationRangeIndicator defaultRangeIndicator = new DefaultAnnotationRangeIndicator();
	
	private static Map images = new HashMap();
	
	private static DefaultAnnotationThemeFactory defaultAnnotations = new DefaultAnnotationThemeFactory();
	
	private static boolean disposerRegistered;
	
	
	/**
	 * Factory method for selecting a factory based on the annotation object.
	 * @param annotation
	 * @return An annotation image factory for selecting annotation images
	 */
	public static AnnotationThemeFactory getFactory(ProjectionAnnotation annotation) {
		return getFactory(annotation.getClass());
	}
	
	public static AnnotationThemeFactory getFactory(Class annotationType) {
		return defaultAnnotations;
	}
	
	
	protected Image getImage(String key, boolean isFolded) {
		
		String trueKey = key + ((isFolded) ? ".collapsed" : ".expanded");
		return FoldingPlugin.getImages().getImage(trueKey);
	}
	
	
	public abstract Image getCollapsedImage(Display disp, ProjectionAnnotation annotation);
	public abstract Image getExpandedImage(Display disp, ProjectionAnnotation annotation);
	public abstract ProjectionAnnotationRangeIndicator getRangeIndicator(ProjectionAnnotation annotation);
	
	

	
	protected ProjectionAnnotationRangeIndicator getDefaultRangeIndicator() {
		return defaultRangeIndicator;
	}
	
	
	
	
	
	
}
