package core.tables;

public class FastTable {
	
	int[] userData;
	int[] costData;
	
	int size;
	
	public FastTable(int initialCapacity) {
		userData = new int[initialCapacity * 180];
		costData = new int[initialCapacity * 180];
	}
	
//	public void add(short[] count, int[] cost) {
//		
//		userData[size] = count;
//		costData[size] = cost;
//		
//		size++;
//	}
//	
//	public int[] getCost(int index) {
//		return costData[index];
//	}
//	
//	public short[] getCount(int index) {
//		return userData[index];
//	}
	
	public int size() {
		return size;
	}

}
