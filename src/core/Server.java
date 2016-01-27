package core;

import java.util.Date;

public class Server {
	
	// ==== Properties ====
	
	public final Date entryDate;
	public final long userID;
	public final Date exitDate;
	public final int pagesViewed;
	public final boolean conversion;
	
	
	// ==== Constructor ====
	
	public Server(Date entryDate, long userID, Date exitDate, int pagesViewed, boolean conversion) {
		this.entryDate = entryDate;
		this.userID = userID;
		this.exitDate = exitDate;
		this.pagesViewed = pagesViewed;
		this.conversion = conversion;
	}
}
