package de.creativemd.launcher.screen;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.cef.browser.CefBrowser;

import de.creativemd.launcher.core.Core;
import de.creativemd.launcher.frame.BrowserFrame;
import de.creativemd.launcher.screen.control.ScreenControl;
import de.creativemd.launcher.utils.FileUtils;

public class Screen {
	
	public static Path TEMP_FILE = Core.TEMP_PATH.resolve("temp.html");
	
	public final String id;
	public BrowserFrame parent;
	public CefBrowser browser;
	public LinkedHashMap<String, ScreenControl> controls = new LinkedHashMap<>();
	protected LinkedHashMap<String, String> variables = new LinkedHashMap<>();
	
	public Screen(String id, ScreenControl... controls) {
		this.id = id;
		for (ScreenControl control : controls) {
			this.controls.put(control.id, control);
		}
		initVariables();
	}
	
	public void addControl(ScreenControl control) {
		controls.put(control.id, control);
	}
	
	public void initVariables() {
		
	}
	
	public ScreenControl getControl(String id) {
		return controls.get(id);
	}
	
	public String build() {
		try {
			
			List<String> lines = FileUtils.parseFile(Core.STYLE.path.resolve("screen").resolve(id + ".html"));
			
			for (Iterator<Entry<String, String>> iterator = variables.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String> entry = iterator.next();
				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);
					if (line.contains(entry.getKey())) {
						lines.set(i, line.replaceAll("§" + entry.getKey() + "§", entry.getValue()));
					}
				}
			}
			
			LinkedHashMap<String, String> draws = new LinkedHashMap<>();
			for (ScreenControl control : controls.values()) {
				control.init(this);
				draws.put("$" + control.id + "$", String.join(System.lineSeparator(), control.draw()));
			}
			
			Core.STYLE.addSpecialPages(draws);
			
			for (Iterator<Entry<String, String>> iterator = draws.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String> entry = iterator.next();
				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);
					if (line.contains(entry.getKey())) {
						lines.set(i, line.replace(entry.getKey(), entry.getValue()));
					}
				}
			}
			
			FileUtils.saveFile(TEMP_FILE, lines);
			
			return "file:///" + TEMP_FILE.toAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "about:blank";
	}
	
	public List<String> javaQueue = new ArrayList<>();
	
	public void executeJavascript(String code) {
		if (javaQueue != null)
			javaQueue.add(code);
		else
			browser.executeJavaScript(code, browser.getURL(), 0);
	}
	
	public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
		if (!isLoading) {
			List<String> queue = javaQueue;
			javaQueue = null;
			for (String code : queue) {
				browser.executeJavaScript(code, browser.getURL(), 0);
			}
		}
	}
	
	public void init(BrowserFrame frame) {
		this.parent = frame;
	}
	
	public void load(CefBrowser browser) {
		this.browser = browser;
	}
	
}
