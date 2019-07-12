package de.creativemd.launcher.screen.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import de.creativemd.launcher.core.Core;
import de.creativemd.launcher.screen.Screen;
import de.creativemd.launcher.screen.event.ControlEvent;
import de.creativemd.launcher.utils.FileUtils;

public abstract class ScreenControl {
	
	private static HashMap<String, List<String>> controlDraws = new HashMap<>();
	
	public Screen parent;
	public final String id;
	protected final LinkedHashMap<String, String> variables = new LinkedHashMap<>();
	
	public ScreenControl(String id) {
		this.id = id;
	}
	
	public void init(Screen surface) {
		this.parent = surface;
		initVariables();
	}
	
	public void initVariables() {
		variables.put("id", id);
	}
	
	public abstract void onEvent(ControlEvent event);
	
	public List<String> draw() {
		List<String> draw = controlDraws.get(id);
		if (draw == null) {
			draw = FileUtils.parseFile(Core.STYLE.path.resolve("control").resolve(this.getClass().getSimpleName().toLowerCase() + ".html"));
			controlDraws.put(id, draw);
		}
		
		for (Iterator<Entry<String, String>> iterator = variables.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			for (int i = 0; i < draw.size(); i++) {
				String line = draw.get(i);
				if (line.contains(entry.getKey())) {
					draw.set(i, line.replaceAll("§" + entry.getKey() + "§", entry.getValue()));
				}
			}
		}
		
		return draw;
	}
	
	public void runCode(String code) {
		parent.executeJavascript(code);
	}
	
}
