package de.creativemd.launcher.screen.control;

import de.creativemd.launcher.screen.event.ControlEvent;
import de.creativemd.launcher.task.IProgressBar;

public class ProgressBar extends ScreenControl implements IProgressBar {
	
	public ProgressBar(String id) {
		super(id);
	}
	
	public int processPos;
	public int processMax = 0;
	public int stepPos;
	public int stepMax;
	
	@Override
	public void finishStep() {
		if (stepMax > 0)
			stepPos = stepMax;
		else
			stepPos = stepMax = 1;
		updateProgress();
	}
	
	@Override
	public void setProgress(int progress) {
		if (progress < processMax)
			processPos = progress;
		else
			processPos = processMax;
		updateProgress();
	}
	
	@Override
	public void startProcess(int steps) {
		processMax = steps;
		processPos = -1;
		stepMax = 0;
		stepPos = 0;
		updateProgress();
	}
	
	@Override
	public void startStep(int max) {
		if (processPos < processMax)
			processPos++;
		stepMax = max;
		stepPos = 0;
		updateProgress();
	}
	
	protected double lastProgress = 0;
	
	public void updateProgress() {
		double progress;
		if (processMax > 0) {
			progress = Math.max(processPos, 0) / (double) processMax;
			if (stepMax > 0)
				progress += (stepPos / (double) stepMax) / processMax;
			progress *= 100;
		} else
			progress = 0;
		
		if (lastProgress != progress) {
			runCode("$('#" + id + "').css('width', '" + progress + "%').attr('data-percent', " + progress + ");$('#" + id + "_label').text('" + ((int) progress) + "%');");
			lastProgress = progress;
		}
	}
	
	@Override
	public void onEvent(ControlEvent event) {
		
	}
	
}
