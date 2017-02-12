/*
 * Created on Aug 17, 2004
 */
package com.cb.eclipse.folding.java.calculation;

import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public abstract class CompositeCalculationStrategy extends AbstractCalculationStrategy {

	private RegionCalculationStrategy composite;
	
	public CompositeCalculationStrategy(RegionCalculationStrategy wrapped) {
		composite = wrapped;
	}
	
	public void handle(int nextToken, int start, int end, IJavaElement owner) throws JavaModelException {
		composite.handle(nextToken, start, end, owner);
	}
	
	public boolean keepProcessing(int nextToken) {
		return composite.keepProcessing(nextToken);
	}
	
	public void postScan(int position, IJavaElement elem) throws JavaModelException {
		composite.postScan(position, elem);
	}
	
	public boolean shouldScan(IJavaElement elem) throws JavaModelException {
		return composite.shouldScan(elem);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#dispose()
	 */
	public void dispose() {
		super.dispose();
		composite.dispose();
	}
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#initialize()
	 */
	public void initialize() {
		super.initialize();
		composite.initialize();
	}
	/* (non-Javadoc)
	 * @see com.cb.eclipse.folding.calculation.RegionCalculationStrategy#result()
	 */
	public Set result() throws JavaModelException {
		Set compositeResult = composite.result();
		Set thisResult = super.result();
		thisResult.addAll(compositeResult);
		return thisResult;
	}
}
