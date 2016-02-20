package core.records;

public class Impression extends CostRecord {

	// ==== Constructor ====

	public Impression(String string) {
		this(string.split(","));
	}

	public Impression(String[] data) {
		super(data, 7);
	}
}
