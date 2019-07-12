package de.creativemd.launcher.core;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import de.creativemd.launcher.frame.BrowserFrame;
import de.creativemd.launcher.screen.control.ScreenControl;
import de.creativemd.launcher.screen.event.ControlEvent;
import de.creativemd.launcher.screen.event.ControlEventParser;

public class ScreenMessageHandler extends CefMessageRouterHandlerAdapter {
	
	@Override
	public boolean onQuery(CefBrowser browser, CefFrame cefFrame, long query_id, String request, boolean persistent, CefQueryCallback callback) {
		String[] data = request.split(":");
		if (data.length == 0) {
			callback.failure(100, "No control given");
			return false;
		}
		
		BrowserFrame frame = Core.getFrame(browser);
		if (frame == null) {
			callback.failure(101, "Frame could not be found");
			return false;
		}
		
		String controlName = data[0];
		ScreenControl control = frame.surface.getControl(controlName);
		if (control == null) {
			callback.failure(102, "Control coult not be found");
			return false;
		}
		
		ControlEvent event = ControlEventParser.parseEvent(data.length > 1 ? data[1] : "", data.length > 2 ? data[2] : "", callback);
		if (event == null) {
			callback.failure(103, "Event could not be created");
			return false;
		}
		
		control.onEvent(event);
		if (event.result() == null) {
			callback.failure(104, "Event not properly executed");
			return false;
		}
		
		return event.result();
	}
}