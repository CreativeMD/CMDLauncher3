package de.creativemd.launcher.screen;

import java.util.concurrent.TimeUnit;

import org.cef.browser.CefBrowser;

import de.creativemd.launcher.core.Core;
import de.creativemd.launcher.screen.control.Label;
import de.creativemd.launcher.screen.control.ProgressBar;
import de.creativemd.launcher.task.Task;
import de.creativemd.launcher.task.TaskManager;
import de.creativemd.launcher.task.TaskManager.IEvent;
import de.creativemd.launcher.task.TaskManager.ITaskEvent;

public class LoadingScreen extends Screen {
	
	public TaskManager manager;
	public ProgressBar bar;
	public Label label;
	
	public LoadingScreen(String id, TaskManager manager) {
		super(id);
		this.bar = new ProgressBar("progress");
		this.addControl(bar);
		this.label = new Label("status", "Loading ...");
		this.addControl(label);
		this.manager = manager;
		
		manager.startTaskEvent = new ITaskEvent() {
			
			@Override
			public void event(TaskManager manager, Task task) {
				label.setText(Core.LANG.translate(task.id) + " ...");
			}
		};
		manager.finishEvent = new IEvent() {
			
			@Override
			public void event(TaskManager manager) {
				label.setText("Finished startup");
				try {
					TimeUnit.SECONDS.sleep(1L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LoadingScreen.this.parent.loadSurface(new Screen("overview"));
			}
		};
		
		manager.bar = bar;
	}
	
	@Override
	public void load(CefBrowser browser) {
		super.load(browser);
		manager.start();
	}
	
}
