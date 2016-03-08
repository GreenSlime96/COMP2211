package core.data;

import java.util.function.Predicate;

import core.users.User;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

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
		
		if (flags == FLAGS_ALL) {
			sb.append("None");
		} else {
			for (User u : User.values()) {
				if (getField(u)) {
					sb.append(u.title);
					sb.append('\n');
				}
			}
		}
		
		return sb.toString();
	}
}
