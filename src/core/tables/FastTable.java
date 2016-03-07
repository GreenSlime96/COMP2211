package core.tables;

import java.io.Serializable;

public class FastTable implements Serializable {
	
	private static final long serialVersionUID = 4460536860534037743L;
	
	short[][] userData;
	int[][] costData;
	
	int size;
	
	public FastTable(int initialCapacity) {
		userData = new short[initialCapacity][];
		costData = new int[initialCapacity][];
	}
	
	public void add(short[] count, int[] cost) {
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
