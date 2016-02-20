package core.records;

import java.time.LocalDateTime;
import java.util.Objects;

import util.DateProcessor;

public abstract class Record {
	
	// ==== Properties ====
	
	final long dateTime;
	final long userID;

	
	// ==== Constructor ====
	
	public Record(String[] data, int length) {
		Objects.requireNonNull(data);
		
		if (data.length != length)
			throw new IllegalArgumentException("invalid record");
		
		dateTime = DateProcessor.stringToLong(data[0]);
		userID = Long.parseLong(data[1]);
	}
	
	
	// ==== Record Methods ====
	
	public final long getUserID() {
		return userID;
	}
	
	public final LocalDateTime getDateTime() {
		return DateProcessor.longToLocalDateTime(dateTime);
	}
}
