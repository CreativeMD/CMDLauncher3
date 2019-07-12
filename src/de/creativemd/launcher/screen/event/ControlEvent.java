package de.creativemd.launcher.screen.event;

import org.cef.callback.CefQueryCallback;

public class ControlEvent {
	
	private Boolean result = null;
	private CefQueryCallback callback;
	public final String data;
	
	public ControlEvent(CefQueryCallback callback, String data) {
		this.callback = callback;
		this.data = data;
	}
	
	public void success(String text) {
		result = true;
		callback.success(text);
	}
	
	public void failure(int code, String text) {
		result = false;
		callback.failure(code, text);
	}
	
	public Boolean result() {
		return result;
	}
	
}
