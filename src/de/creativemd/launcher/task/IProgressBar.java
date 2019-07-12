package de.creativemd.launcher.task;

public interface IProgressBar {
	
	void startProcess(int steps);
	
	void finishStep();
	
	void startStep(int max);
	
	void setProgress(int progress);
	
}
