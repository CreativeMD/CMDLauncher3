package de.creativemd.launcher.module.state;

import de.creativemd.launcher.utils.Logger;

public interface IModuleState {
	
	public boolean isAvailable();
	
	public boolean isComplete();
	
	public String getInfo();
	
	public default void log(Logger log, String id) {
		if (isComplete())
			log.log("Module '" + id + "' was loaded successfully! " + getInfo());
		else if (isAvailable())
			log.log("Module '" + id + "' was loaded partially! " + getInfo());
		else
			log.log("Module '" + id + "' failed to load! " + getInfo());
	}
	
}
