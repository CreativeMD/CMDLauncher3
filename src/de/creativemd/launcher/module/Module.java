package de.creativemd.launcher.module;

import java.util.HashMap;

import de.creativemd.launcher.core.Core;
import de.creativemd.launcher.module.state.IModuleState;
import de.creativemd.launcher.module.state.UnavailableState;

public abstract class Module {
	
	private static HashMap<String, Module> runningModules = new HashMap<>();
	private static HashMap<String, IModuleState> modulesState = new HashMap<>();
	
	public static IModuleState checkForModule(String id) {
		IModuleState state = modulesState.get(id);
		if (state != null)
			return state;
		return new UnavailableState("Module does not exist!");
	}
	
	public final String id;
	public final String[] hardDependencies;
	
	public Module(String id, String... hardDependencies) {
		this.id = "module." + id;
		this.hardDependencies = hardDependencies;
	}
	
	protected abstract IModuleState load();
	
	public IModuleState loadModule() {
		state = null;
		for (String string : hardDependencies) {
			if (!checkForModule("module." + string).isComplete()) {
				state = new UnavailableState("Missing dependency 'module." + string + "'");
				break;
			}
		}
		
		if (state == null)
			state = load();
		
		state.log(Core.MAIN_LOG, id);
		
		runningModules.put(id, this);
		modulesState.put(id, state);
		
		return state;
	}
	
	private IModuleState state;
	
	public IModuleState getState() {
		return state;
	}
	
	public boolean isAvailable() {
		return state.isAvailable();
	}
	
}
