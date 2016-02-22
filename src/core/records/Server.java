 package core.records;

import java.time.LocalDateTime;

import util.DateProcessor;

public class Server extends Record {

	// ==== Properties ====

	private final long exitDateTime;
	private final int pagesViewed;
	private final boolean conversion;

	
	// ==== Constructor ====
	
	public Server(String string) {
		this(string.split(","));
	}
	
	public Server(String[] data) {
		super(data, 5);
		
		exitDateTime = DateProcessor.stringToLong(data[2]);
		pagesViewed = Integer.parseInt(data[3]);
		conversion = data[4].equals("No") ? false : true;
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
