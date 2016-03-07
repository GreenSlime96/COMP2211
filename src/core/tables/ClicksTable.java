package core.tables;

import java.util.Arrays;

public class ClicksTable implements CostTable {
	
	// ==== Properties ====
	
	int size;
	
	int[] dateTime;	
	long[] userID;	
	short[] userData;
	double[] cost;
	
	
	// ==== Constructor ====
	
	public ClicksTable(int initialCapacity) {
		if (initialCapacity >= 0) {
			dateTime = new int[initialCapacity];
			userID = new long[initialCapacity];
			userData = new short[initialCapacity];
			cost = new double[initialCapacity];
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
	}	
	
	public ClicksTable() {
		this(DEFAULT_CAPACITY);
	}	
	
	
	// ==== Accessors ====
	
	public boolean add(int dateTime, long userID, double cost) {
		ensureCapacityInternal(size + 1);
			
		this.dateTime[size] = dateTime;
		this.userID[size] = userID;
		this.cost[size] = cost;
		
		size++;
		
		return true;
	}
	
	public int getDateTime(int index) {
		rangeCheck(index);
		
		return dateTime[index];
	}
	
	public long getUserID(int index) {
		rangeCheck(index);
		
		return userID[index];
	}
	
	public short getUserData(int index) {
		rangeCheck(index);
		
		return userData[index];
	}
	
	public void setUserData(int index, short userData) {
		rangeCheck(index);

		this.userData[index] = userData;
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
    
    public int indexOfDate(int key) {      	
        int low = 0;
        int high = dateTime.length - 1;
    	
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = dateTime[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        
        return low;  // key not found.
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
