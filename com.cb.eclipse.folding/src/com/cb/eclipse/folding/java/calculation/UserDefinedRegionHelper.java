/*
 * Created on Sep 10, 2004
 */
package com.cb.eclipse.folding.java.calculation;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.JavaModelException;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class UserDefinedRegionHelper {
	
	
	public UserDefinedRegionHelper() {
		
		
	}
	
	
	public boolean isOpeningSentinel(int start, int end, IJavaElement owner) throws JavaModelException {
		String startSentinel = FoldingPlugin.getPrefs().getString(PreferenceKeys.USER_DEFINED_START);
		return isSentinel(startSentinel, start, end, owner);
	}
	
	public boolean isClosingSentinel(int start, int end, IJavaElement owner) throws JavaModelException {
		String endSentinel = FoldingPlugin.getPrefs().getString(PreferenceKeys.USER_DEFINED_END);
		return isSentinel(endSentinel, start, end, owner);
	}
	
	public boolean isSentinel(String sentinel, int start, int end, IJavaElement owner) throws JavaModelException {
		ISourceReference reference = (ISourceReference) owner;
		
		String contents = reference.getSource();
		ISourceRange range = (ISourceRange)reference.getSourceRange();
		
		
		int correctedStart = start - range.getOffset();
		// Math.min should be redundant (no JavaElement should contain half of a comment)
		int correctedEnd = Math.min( (end - range.getOffset()) , range.getLength());
		
		//System.out.println("Checking for sentinel: " + contents.substring(correctedStart, correctedEnd));
		boolean keepScanning = true;
		int shift = correctedStart+2; // +2 refers to the // characters which we know we can remove
		
		while(keepScanning && shift <= correctedEnd) {
			char nextChar = contents.charAt(shift);
			
			if(nextChar != ' ' && nextChar != '\t') {
				keepScanning = false;				
			}
			else {
				shift++;
			}
			
		}
		
		boolean foundSentinel = (contents.startsWith(sentinel, shift));
		//System.out.println("Sentinel was found: " + foundSentinel);
		return foundSentinel;
	}
	
	
	
	
	
}
