package core.records;

abstract class CostRecord extends Record {
	
	// ==== Properties ====
	
	private final double cost;
	
	
	// ==== Constructor ====
	
	public CostRecord(long dateTime, long userID, int userData, double cost) {
		super(dateTime, userID, userData);
		
		this.cost = cost;
	}
	
	
	// ==== Accessor ====
	
	public final double getCost() {
		return cost;
	}

}
