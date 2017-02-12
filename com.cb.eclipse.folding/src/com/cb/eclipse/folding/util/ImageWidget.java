/*
 * Created on Aug 26, 2004
 */
package com.cb.eclipse.folding.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class ImageWidget {

	private Image image;
	public ImageWidget(Image img) {
		image = img;
	}
	
	public Control getControl(Composite owner) {
		Canvas canvas = new Canvas(owner, SWT.NONE);
		canvas.addPaintListener (new PaintListener () {
			Rectangle imgBounds = image.getBounds();
			public void paintControl (PaintEvent e) {
				Rectangle imgBounds = image.getBounds();
				
				e.gc.drawImage (image, 0, 0, imgBounds.width, imgBounds.height, imgBounds.x, imgBounds.y, imgBounds.width, imgBounds.height);
			}
		});
		
		canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT);		
		return canvas;
	}
}
