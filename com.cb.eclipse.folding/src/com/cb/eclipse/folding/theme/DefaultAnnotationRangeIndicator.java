/*
 * Created on Aug 20, 2004
 */
package com.cb.eclipse.folding.theme;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

/**
 * A Default implementation of the ProjectionAnnotationRangeIndicator class. This is a reproduction 
 * of the core Eclipse implementation.
 * @author R.J. Lorimer
 */
public class DefaultAnnotationRangeIndicator implements ProjectionAnnotationRangeIndicator {
	
	private static final int COLOR= SWT.COLOR_DARK_GRAY;
	
	
	public void paint(GC gc, Canvas canvas, Rectangle r) {
		final int MARGIN= 3;
		Color fg= gc.getForeground();
		gc.setForeground(canvas.getDisplay().getSystemColor(COLOR));
		
		gc.setLineWidth(1);
		gc.drawLine(r.x + 4, r.y + 12, r.x + 4, r.y + r.height - MARGIN);
		gc.drawLine(r.x + 4, r.y + r.height - MARGIN, r.x + r.width - MARGIN, r.y + r.height - MARGIN);
		gc.setForeground(fg);
	}
	
	/*
	 * Produces a line diagram...
	 * |
	 * |
	 * |
	 * |
	 * |
	 * |__
	 */

}
