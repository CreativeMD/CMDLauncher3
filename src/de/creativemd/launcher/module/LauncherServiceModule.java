package de.creativemd.launcher.module;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import de.creativemd.launcher.module.state.ActiveState;
import de.creativemd.launcher.module.state.IModuleState;
import de.creativemd.launcher.module.state.UnavailableState;

public class LauncherServiceModule extends Module {
	
	public LauncherServiceModule() {
		super("launcher_service", "internet");
	}
	
	@Override
	protected IModuleState load() {
		try {
			if ("true".equals(readStringFromURL("https://launcher.creativemd.de/service/connectLauncher.php")))
				return new ActiveState("Connected to launcher database!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new UnavailableState("Could not connect to launcher database!");
	}
	
	public static String readStringFromURL(String requestURL) throws IOException {
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
}
