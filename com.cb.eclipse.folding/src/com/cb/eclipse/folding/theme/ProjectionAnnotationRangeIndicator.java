/*
 * Created on Aug 20, 2004
 */
package com.cb.eclipse.folding.theme;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

/**
 * The indication painter for a range of the projection that is 
 * expanded.
 * @author R.J. Lorimer
 */
public interface ProjectionAnnotationRangeIndicator {
	
	public void paint(GC gc, Canvas canvas, Rectangle r);

}
