package de.creativemd.launcher.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
	
	public static List<String> parseFile(Path path) {
		return parseFile(path.toFile());
	}
	
	public static List<String> parseFile(File file) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"));
			List<String> lines = reader.lines().collect(Collectors.toList());
			reader.close();
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void saveFile(Path path, List<String> lines) {
		saveFile(path.toFile(), lines);
	}
	
	public static void saveFile(File file, List<String> lines) {
		try {
			file.getParentFile().mkdirs();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "Cp1252"));
			for (String line : lines) {
				writer.write(line);
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
