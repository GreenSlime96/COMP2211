package core.data;

import java.util.function.Predicate;

import core.users.User;

public class DataFilter implements Predicate<Short> {
	
	// ==== Constants ====
	
	public static final short FLAGS_ALL = -1;
	
	
	// ==== Properties ====
		
	private short flags;
	
	
	// ==== Constructor ====
	
	public DataFilter() {
		flags = FLAGS_ALL;
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
	
	
	// ==== Predicate Implementation ====

	@Override
	public boolean test(Short t) {
		return test(t);
	}
	
	public boolean test(short user) {
		return (user & flags) == user;
	}
}
