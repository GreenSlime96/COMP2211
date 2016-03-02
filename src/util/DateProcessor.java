package util;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateProcessor {
	
	// ==== Constants ====
	
	public static final int DATE_NULL = -1;
	
	static final ZoneOffset DEFAULT_ZONE = ZoneOffset.UTC;
	
	
	// ==== Static Methods ====
	
	public static int toEpochSeconds(char[] data) {
		if (data.length != 19)
			if (data[0] == 'n' && data[1] == '/' && data[2] == 'a')
				return DATE_NULL;
			else
				throw new IllegalArgumentException("date in incorrect format! " + new String(data));
		
		final int year = charArrayToInt(data, 0, 4);
		final int month = charArrayToInt(data, 5, 7);
		final int day = charArrayToInt(data, 8, 10);
		
		final int hour = charArrayToInt(data, 11, 13);
		final int minute = charArrayToInt(data, 14, 16);
		final int second = charArrayToInt(data, 17, 19);
		
		return toEpochSeconds(year, month, day, hour, minute, second);	
	}
	
	public static int _toEpochSeconds(ByteBuffer bb) {
		int index = bb.position();
		
		int year = bb.get() & 0xF;
		year *= 10;
		year += bb.get() & 0xF;
		year *= 10;
		year += bb.get() & 0xF;
		year *= 10;
		year += bb.get() & 0xF;

		bb.position(index += 5);

		int month = bb.get() & 0xF;
		month *= 10;
		month += bb.get() & 0xF;

		bb.position(index += 3);

		int day = bb.get() & 0xF;
		day *= 10;
		day += bb.get() & 0xF;

		bb.position(index += 3);

		int hour = bb.get() & 0xF;
		hour *= 10;
		hour += bb.get() & 0xF;

		bb.position(index += 3);

		int minute = bb.get() & 0xF;
		minute *= 10;
		minute += bb.get() & 0xF;

		bb.position(index += 3);

		int second = bb.get() & 0xF;
		second *= 10;
		second += bb.get() & 0xF;

		bb.position(index += 3);

		return toEpochSeconds(year, month, day, hour, minute, second);	
	}
	
	public static int toEpochSeconds(ByteBuffer bb) {
		int year = bb.get() & 0xF;
		year *= 10;
		year += bb.get() & 0xF;
		year *= 10;
		year += bb.get() & 0xF;
		year *= 10;
		year += bb.get() & 0xF;

		bb.position(bb.position() + 1);

		int month = bb.get() & 0xF;
		month *= 10;
		month += bb.get() & 0xF;

		bb.position(bb.position() + 1);
		
		int day = bb.get() & 0xF;
		day *= 10;
		day += bb.get() & 0xF;

		bb.position(bb.position() + 1);
		
		int hour = bb.get() & 0xF;
		hour *= 10;
		hour += bb.get() & 0xF;

		bb.position(bb.position() + 1);
		
		int minute = bb.get() & 0xF;
		minute *= 10;
		minute += bb.get() & 0xF;

		bb.position(bb.position() + 1);
		
		int second = bb.get() & 0xF;
		second *= 10;
		second += bb.get() & 0xF;

		bb.position(bb.position() + 1);
		
		return toEpochSeconds(year, month, day, hour, minute, second);	
	}
	
	public static long toEpochSeconds(LocalDateTime dateTime) {
		return dateTime.toEpochSecond(DEFAULT_ZONE);
	}
	
	public static int toEpochSeconds(String data) {
		return toEpochSeconds(data.toCharArray());
	}
	
	public static LocalDateTime toLocalDateTime(long epochSecond) {
		return LocalDateTime.ofEpochSecond(epochSecond, 0, DEFAULT_ZONE);
	}
	
	// ==== Private Helper Methods ====
	
	private static int toEpochSeconds(int year, int month, int day, int hour, int minute, int second) {
		int total = 0;
		
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
}
