package de.creativemd.launcher.screen.event;

import org.cef.callback.CefQueryCallback;

public class ControlEventParser {
	
	public static ControlEvent parseEvent(String event, String data, CefQueryCallback callback) {
		return new ControlEvent(callback, data);
	}
	
}
