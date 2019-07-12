package de.creativemd.launcher.module;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import de.creativemd.launcher.module.state.ActiveState;
import de.creativemd.launcher.module.state.IModuleState;
import de.creativemd.launcher.module.state.UnavailableState;

public class InternetModule extends Module {
	
	public InternetModule() {
		super("internet");
	}
	
	@Override
	protected IModuleState load() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			conn.getInputStream().close();
			return new ActiveState("Established connection to 'google.com!'");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return new UnavailableState("Failed to access 'google.com'!");
		}
	}
	
}
