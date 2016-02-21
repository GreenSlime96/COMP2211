package core.records;

abstract class CostRecord extends Record {
	
	// ==== Properties ====
	
	private final double cost;
	
	
	// ==== Constructor ====

	public CostRecord(String[] data, int length) {
		super(data, length);
		
		cost = Double.parseDouble(data[length - 1]);
	}
	
	public CostRecord(long dateTime, long userID, double cost) {
		super(dateTime, userID);
		
		this.cost = cost;
	}
	
	
	// ==== Accessor ====
	
	public final double getCost() {
		return cost;
	}

}
