package de.creativemd.launcher.utils;

public class StringUtils {
	
	public static String toString(long nanoSeconds) {
		StringBuilder builder = new StringBuilder();
		int days = 0;
		if (nanoSeconds > 8.64e+13) // days
		{
			days = (int) (nanoSeconds / 8.64e+13);
			nanoSeconds -= ((long) days) * 8.64e+13;
			builder.append(days + "d ");
		}
		
		int hours = 0;
		if (nanoSeconds > 3.6e+12) // hours
		{
			hours = (int) (nanoSeconds / 3.6e+12);
			nanoSeconds -= ((long) hours) * 3.6e+12;
			builder.append(hours + "h ");
		}
		
		if (days > 0)
			return builder.toString();
		
		int minutes = 0;
		if (nanoSeconds > 6e+10) // minutes
		{
			minutes = (int) (nanoSeconds / 6e+10);
			nanoSeconds -= ((long) minutes) * 6e+10;
			builder.append(minutes + "m ");
		}
		
		if (hours > 0)
			return builder.toString();
		
		int seconds = 0;
		if (nanoSeconds > 1e+9) // seconds
		{
			seconds = (int) (nanoSeconds / 1e+9);
			nanoSeconds -= ((long) seconds) * 1e+9;
			builder.append(seconds + "s ");
		}
		
		if (minutes > 0 || seconds > 1)
			return builder.toString();
		
		int milliseconds = 0;
		if (nanoSeconds > 1000000) // milliseconds
		{
			milliseconds = (int) (nanoSeconds / 1000000);
			nanoSeconds -= ((long) milliseconds) * 1000000;
			builder.append(milliseconds + "ms ");
		}
		
		if (seconds > 0 || milliseconds > 0)
			return builder.toString();
		
		builder.append(nanoSeconds + "ns");
		return builder.toString();
	}
	
}
