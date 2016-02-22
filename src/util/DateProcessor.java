package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateProcessor {
	
	// ==== Constants ====
	
	public static final long DATE_NULL = -1;
	
	static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	
	// ==== Static Methods ====
	
	/**
	 * Converts a Date String into a long
	 * 
	 * @param date - string representation of date in yyyy-MM-dd HH:mm:ss
	 * @return long representing the date
	 */
	public static long stringToLong(String date) {
		return charArrayToLong(date.toCharArray());
	}
	
	public static long charArrayToLong(char[] data) {
		if (data.length < 19)
			if (data[0] == 'n' && data[1] == '/' && data[2] == 'a')
				return DATE_NULL;
			else
				throw new IllegalArgumentException("date in incorrect format!");
		
		final long year = charArrayToInt(data, 0, 4);
		final long month = charArrayToInt(data, 5, 7);
		final long day = charArrayToInt(data, 8, 10);
		
		final int hour = charArrayToInt(data, 11, 13);
		final int minute = charArrayToInt(data, 14, 16);
		final int second = charArrayToInt(data, 17, 19);	

		return (year << 48) | (month << 40) | (day << 32) | (hour << 24) | (minute << 16) | (second << 8);
	}
	
	
	/**
	 * Converts a long back into LocalDateTime
	 * 
	 * @param dateTime the long representation of this date
	 * @return  the LocalDateTime represented by thie date
	 */
	public static LocalDateTime longToLocalDateTime(long dateTime) {
		if (dateTime == DATE_NULL)
			return null;
		
		final int year = (int) (dateTime >> 48);
		final int month = (int) (dateTime >> 40 & 0xFF);
		final int day = (int) (dateTime >> 32 & 0xFF);
		
		// process time
		final int hour = (int) (dateTime >> 24 & 0xFF);
		final int minute = (int) (dateTime >> 16 & 0xFF);
		final int second = (int) (dateTime >> 8 & 0xFF);
		
		// create new LocalDateTime instance
		return LocalDateTime.of(year, month, day, hour, minute, second);
	}
	
	
	public static LocalDateTime stringToLocalDateTime(String date) {
		return parseDate(date);
	}
	
	public static LocalDateTime parse(String date) {
		return parseDate(date);
	}
	
	
	// ==== Private Helper Methods ====
	
	private static LocalDateTime parseDate(String date) {
		final int year = Integer.parseInt(date.substring(0, 4));
		final int month = Integer.parseInt(date.substring(5, 7));
		final int day = Integer.parseInt(date.substring(8, 10));
		final int hour = Integer.parseInt(date.substring(11, 13));
		final int minute = Integer.parseInt(date.substring(14, 16));
		final int second = Integer.parseInt(date.substring(17, 19));

		return LocalDateTime.of(year, month, day, hour, minute, second);
	}
	
	/**
	 * Converts an array of characters into digits
	 * 
	 * @param data - character buffer representing data
	 * @param start - start reference
	 * @param end - end reference
	 * @return
	 */
	public static int charArrayToInt(char[] data, int start, int end) {
		int result = 0;
		
		for (int i = start; i < end; i++) {				
			result *= 10;
			result += data[i] & 0xF;
		}
		
		return result;
	}
	
	public static int charArrayToInt(byte[] data, int start, int end) {
		int result = 0;
		
		for (int i = start; i < end; i++) {				
			result *= 10;
			result += data[i] & 0xF;
		}
		
		return result;
	}
}
