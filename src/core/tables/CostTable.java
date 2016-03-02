package core.tables;

public interface CostTable {
	
	// ==== Constants ====
    
    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    static final int DEFAULT_CAPACITY = 10;
    
    
    // ==== Interface Methods ====
    
    double getCost(int index);

	int getDateTime(int index);
	
	short getUserData(int index);

	int size();
}
