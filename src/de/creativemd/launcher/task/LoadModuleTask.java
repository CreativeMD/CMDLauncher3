package de.creativemd.launcher.task;

import de.creativemd.launcher.module.Module;
import de.creativemd.launcher.task.exception.TaskException;
import de.creativemd.launcher.utils.Logger;

public class LoadModuleTask extends Task {
	
	public Module module;
	
	public LoadModuleTask(String id, Module module) {
		super(id);
		this.module = module;
	}
	
	@Override
	protected boolean runTask(TaskManager manager, Logger log, IProgressBar bar) throws TaskException {
		bar.startStep(1);
		return module.loadModule().isAvailable();
	}
	
}
