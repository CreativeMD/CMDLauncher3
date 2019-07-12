package de.creativemd.launcher.module.state;

public class ActiveState implements IModuleState {
	
	public final String info;
	
	public ActiveState(String info) {
		this.info = info;
	}
	
	@Override
	public boolean isAvailable() {
		return true;
	}
	
	@Override
	public boolean isComplete() {
		return true;
	}
	
	@Override
	public String getInfo() {
		return info;
	}
	
}
