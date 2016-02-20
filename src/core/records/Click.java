package core.records;

public class Click extends CostRecord {

	// ==== Constructor ====

	public Click(String string) {
		this(string.split(","));
	}

	public Click(String[] data) {
		super(data, 3);
	}
}
