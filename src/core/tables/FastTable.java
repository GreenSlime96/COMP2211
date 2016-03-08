package core.tables;

public class FastTable {
	
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
