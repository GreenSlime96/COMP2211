package core.tables;

import java.util.Arrays;

public class ImpressionsTable implements CostTable {

	
	// ==== Properties ====
	
	int size;
	
	int[] dateTime;	
	short[] userData;
	float[] cost;
	
	
	// ==== Constructor ====
	
	public ImpressionsTable(int initialCapacity) {
		if (initialCapacity >= 0) {
			dateTime = new int[initialCapacity];
			userData = new short[initialCapacity];
			cost = new float[initialCapacity];
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
	}	
	
	public ImpressionsTable() {
		this(DEFAULT_CAPACITY);
	}	
	
	
	// ==== Accessors ====
	
	public void append(ImpressionsTable table) {
		System.arraycopy(table.dateTime, 0, dateTime, size, table.size);
		System.arraycopy(table.userData, 0, userData, size, table.size);
		System.arraycopy(table.cost, 0, cost, size, table.size);
		
		size += table.size;
	}
	
	public boolean add(int dateTime, short userData, float cost) {
		ensureCapacityInternal(size + 1);
			
		this.dateTime[size] = dateTime;
		this.userData[size] = userData;
		this.cost[size] = cost;
		
		size++;
		
		return true;
	}
	
	public int getDateTime(int index) {
		rangeCheck(index);
		
		return dateTime[index];
	}
	
	public short getUserData(int index) {
		rangeCheck(index);
		
		return userData[index];
	}
	
	public double getCost(int index) {
		rangeCheck(index);
		
		return cost[index];
	}
	
	public int size() {
		return size;
	}
	
    public void trimToSize() {
        if (size < dateTime.length) {
            dateTime = Arrays.copyOf(dateTime, size);
            userData = Arrays.copyOf(userData, size);
            cost = Arrays.copyOf(cost, size);
        }
    }
	
	
	// ==== Private Helper Methods ====
	
	private void ensureCapacityInternal(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - dateTime.length > 0)
            grow(minCapacity);
	}
	
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = dateTime.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        
        // minCapacity is usually close to size, so this is a win:
        dateTime = Arrays.copyOf(dateTime, newCapacity);
//        userID = Arrays.copyOf(userID, newCapacity);
        userData = Arrays.copyOf(userData, newCapacity);
        cost = Arrays.copyOf(cost, newCapacity);
    }
    
	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) // overflow
			throw new OutOfMemoryError();
		
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}
	
	private void rangeCheck(int index) {
		if (index >= size)
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size;
	}
}
