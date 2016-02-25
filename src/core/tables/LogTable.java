package core.tables;

import java.util.Arrays;

import core.records.Server;

public class LogTable<E extends Server> {
	
	// ==== Constants ====
    
    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    static final int DEFAULT_CAPACITY = 10;
	
	// ==== Properties ====
	
	int size;
	
	long[] dateTime;	
	long[] userID;	
	int[] userData;
	long[] exitDateTime;
	int[] pagesViewed;
	boolean[] conversion;
	
	// ==== Constructor ====
	
	public LogTable(int initialCapacity) {
		if (initialCapacity >= 0) {
			dateTime = new long[initialCapacity];
			userID = new long[initialCapacity];
			userData = new int[initialCapacity];
			exitDateTime = new long[initialCapacity];
			pagesViewed = new int[initialCapacity];
			conversion = new boolean[initialCapacity];
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
	}	
	
	public LogTable() {
		this(DEFAULT_CAPACITY);
	}	
	
	
	// ==== Accessors ====
	
	public boolean add(long dateTime, long userID, int userData, long exitDateTime, int pagesViewed, boolean conversion) {
		ensureCapacityInternal(size + 1);
			
		this.dateTime[size] = dateTime;
		this.userID[size] = userID;
		this.userData[size] = userData;
		this.exitDateTime[size] = exitDateTime;
		this.pagesViewed[size] = pagesViewed;
		this.conversion[size] = conversion;
		
		size++;
		
		return true;
	}
	
	public boolean add(E serverRecord) {
		final long dateTime = serverRecord.getEpochSeconds();
		final long userID = serverRecord.getUserID();
		final int userData = serverRecord.getUserData();
		final long exitDateTime = serverRecord.getExitEpochSeconds();
		final int pagesViewed = serverRecord.getPagesViewed();
		final boolean conversion = serverRecord.getConversion();
		
		return add(dateTime, userID, userData, exitDateTime, pagesViewed, conversion);
	}
	
	public void setUserData(int index, int userData) {
		rangeCheck(index);
		
		this.userData[index] = userData;
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
	
	public long getExitDateTime(int index) {
		rangeCheck(index);
		
		return exitDateTime[index];
	}
	
	public int getPagesViewed(int index) {
		rangeCheck(index);
		
		return pagesViewed[index];
	}
	
	public boolean getConversion(int index) {
		rangeCheck(index);
		
		return conversion[index];
	}
	
	public int size() {
		return size;
	}
	
    public void trimToSize() {
        if (size < dateTime.length) {
            dateTime = Arrays.copyOf(dateTime, size);
            userID = Arrays.copyOf(userID, size);
            userData = Arrays.copyOf(userData, size);
            exitDateTime = Arrays.copyOf(exitDateTime, size);
            pagesViewed = Arrays.copyOf(pagesViewed, size);
            conversion = Arrays.copyOf(conversion, size);
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
        exitDateTime = Arrays.copyOf(exitDateTime, size);
        pagesViewed = Arrays.copyOf(pagesViewed, size);
        conversion = Arrays.copyOf(conversion, size);
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
