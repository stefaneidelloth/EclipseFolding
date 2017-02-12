/*
 * Created on Aug 24, 2004
 */
package com.cb.eclipse.folding.theme;


/**
 * A convenience key for mapping images to their lookup information.
 * @author R.J. Lorimer
 */
public class ImageLookupKey {
	
	private Class resourceClass;
	private String resourcePath;
	
	ImageLookupKey(Class resourceClass, String resourcePath) {
		this.resourceClass = resourceClass;
		this.resourcePath = resourcePath;
	}
	
	public Class getResourceClass() {
		return resourceClass;
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public boolean equals(Object o) {
		boolean result;
		result = (o.getClass().equals(getClass()));
						
		if(result) {
			ImageLookupKey other = (ImageLookupKey)o;
			result = this.resourceClass.equals(other.resourceClass) && this.resourcePath.equals(other.resourcePath); 
		}
		return result;
	}
	
	public int hashCode() {
		return 11 + resourcePath.hashCode() + resourceClass.hashCode();
	}
	
}