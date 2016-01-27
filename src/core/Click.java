package core;

import java.util.Date;

public class Click {
	
	// ==== Properties ====
	
	public final Date date;
	public final long userID;
	public final double cost;
	
	
	// ==== Constructor ====
	
	public Click(Date date, long userID, double cost) {
		this.date = date;
		this.userID = userID;
		this.cost = cost;
	}
	
}
