package core.users;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import gnu.trove.TCollections;
import gnu.trove.list.TShortList;
import gnu.trove.list.array.TShortArrayList;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.hash.TShortByteHashMap;

public enum User {
	GENDER_MALE("Male"), 
	GENDER_FEMALE("Female"),
	
	AGE_BELOW_25("<25"),
	AGE_25_TO_34("25-34"),
	AGE_35_TO_44("35-44"),
	AGE_45_TO_54("45-54"),
	AGE_ABOVE_54(">54"),
	
	INCOME_LOW("Low"),
	INCOME_MEDIUM("Medium"),
	INCOME_HIGH("High"),
	
	CONTEXT_NEWS("News"),
	CONTEXT_SHOPPING("Shopping"),
	CONTEXT_SOCIAL_MEDIA("Social Media"),
	CONTEXT_BLOG("Blog"),
	CONTEXT_HOBBIES("Hobbies"),
	CONTEXT_TRAVEL("Travel");

	// ==== Constants ====
	
	public static final TShortByteMap byteCache;
	public static final short[] shortCache;
	
	static {
		// internal class to represent a node
		class Node {			
			private List<Node> children;
			final short mask;
			
			public Node(short mask, User value) {
				this.mask = (short) (mask | value.mask);
			}
			
			public Node() {
				mask = 0;
			}
			
			public Node addChild(User value) {
				if (children == null)
					children = new ArrayList<Node>();
				
				final Node node = new Node(mask, value);
				children.add(node);
				
				return node;
			}
		}
		
		// create empty root node -- start of tree
		final Node root = new Node();
		
		// smart way of caching enums, boolean set
		boolean[] processed = new boolean[values().length];
		
		// processing fringe
		List<Node> fringe = new ArrayList<Node>();
		fringe.add(root);
		
		for (User u : values()) {
			// if this has been processed before
			if (processed[u.ordinal()])
				continue;
			
			List<Node> newFringe = new ArrayList<Node>();

			// process similar prefixes
			for (User v : values()) {
				
				// if prefixes are same, mark as processed
				if (v.prefix.equals(u.prefix)) {
					processed[v.ordinal()] = true;
					
					// breadth-first generation
					for (Node n : fringe)
						newFringe.add(n.addChild(v));
				}
			}
			
			fringe = newFringe;
		}
		
		// list that grows!
		TShortByteHashMap tempMap = new TShortByteHashMap();
		TShortList shorts = new TShortArrayList();
		
		// dfs to track nodes
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		while (!stack.isEmpty()) {
			Node node = stack.pop();
			
			if (node.children == null) {				
				tempMap.put(node.mask, (byte) (shorts.size() + Byte.MIN_VALUE));
				shorts.add(node.mask);
				System.out.println(node.mask);
			} else {
				for (Node n : node.children) {
					stack.push(n);
				}
			}			
		}
		
		tempMap.compact();
		
		byteCache = TCollections.unmodifiableMap(tempMap);
		shortCache = shorts.toArray();
	}
	
	private static final int[] info_map;
	private static final int[] skip_map;
	
	static {
		info_map = new int[128];
		skip_map = new int[128];
		
		// 1st character of age
		info_map['<'] = AGE_BELOW_25.mask;
		info_map['2'] = AGE_25_TO_34.mask;
		info_map['3'] = AGE_35_TO_44.mask;
		info_map['4'] = AGE_45_TO_54.mask;
		info_map['>'] = AGE_ABOVE_54.mask;
		
		// 1st character of income
		info_map['L'] = INCOME_LOW.mask;
		info_map['M'] = INCOME_MEDIUM.mask;
		info_map['H'] = INCOME_HIGH.mask;
		
		// 4th character of context
		info_map['s'] = CONTEXT_NEWS.mask;
		info_map['p'] = CONTEXT_SHOPPING.mask;
		info_map['i'] = CONTEXT_SOCIAL_MEDIA.mask;
		info_map['g'] = CONTEXT_BLOG.mask;
		info_map['b'] = CONTEXT_HOBBIES.mask;
		info_map['v'] = CONTEXT_TRAVEL.mask;
		
		// 1st character of age
		skip_map['<'] = AGE_BELOW_25.length;
		skip_map['2'] = AGE_25_TO_34.length;
		skip_map['3'] = AGE_35_TO_44.length;
		skip_map['4'] = AGE_45_TO_54.length;
		skip_map['>'] = AGE_ABOVE_54.length;
		
		// 1st character of income
		skip_map['L'] = INCOME_LOW.length + 3;
		skip_map['M'] = INCOME_MEDIUM.length + 3;
		skip_map['H'] = INCOME_HIGH.length + 3;
		
		// 4th character of context
		skip_map['s'] = CONTEXT_NEWS.length - 4;
		skip_map['p'] = CONTEXT_SHOPPING.length- 4;
		skip_map['i'] = CONTEXT_SOCIAL_MEDIA.length- 4;
		skip_map['g'] = CONTEXT_BLOG.length- 4;
		skip_map['b'] = CONTEXT_HOBBIES.length- 4;
		skip_map['v'] = CONTEXT_TRAVEL.length- 4;
	}
	
	
	// ==== Properties ====

