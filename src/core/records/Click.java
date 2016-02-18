package core.records;

import java.time.LocalDateTime;

public class Click {
	
	// ==== Properties ====
	
	public final LocalDateTime date;
	public final long userID;
	public final double cost;
	
	
	// ==== Constructor ====
	
	public Click(LocalDateTime date, long userID, double cost) {
		this.date = date;
		this.userID = userID;
		this.cost = cost;
	}
	
}
