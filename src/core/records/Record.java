package core.records;

import java.time.LocalDateTime;
import util.DateProcessor;

public abstract class Record {
	
	// ==== Properties ====
	
	private final long epochSeconds;
	private final long userID;
	private final int userData;
	
	// ==== Constructor ====
	
	public Record(long dateTime, long userID, int userData) {
		this.epochSeconds = dateTime;
		this.userID = userID;
		this.userData = userData;
	}	
	
	// ==== Record Methods ====
	
	public final long getUserID() {
		return userID;
	}
	
	public final int getUserData() {
		return userData;
	}
	
	public final LocalDateTime getLocalDateTime() {		
		return DateProcessor.epochSecondsToLocalDateTime(epochSeconds);
	}
	
	public final long getEpochSeconds() {
		return epochSeconds;
	}
}
