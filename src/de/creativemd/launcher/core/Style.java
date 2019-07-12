package de.creativemd.launcher.core;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.creativemd.launcher.utils.FileUtils;

public class Style {
	
	public final Path path;
	public final String name;
	
	protected HashMap<String, String> specialPages;
	
	public Style(String name, Path path) {
		this.name = name;
		this.path = path;
		initSpecialPages();
	}
	
	public void addSpecialPages(Map<String, String> map) {
		map.putAll(specialPages);
	}
	
	public void initSpecialPages() {
		specialPages = new HashMap<>();
		
		LinkedHashMap<String, String> variables = new LinkedHashMap<>();
		variables.put("path", "file:///" + path.toAbsolutePath().toString().replace("\\", "/") + "/");
		
		for (File file : path.resolve("special").toFile().listFiles()) {
			if (file.isDirectory())
				continue;
			
			List<String> lines = FileUtils.parseFile(file);
			
			for (Iterator<Entry<String, String>> iterator = variables.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String> entry = iterator.next();
				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);
					if (line.contains(entry.getKey())) {
						lines.set(i, line.replaceAll("§" + entry.getKey() + "§", entry.getValue()));
					}
				}
			}
			
			specialPages.put("$" + file.getName().replaceFirst("[.][^.]+$", "") + "$", String.join(System.lineSeparator(), lines));
		}
	}
	
}
