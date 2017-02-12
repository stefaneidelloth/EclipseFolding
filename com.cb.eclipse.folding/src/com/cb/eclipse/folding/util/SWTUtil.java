/*
 * Created on Sep 23, 2004
 */
package com.cb.eclipse.folding.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class SWTUtil {

	
	public static void center(Shell shell, Rectangle parent) {
		Point shellDim = shell.getSize();
		int heightDiff = parent.height - shellDim.y;
		int widthDiff = parent.width - shellDim.x;
		
		int top = parent.y + (heightDiff / 2);
		int left = parent.x + (widthDiff / 2);
		
		top = Math.max(0, top);
		left = Math.max(0, left);
		
		shell.setLocation(left, top);
	}
}
