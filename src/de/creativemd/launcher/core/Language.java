package de.creativemd.launcher.core;

import java.util.HashMap;
import java.util.List;

import de.creativemd.launcher.utils.FileUtils;

public class Language {
	
	public final String id;
	private HashMap<String, String> dict = new HashMap<>();
	
	public Language(String id) {
		this.id = id;
	}
	
	public void loadLanguage() {
		Core.MAIN_LOG.log("Loading language " + id + " ...");
		List<String> lines = FileUtils.parseFile(Core.ASSETS_PATH.resolve("lang").resolve(id + ".lang"));
		for (String line : lines) {
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			String[] split = line.split("=");
			if (split.length == 2)
				dict.put(split[0], split[1]);
			else
				Core.MAIN_LOG.log("Found invalid line in '" + id + ".lang': " + line);
		}
		
	}
	
	public String translate(String input) {
		return dict.getOrDefault(input, input);
	}
	
}
