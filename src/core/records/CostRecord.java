package core.records;

public class CostRecord extends Record {
	
	// ==== Properties ====
	
	private final double cost;
	
	
	// ==== Constructor ====

	public CostRecord(String[] data, int length) {
		super(data, length);
		
		cost = Double.parseDouble(data[length - 1]);
	}
	
	
	// ==== Accessor ====
	
	public final double getCost() {
		return cost;
	}

}