	public final int mask;
	public final String title;
	public final int length;
	public final String prefix;

	
	// ==== Constructor ====

	User(String string) {
		prefix = name().substring(0, name().indexOf('_')).intern();
		
		mask = 1 << ordinal();
		
		title = string;
		length = string.length();
	}
	
	
	// ==== Object Overrides ====
	
	@Override
	public String toString() {
		return title;
	}
	

	// ==== Static Helper Methods ====

	public static byte compressUser(short userData) {		
		return byteCache.get(userData);
	}
	
	public static int compress(short userData) {		
		return byteCache.get(userData) - Byte.MIN_VALUE;
	}
	
	public static short unpackUser(byte userByte) {
		return shortCache[userByte - Byte.MIN_VALUE];
	}
	
	public static short unpackUser(int userByte) {
		return shortCache[userByte];
	}
	
	public static short encodeUser(ByteBuffer bb) throws InvalidUserException {
		byte temp;
		
		short userData = 0;
		
		if ((temp = bb.get()) == 'F') {
			userData |= User.GENDER_FEMALE.mask;
			bb.position(bb.position() + 6);
		} else if (temp == 'M') {
			userData |= User.GENDER_MALE.mask;
			bb.position(bb.position() + 4);
		} else {
			throw new InvalidUserException("invalid gender");
		}

		// age
		temp = bb.get();
		userData |= info_map[temp];
		bb.position(bb.position() + skip_map[temp]);
		
		// income
		temp = bb.get();
		userData |= info_map[temp];
		bb.position(bb.position() + skip_map[temp]);
				
		// context
		temp = bb.get();
		userData |= info_map[temp];
		bb.position(bb.position() + skip_map[temp]);
		
		if (bb.get() != ',')
			throw new InvalidUserException("invalid user: " + Integer.toBinaryString(userData));
		
		return userData;
	}
	
	public static int _encodeUser(MappedByteBuffer mbb) throws InvalidUserException {
		byte temp;
		int index = mbb.position();
		int userData = 0;
		
		if ((temp = mbb.get()) == 'F') {
			userData |= User.GENDER_FEMALE.mask;
			mbb.position(index += 7);
		} else if (temp == 'M') {
			userData |= User.GENDER_MALE.mask;
			mbb.position(index += 5);
		} else {
			throw new InvalidUserException("invalid gender");
		}

		if ((temp = mbb.get()) == '2') {
			userData |= User.AGE_25_TO_34.mask;
			mbb.position(index += 6);
		} else if (temp == '4') {
			userData |= User.AGE_45_TO_54.mask;
			mbb.position(index += 6);
		} else if (temp == '3') {
			userData |= User.AGE_35_TO_44.mask;
			mbb.position(index += 6);
		} else if (temp == '>') {
			userData |= User.AGE_ABOVE_54.mask;
			mbb.position(index += 4);
		} else if (temp == '<') {
			userData |= User.AGE_BELOW_25.mask;
			mbb.position(index += 4);
		} else {
			throw new InvalidUserException("invalid age");
		}

		if ((temp = mbb.get()) == 'H') {
			userData |= User.INCOME_HIGH.mask;
			mbb.position(index += 5);
		} else if (temp == 'M') {
			userData |= User.INCOME_MEDIUM.mask;
			mbb.position(index += 7);
		} else if (temp == 'L') {
			userData |= User.INCOME_LOW.mask;
			mbb.position(index += 4);
		} else {
			throw new InvalidUserException("invalid income");
		}

		if ((temp = mbb.get()) == 'N') {
			userData |= User.CONTEXT_NEWS.mask;
			mbb.position(index += 5);
		} else if (temp == 'S') {
			if ((temp = mbb.get()) == 'o') {
				userData |= User.CONTEXT_SOCIAL_MEDIA.mask;
				mbb.position(index += 13);
			} else if (temp == 'h') {
				userData |= User.CONTEXT_SHOPPING.mask;
				mbb.position(index += 9);
			} else {
				throw new InvalidUserException("invalid context");
			}
		} else if (temp == 'B') {
			userData |= User.CONTEXT_BLOG.mask;
			mbb.position(index += 5);
		} else if (temp == 'T') {
			userData |= User.CONTEXT_TRAVEL.mask;
			mbb.position(index += 7);
		} else if (temp == 'H') {
			userData |= User.CONTEXT_HOBBIES.mask;
			mbb.position(index += 8);
		} else {
			throw new InvalidUserException("invalid context");
		}
		
		return userData;
	}
	
	public static boolean checkFlag(short userData, User field) {
		return (userData & field.mask) != 0;
	}
}
