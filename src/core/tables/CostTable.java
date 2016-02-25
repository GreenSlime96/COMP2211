package core.tables;

import java.util.Arrays;

import core.records.CostRecord;

public class CostTable<E extends CostRecord> {
	
	// ==== Constants ====
    
    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    static final int DEFAULT_CAPACITY = 10;
	
	// ==== Properties ====
	
	int size;
	
	long[] dateTime;	
	long[] userID;	
	int[] userData;
	double[] cost;
	
	
	// ==== Constructor ====
	
	public CostTable(int initialCapacity) {
		if (initialCapacity >= 0) {
			dateTime = new long[initialCapacity];
			userID = new long[initialCapacity];
			userData = new int[initialCapacity];
			cost = new double[initialCapacity];
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
	}	
	
	public CostTable() {
		this(DEFAULT_CAPACITY);
	}	
	
	
	// ==== Accessors ====
	
	public boolean add(long dateTime, long userID, int userData, double cost) {
		ensureCapacityInternal(size + 1);
			
		this.dateTime[size] = dateTime;
		this.userID[size] = userID;
		this.userData[size] = userData;
		this.cost[size] = cost;
		
		size++;
		
		return true;
	}
	
	public boolean add(E costRecord) {
		final long dateTime = costRecord.getEpochSeconds();
		final long userID = costRecord.getUserID();
		final int userData = costRecord.getUserData();
		final double cost = costRecord.getCost();
		
		return add(dateTime, userID, userData, cost);
	}
	
	public long getDateTime(int index) {
		rangeCheck(index);
		
		return dateTime[index];
	}
	
	public long getUserID(int index) {
		rangeCheck(index);
		
		return userID[index];
	}
	
	public int getUserData(int index) {
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
            userID = Arrays.copyOf(userID, size);
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
        userID = Arrays.copyOf(userID, newCapacity);
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
