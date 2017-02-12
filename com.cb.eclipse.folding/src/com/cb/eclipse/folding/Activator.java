package com.cb.eclipse.folding;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public  class Activator extends AbstractUIPlugin {
	
	private static Activator instance;


	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setInstance(this);		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		setInstance(null);
		super.stop(context);
	}
	
	/**
	 * @return  
	 */
	public static Activator getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					"Activator has not yet been created. Call constructor first.");
		}
		return instance;
	}


	protected void setInstance(Activator abstractActivator) {
		instance = abstractActivator;

	}

	//#end region

	
}
