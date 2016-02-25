package util;

import java.nio.MappedByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;

public class DateProcessor {
	
	// ==== Constants ====
	
	public static final long DATE_NULL = -1;
	
	static final ZoneOffset DEFAULT_ZONE = ZoneOffset.UTC;
	
	// see LocalDate.class header
	static final int DAYS_PER_CYCLE = 146097;
    static final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);
    
    // see LocalTime.class header
    static final int MINUTES_PER_HOUR = 60;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	
	
	// ==== Static Methods ====
	
	/**
	 * Converts a Date String into a long
	 * 
	 * @param date - string representation of date in yyyy-MM-dd HH:mm:ss
	 * @return long representing the date
	 */
	public static long stringToLong(String date) {
		return compactToLong(date.toCharArray());
	}
	
	public static long compactToLong(char[] data) {
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
	
	public static long toEpochSeconds(char[] data) {
		if (data.length != 19)
			if (data[0] == 'n' && data[1] == '/' && data[2] == 'a')
				return DATE_NULL;
			else
				throw new IllegalArgumentException("date in incorrect format!");
		
		final int year = charArrayToInt(data, 0, 4);
		final int month = charArrayToInt(data, 5, 7);
		final int day = charArrayToInt(data, 8, 10);
		
		final int hour = charArrayToInt(data, 11, 13);
		final int minute = charArrayToInt(data, 14, 16);
		final int second = charArrayToInt(data, 17, 19);
		
		return toEpochSeconds(year, month, day, hour, minute, second);	
	}
	
	public static long toEpochSeconds(byte[] data) {
		if (data.length != 19)
			if (data[0] == 'n' && data[1] == '/' && data[2] == 'a')
				return DATE_NULL;
			else
				throw new IllegalArgumentException("date in incorrect format!");
		
//		final int year = 1000 * (data[0] & 0xF) + 100 * (data[1] & 0xF) + 10 * (data[2] & 0xF) + (data[3] & 0xF);
//		final int month = 10 * (data[5] & 0xF) + (data[6] & 0xF);
//		final int day = 10 * (data[8] & 0xF) + (data[9] & 0xF);
//		
//		final int hour = 10 * (data[11] & 0xF) + (data[12] & 0xF);
//		final int minute = 10 * (data[14] & 0xF) + (data[15] & 0xF);
//		final int second = 10 * (data[17] & 0xF) + (data[18] & 0xF);
		
		final int year = byteArrayToInt(data, 0, 4);
		final int month = byteArrayToInt(data, 5, 7);
		final int day = byteArrayToInt(data, 8, 10);
		
		final int hour = byteArrayToInt(data, 11, 13);
		final int minute = byteArrayToInt(data, 14, 16);
		final int second = byteArrayToInt(data, 17, 19);
		
		return toEpochSeconds(year, month, day, hour, minute, second);	
	}
	
	public static long toEpochSeconds(MappedByteBuffer mbb) {
		int index = mbb.position();
		
		int year = mbb.get() & 0xF;
		year *= 10;
		year += mbb.get() & 0xF;
		year *= 10;
		year += mbb.get() & 0xF;
		year *= 10;
		year += mbb.get() & 0xF;

		mbb.position(index += 5);

		int month = mbb.get() & 0xF;
		month *= 10;
		month += mbb.get() & 0xF;

		mbb.position(index += 3);

		int day = mbb.get() & 0xF;
		day *= 10;
		day += mbb.get() & 0xF;

		mbb.position(index += 3);

		int hour = mbb.get() & 0xF;
		hour *= 10;
		hour += mbb.get() & 0xF;

		mbb.position(index += 3);

		int minute = mbb.get() & 0xF;
		minute *= 10;
		minute += mbb.get() & 0xF;

		mbb.position(index += 3);

		int second = mbb.get() & 0xF;
		second *= 10;
		second += mbb.get() & 0xF;

		mbb.position(index += 3);

		return toEpochSeconds(year, month, day, hour, minute, second);	
	}
	
	public static long toEpochSeconds(LocalDateTime dateTime) {
		return dateTime.toEpochSecond(DEFAULT_ZONE);
	}
	
	public static long stringToEpoch(String data) {
		return toEpochSeconds(data.toCharArray());
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
	
	public static long toEpochSeconds(long dateTime) {
		if (dateTime == DATE_NULL)
			return DATE_NULL;
		
		final int year = (int) (dateTime >> 48);
		final int month = (int) (dateTime >> 40 & 0xFF);
		final int day = (int) (dateTime >> 32 & 0xFF);
		
		// process time
		final int hour = (int) (dateTime >> 24 & 0xFF);
		final int minute = (int) (dateTime >> 16 & 0xFF);
		final int second = (int) (dateTime >> 8 & 0xFF);
		
		final ZonedDateTime zdt = ZonedDateTime.of(year, month, day, hour, minute, second, 0, DEFAULT_ZONE);
		
		// create new LocalDateTime instance
		return zdt.toEpochSecond();
	}
	
	public static LocalDateTime epochSecondsToLocalDateTime(long epochSecond) {
		return LocalDateTime.ofEpochSecond(epochSecond, 0, DEFAULT_ZONE);
	}
 	
	
	public static LocalDateTime stringToLocalDateTime(String date) {
		return parseDate(date);
	}
	
	public static LocalDateTime parse(String date) {
		return parseDate(date);
	}
	
	
	// ==== Private Helper Methods ====
	
	private static long toEpochSeconds(int year, int month, int day, int hour, int minute, int second) {
		long total = 0;
		
		total += 365 * year;
		total += (year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400;
		total += ((367 * month - 362) / 12);
		total += day - 1;
		if (month > 2) {
			total--;
			// if is a leap year
			if (!((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0)) {
				total--;
			}
		}

		return (total - 719528) * 86400 + hour * 3600 + minute * 60 + second;	
	}
	
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
	private static int charArrayToInt(char[] data, int start, int end) {
		int result = 0;
		
		for (int i = start; i < end; i++) {				
			result *= 10;
			result += data[i] & 0xF;
		}
		
		return result;
	}
	
	private static int byteArrayToInt(byte[] data, int start, int end) {
		int result = 0;
		
		for (int i = start; i < end; i++) {				
			result *= 10;
			result += data[i] & 0xF;
		}
		
		return result;
	}

}
