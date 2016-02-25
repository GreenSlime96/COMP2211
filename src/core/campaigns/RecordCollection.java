package core.campaigns;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;

import core.records.*;

public class RecordCollection {
	
	// ==== Constants ====
	
	static final int BYTES_PER_RECORD = 28;
	
	
	// ==== Properties ====
	
	ByteBuffer byteBuffer;
	
	int numberOfElements;
	
	
	// ==== Constructor ====
	
	public RecordCollection(int initialCapacity) {
		byteBuffer = ByteBuffer.allocate(initialCapacity * BYTES_PER_RECORD);		
	}
	
	
	// ==== Accessors ====
	
	public boolean add(long dateTime, long userID, int userData, double cost) {
		byteBuffer.putLong(dateTime);
		byteBuffer.putLong(userID);
		byteBuffer.putInt(userData);
		byteBuffer.putDouble(cost);		
		
		numberOfElements++;
		
		return true;
	}
	
	public long getDateTime(int index) {
		rangeCheck(index);
		
		return byteBuffer.getLong(index * BYTES_PER_RECORD);
	}
	
	public long getUserID(int index) {
		rangeCheck(index);
		
		return byteBuffer.getLong(index * BYTES_PER_RECORD + 8);
	}
	
	public int getUserData(int index) {
		rangeCheck(index);
		
		return byteBuffer.getInt(index * BYTES_PER_RECORD + 16);
	}
	
	public double getCost(int index) {
		rangeCheck(index);
		
		return byteBuffer.getDouble(index * BYTES_PER_RECORD + 20);
	}	
	
	// ==== Private Helper Methods ====
	
	private void rangeCheck(int index) {
		if (index >= numberOfElements)
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + numberOfElements;
	}
}
