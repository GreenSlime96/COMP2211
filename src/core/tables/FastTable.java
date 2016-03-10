package core.tables;

import core.users.User;

public class FastTable {
	
	// store the possibilities here
	
	static final int combinations = User.combinations;
	
	// arrays to store information	
	
	int[] userData;
	int[] costData;
	
	int startHour;
	
	int size;
	
	public FastTable(int initialCapacity) {
		userData = new int[initialCapacity * combinations];
		costData = new int[initialCapacity * combinations];
	}
	
	public void add(short[] count, int[] cost) {
		if (count.length != combinations || cost.length != combinations)
			throw new IllegalArgumentException("invalid length");
		
		userData[size] = count;
		costData[size] = cost;
		
		size++;
	}
	
	public int[] getCost(int index) {
		return costData[index];
	}
	
	public short[] getCount(int index) {
		return userData[index];
	}
	
	public int size() {
		return size;
	}

}
