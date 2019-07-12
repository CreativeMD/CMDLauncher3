package de.creativemd.launcher.core;

import java.util.ArrayList;
import java.util.List;

import de.creativemd.launcher.screen.LoadingScreen;
import de.creativemd.launcher.task.LoadModuleTask;
import de.creativemd.launcher.task.Task;
import de.creativemd.launcher.task.TaskManager;

public class Startup {
	
	public static TaskManager createStartupManager() {
		List<Task> tasks = new ArrayList<>();
		tasks.add(new LoadModuleTask("internet", Core.INTERNET));
		tasks.add(new LoadModuleTask("service", Core.SERVICE));
		return new TaskManager(tasks, false, Core.MAIN_LOG, null);
	}
	
	public static LoadingScreen getStartupScreen() {
		return new LoadingScreen("startup", createStartupManager());
	}
	
}
