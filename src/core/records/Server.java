 package core.records;

import java.time.LocalDateTime;

import util.DateProcessor;

public class Server extends Record {

	// ==== Properties ====

	private final long exitDateTime;
	private final int pagesViewed;
	private final boolean conversion;

	
	// ==== Constructor ====
	
	public Server(long dateTime, long userID, int userData, long exitDateTime, int pagesViewed, boolean conversion) {
		super(dateTime, userID, userData);
		
		this.exitDateTime = exitDateTime;
		this.pagesViewed = pagesViewed;
		this.conversion = conversion;
	}
	
	
	// ==== Accessor ====

	public final LocalDateTime getExitDateTime() {
		return DateProcessor.longToLocalDateTime(exitDateTime);
	}
	
	public final int getPagesViewed() {
		return pagesViewed;
	}
	
	public final boolean getConversion() {
		return conversion;
	}
}
