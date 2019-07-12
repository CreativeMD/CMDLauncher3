package de.creativemd.launcher.task;

import java.util.List;

import de.creativemd.launcher.core.Core;
import de.creativemd.launcher.task.exception.TaskException;
import de.creativemd.launcher.utils.Logger;
import de.creativemd.launcher.utils.StringUtils;

public class TaskManager extends Thread {
	
	public boolean active = true;
	public Logger log;
	public final boolean continuous;
	public boolean idle = false;
	
	public TaskManager(List<Task> tasks, boolean continuous, Logger log, IProgressBar bar) {
		this.tasks = tasks;
		this.continuous = continuous;
		this.log = log;
		this.bar = bar;
	}
	
	public List<Task> tasks;
	
	public Task currentTask;
	public IProgressBar bar;
	
	public IEvent startEvent;
	public ITaskEvent startTaskEvent;
	public ITaskEvent finishTaskEvent;
	public IEvent finishEvent;
	public IEvent noTaskFoundEvent;
	
	public void addTask(Task task) {
		if (continuous)
			this.tasks.add(task);
	}
	
	protected void onStart() {
		if (startEvent != null)
			startEvent.event(this);
	}
	
	protected void onStartTask(Task task) {
		if (startTaskEvent != null)
			startTaskEvent.event(this, task);
	}
	
	protected void onFinishTask(Task task) {
		if (finishTaskEvent != null)
			finishTaskEvent.event(this, task);
	}
	
	protected void onFinish() {
		if (finishEvent != null)
			finishEvent.event(this);
	}
	
	protected void onNoTaskFound() {
		if (noTaskFoundEvent != null)
			noTaskFoundEvent.event(this);
	}
	
	@Override
	public void run() {
		super.run();
		
		onStart();
		
		bar.startProcess(tasks.size());
		
		while (!Core.terminated && (continuous || tasks.size() > 0)) {
			if (Core.closeLauncher) {
				active = false;
				return;
			}
			
			if (tasks.size() > 0) {
				currentTask = tasks.get(0);
				
				long startTime = System.nanoTime();
				onStartTask(currentTask);
				try {
					
					/* if (CurrentTask.CanBeThreaded()) Not supported yet!!!
					 * {
					 * 
					 * }
					 * else
					 * {
					 * 
					 * } */
					
					processTask(currentTask);
				} catch (Exception e) {
					log.log("An error occurred! Please report this on github! {0}", e.getLocalizedMessage());
				}
				
				log.log("Finished task '" + currentTask.id + "' in " + StringUtils.toString(System.nanoTime() - startTime));
				
				onFinishTask(currentTask);
				bar.finishStep();
				
				tasks.remove(0);
			} else {
				if (currentTask != null)
					onNoTaskFound();
				currentTask = null;
				idle = true;
			}
		}
		onFinish();
	}
	
	protected void processTask(Task task) throws TaskException {
		task.run(this, log, bar);
	}
	
	public static interface ITaskEvent {
		
		void event(TaskManager manager, Task task);
		
	}
	
	public static interface IEvent {
		
		void event(TaskManager manager);
		
	}
}
