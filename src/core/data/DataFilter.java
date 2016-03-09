package core.data;

import java.util.function.Predicate;

import core.users.User;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

public class DataFilter implements Predicate<Short> {
	
	// ==== Constants ====
	
	public static final short FLAGS_ALL = -1;
	
	
	// ==== Properties ====
		
	private short flags;
	
	
	// ==== Constructor ====
	
	public DataFilter() {
		this(FLAGS_ALL);
	}
	
	public DataFilter(short flags) {
		this.flags = flags;
	}
	
	public DataFilter(DataFilter dataFilter) {
		this.flags = dataFilter.flags;
	}
	
	
	// ==== Accessors ====
	
	
	public boolean getField(User field) {
		return (flags & field.mask) == field.mask;
	}
	
	public void setField(User field, boolean value) {
		if (value) {
			flags |= field.mask;
		} else {			
			for (User user : User.values()) {				
				if (user.prefix.equals(field.prefix) && user != field && getField(user)) {
					flags &= ~field.mask;
					return;
				}
			}
		}
	}
	
	public int[] getByteFlags() {
		final TIntList list = new TIntArrayList();
		
		for (short flag : User.shortCache) {
			if (test(flag))
				list.add(flag);
		}
		
		return list.toArray();
	}
	
	
	// ==== Predicate Implementation ====

	@Override
	public boolean test(Short t) {
		return test(t);
	}
	
	public boolean test(short user) {
		return (user & flags) == user;
	}
	
	// ==== UI Hooks ====
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		String lastPrefix = User.values()[0].prefix;
		for (User u : User.values()) {
			if (!lastPrefix.equals(u.prefix)) {
				lastPrefix = u.prefix;
				sb.append('\n');
			}
			
			if (sb.length() - sb.lastIndexOf("\n") > 29) {
				sb.append('\n');
			}
			
			if (getField(u)) {
				sb.append(u.title);
				sb.append(' ');
				sb.append(' ');
			}
		}
		
		return sb.toString();
	}
}
