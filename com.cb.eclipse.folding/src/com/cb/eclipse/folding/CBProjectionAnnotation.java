/*
 * Created on Aug 20, 2004
 */
package com.cb.eclipse.folding;

import org.eclipse.jface.text.source.ImageUtilities;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import com.cb.eclipse.folding.theme.AnnotationThemeFactory;
import com.cb.eclipse.folding.theme.ProjectionAnnotationRangeIndicator;

/**
 * A common super class for all types of projection annotation. This implementation 
 * wires in all of the theme-ing and painting support that resides in the left 
 * gutter.
 * 
 * @author R.J. Lorimer
 */
public class CBProjectionAnnotation extends ProjectionAnnotation {

	public CBProjectionAnnotation(boolean isCollapsed) {
		super(isCollapsed);
	}

	private boolean rangeIndication;
	protected Image collapsed;
	protected Image expanded;

	/**
	 * Enables and disables the range indication for this annotation.
	 * @param rangeIndication  the enable state for the range indication
	 */
	public void setRangeIndication(boolean rangeIndication) {
		this.rangeIndication= rangeIndication;
	}

	private void drawRangeIndication(GC gc, Canvas canvas, Rectangle r) {
		AnnotationThemeFactory themeFactory = AnnotationThemeFactory.getFactory(this);
		ProjectionAnnotationRangeIndicator indicator = themeFactory.getRangeIndicator(this);
		indicator.paint(gc, canvas, r);
	}

	/**
	 * Paints the annotation based on the settings supplied by the model.
	 */
	public void paint(GC gc, Canvas canvas, Rectangle rectangle) {
		Image image= getImage(canvas.getDisplay());
		if (image != null) {
			ImageUtilities.drawImage(image, gc, canvas, rectangle, SWT.CENTER, SWT.TOP);
			if (rangeIndication)
				drawRangeIndication(gc, canvas, rectangle);
		}
	}

	/**
	 * Gets the correct image to render based on the state of the annotation.
	 * 
	 * It is recommended (and is structured in this class) that initializeImages 
	 * is used in implementations of this method to populate any image fields.
	 * @param display
	 * @return
	 */
	protected Image getImage(Display display) {
		initializeImages(display);
		return isCollapsed() ? collapsed : expanded;
	}
	
	/**
	 * Initializes the images on this class from the theme factory. 
	 * @param display
	 */
	private void initializeImages(Display display) {
		if (collapsed == null) {
			AnnotationThemeFactory images = AnnotationThemeFactory.getFactory(this);
			collapsed = images.getCollapsedImage(display, this);
			expanded = images.getExpandedImage(display, this);			
		}		
	}
}
