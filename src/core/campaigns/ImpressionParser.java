package core.campaigns;

import java.nio.ByteBuffer;

public class ImpressionParser {
	
	// ==== Unreachable Constructor ====
	
	private ImpressionParser() {
	}

	
	// ==== Static Methods ====
	
	public static long parseUserID(ByteBuffer byteBuffer) {
		long userID = byteBuffer.get() & 0xF;

		byte temp;
		while ((temp = byteBuffer.get()) != ',') {
			userID *= 10;
			userID += temp & 0xF;
		}
		
		return userID;
	}
	
	public static short parseCost(ByteBuffer byteBuffer) {
		short costTemp = (short) (byteBuffer.get() & 0xF);

		byte temp;
		while ((temp = byteBuffer.get()) != '.') {
			costTemp *= 10;
			costTemp += temp & 0xF;
		}

		costTemp *= 10;
		costTemp += byteBuffer.get() & 0xF;

		costTemp *= 10;
		costTemp += byteBuffer.get() & 0xF;

		costTemp *= 10;
		costTemp += byteBuffer.get() & 0xF;

		costTemp *= 10;
		costTemp += byteBuffer.get() & 0xF;

		costTemp *= 10;
		costTemp += byteBuffer.get() & 0xF;

		costTemp *= 10;
		costTemp += byteBuffer.get() & 0xF;
		
		if (costTemp < 0) {
			System.out.println("error: overflow!");
			System.exit(0);
		}
		
		return costTemp;
	}
}
