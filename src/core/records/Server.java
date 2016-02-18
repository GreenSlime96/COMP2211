package core.records;

import java.time.LocalDateTime;

public class Server {
	
	// ==== Properties ====
	
	public final LocalDateTime entryDate;
	public final long userID;
	public final LocalDateTime exitDate;
	public final int pagesViewed;
	public final boolean conversion;
	
	
	// ==== Constructor ====
	
	public Server(LocalDateTime entryDate, long userID, LocalDateTime exitDate, int pagesViewed, boolean conversion) {
		this.entryDate = entryDate;
		this.userID = userID;
		this.exitDate = exitDate;
		this.pagesViewed = pagesViewed;
		this.conversion = conversion;
	}
}
