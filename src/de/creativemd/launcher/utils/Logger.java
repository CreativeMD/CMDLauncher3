package de.creativemd.launcher.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logger {
	
	public Logger(boolean consoleOutput) {
		this.consoleOutput = consoleOutput;
	}
	
	public Logger() {
		this(false);
	}
	
	public boolean consoleOutput;
	String lastLine;
	List<ILogListener> listeners = new ArrayList<>();
	
	public void addListener(ILogListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(ILogListener listener) {
		this.listeners.remove(listener);
	}
	
	private String toString(Object object) {
		if (object instanceof boolean[])
			return Arrays.toString((boolean[]) object);
		if (object instanceof byte[])
			return Arrays.toString((byte[]) object);
		if (object instanceof short[])
			return Arrays.toString((short[]) object);
		if (object instanceof char[])
			return Arrays.toString((char[]) object);
		if (object instanceof int[])
			return Arrays.toString((int[]) object);
		if (object instanceof float[])
			return Arrays.toString((float[]) object);
		if (object instanceof long[])
			return Arrays.toString((long[]) object);
		if (object instanceof double[])
			return Arrays.toString((double[]) object);
		
		return Arrays.toString((Object[]) object);
	}
	
	private Object[] format(Object[] array) {
		Object[] newArray = null;
		for (int i = 0; i < array.length; i++) {
			if (array[i].getClass().isArray()) {
				newArray = new Object[array.length];
				break;
			}
		}
		
		if (newArray == null)
			return array;
		
		for (int i = 0; i < array.length; i++) {
			if (array[i].getClass().isArray())
				newArray[i] = toString(array[i]);
			else
				newArray[i] = array[i];
		}
		return newArray;
	}
	
	private static final String ANSI_RESET = "\u001B[0m";
	//private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	
	public void logError(String text, Object... params) {
		text = String.format(text, format(params));
		if (consoleOutput) {
			System.out.println(ANSI_RED + text + ANSI_RESET);
			
		}
		for (ILogListener listener : listeners) {
			listener.log(text, false, false);
		}
		lastLine = text;
	}
	
	public void log(String text, Object... params) {
		text = String.format(text, format(params));
		if (consoleOutput)
			System.out.println(text);
		for (ILogListener listener : listeners) {
			listener.log(text, false, false);
		}
		lastLine = text;
	}
	
	public void logLastLine(String text, Object... params) {
		text = String.format(text, format(params));
		if (!lastLine.equals(text)) {
			if (consoleOutput)
				System.out.println(text);
			for (ILogListener listener : listeners) {
				listener.log(text, true, false);
			}
			lastLine = text;
		}
		
	}
	
	public static interface ILogListener {
		
		public void log(String logged, boolean replace, boolean error);
		
	}
	
}