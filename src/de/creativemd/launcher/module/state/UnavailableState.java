package de.creativemd.launcher.module.state;

public class UnavailableState implements IModuleState {
	
	public final String info;
	
	public UnavailableState(String info) {
		this.info = info;
	}
	
	@Override
	public boolean isAvailable() {
		return false;
	}
	
	@Override
	public String getInfo() {
		return info;
	}
	
	@Override
	public boolean isComplete() {
		return false;
	}
	
}
