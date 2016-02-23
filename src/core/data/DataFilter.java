package core.data;

import java.util.function.Predicate;

public class DataFilter implements Predicate<Integer> {
	
	// ==== Constants ====
	
	public static final int FLAGS_ALL = -1;
	
	
	// ==== Properties ====
		
	private int flags;
	
	
	// ==== Constructor ====
	
	public DataFilter() {
		flags = FLAGS_ALL;
	}
	
	
	// ==== Accessors ====
	
	
	public boolean getField(User field) {
		return (1 & (flags << field.mask)) != 0;
	}
	
	public void setField(User field, boolean value) {
		if (value)
			flags |= field.mask;
		else
			flags &= ~field.mask;
	}
	
	
	// ==== Predicate Implementation ====

	@Override
	public boolean test(Integer t) {
		return test(t);
	}
	
	public boolean test(int user) {
		return (user & flags) == user;
	}
}
