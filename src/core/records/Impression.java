package core.records;

import java.time.LocalDateTime;

public class Impression {
	
	// ==== Properties ====
	
	public final LocalDateTime date;
	public final long userID;
	public final double cost;
	
	
	// ==== Constructor ====
	
	public Impression(LocalDateTime date, long userID, double cost) {
		this.date = date;
		this.userID = userID;
		this.cost = cost;
	}
}
