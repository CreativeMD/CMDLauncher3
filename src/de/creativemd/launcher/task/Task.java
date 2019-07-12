package de.creativemd.launcher.task;

import de.creativemd.launcher.module.Module;
import de.creativemd.launcher.task.exception.TaskException;
import de.creativemd.launcher.utils.Logger;

public abstract class Task {
	
	public final String id;
	public final String[] requiredModules;
	
	public Task(String id, String... requiredModules) {
		this.id = "task." + id;
		this.requiredModules = requiredModules;
	}
	
	public boolean run(TaskManager manager, Logger log, IProgressBar bar) throws TaskException {
		for (String string : requiredModules) {
			if (!Module.checkForModule("task." + string).isComplete())
				throw new TaskException("Missing dependency 'task." + string + "'!");
		}
		
		return runTask(manager, log, bar);
	}
	
	protected abstract boolean runTask(TaskManager manager, Logger log, IProgressBar bar) throws TaskException;
	
}
