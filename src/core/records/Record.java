package core.records;

import java.time.LocalDateTime;
import java.util.Objects;

import util.DateProcessor;

public abstract class Record {
	
	// ==== Properties ====
	
	final long dateTime;
	final long userID;
	
//	LocalDateTime ldt;
	
	
	// ==== Constructor ====
	
	public Record(String[] data, int length) {
		Objects.requireNonNull(data);
		
		if (data.length != length)
			throw new IllegalArgumentException("invalid record");
		
		dateTime = DateProcessor.stringToLong(data[0]);
		userID = Long.parseLong(data[1]);
	}
	
	public Record(long dateTime, long userID) {
		this.dateTime = dateTime;
		this.userID = userID;
	}
	
	
	// ==== Record Methods ====
	
	public final long getUserID() {
		return userID;
	}
	
	public final LocalDateTime getDateTime() {
//		if (ldt == null && dateTime != DateProcessor.DATE_NULL)
//			ldt = DateProcessor.longToLocalDateTime(dateTime);
		
		return DateProcessor.longToLocalDateTime(dateTime);
	}
	
	public final long getLongDateTime() {
		return dateTime;
	}
}
