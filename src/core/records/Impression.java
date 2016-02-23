package core.records;

public class Impression extends CostRecord {
	
	// ==== Constructor ====

	public Impression(long dateTime, long userID, int userData, double cost) {
		super(dateTime, userID, userData, cost);
	}	
}
