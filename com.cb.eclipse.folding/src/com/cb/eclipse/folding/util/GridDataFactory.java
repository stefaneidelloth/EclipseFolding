/*
 * Created on Aug 24, 2004
 */
package com.cb.eclipse.folding.util;

import org.eclipse.swt.layout.GridData;

/**
 * Creates grid data objects that can be reused -
 * this class assumes that users will not fidget with the 
 * non-immutable implementations...
 * @author R.J. Lorimer
 */
public class GridDataFactory {
	
	private static GridData excessWidthData;
	static {
		GridData excessWidthData = new GridData();
		excessWidthData.grabExcessHorizontalSpace=true;
		excessWidthData.horizontalAlignment=GridData.HORIZONTAL_ALIGN_END;

			
	}
	

	/**
	 * @return
	 */
	public static GridData getExcessWidthData() {
		return excessWidthData;
	}
	public static GridData getTwoColumnData(int indent) {
		GridData multiColumn = new GridData();
		multiColumn.horizontalSpan = 2;
		multiColumn.horizontalIndent=indent;
		multiColumn.grabExcessVerticalSpace=false;
		return multiColumn;
	}
	
	public static GridData getMinimumWidthData() {
		GridData textWidth = new GridData();
		textWidth.widthHint=50;
		return textWidth;
	}
	
}
