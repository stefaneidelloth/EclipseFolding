/*
 * Created on Sep 7, 2004
 */
package com.cb.eclipse.folding.theme;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.graphics.Image;

import com.cb.eclipse.folding.FoldingPlugin;

/**
 * A centralized class for managing images in the FoldingPlugin.
 * @author R.J. Lorimer
 */
public class Images {
	
	private ImageRegistry registry;
	
	public Images() {
		registry = new ImageRegistry();

		// pre-load.
		registry.put(FoldingPlugin.getMessage("default.icons.title")+".collapsed", ImageDescriptor.createFromFile(ProjectionAnnotation.class, "images/collapsed.gif"));
		registry.put(FoldingPlugin.getMessage("default.icons.title")+".expanded", ImageDescriptor.createFromFile(ProjectionAnnotation.class, "images/expanded.gif"));
		registry.put(FoldingPlugin.getMessage("modern.icons.title")+".collapsed", ImageDescriptor.createFromFile(Images.class, "image/collapsed.gif"));
		registry.put(FoldingPlugin.getMessage("modern.icons.title")+".expanded", ImageDescriptor.createFromFile(Images.class, "image/expanded.gif"));
		
		// TODO - constantify these
		registry.put("CHECKED", ImageDescriptor.createFromFile(
				FoldingPlugin.class, 
				"images/common/checked.gif"
				)
			);
		registry.put("UNCHECKED", ImageDescriptor.createFromFile(
				FoldingPlugin.class, 
				"images/common/unchecked.gif"
				)
			);	
	}
	
	public Image getImage(String key) {
		return registry.get(key);
	}
	
	public Image getCheckBoxImage(boolean checked) {
		return ((checked) ? registry.get("CHECKED") : registry.get("UNCHECKED"));
	}
	public void registerImage(String key, ImageLookupKey globalKey) {
		registerImage(key, globalKey.getResourceClass(), globalKey.getResourcePath());
	}
	
	public void registerImage(String key, Class resourceClass, String resourcePath) {
		if(registry.get(key) != null) {
			throw new IllegalArgumentException("Image with key: " + key + " already exists");
		}
		registry.put(key, ImageDescriptor.createFromFile(resourceClass, resourcePath));
	}
	
	
	public String[] getAvailableFoldingKeys() {
		return new String[] { FoldingPlugin.getMessage("default.icons.title"), FoldingPlugin.getMessage("modern.icons.title") };
	}

}
