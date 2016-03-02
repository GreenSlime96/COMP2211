package core.data;

import java.util.function.Predicate;

import core.users.User;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

public class DataFilter implements Predicate<Short> {
	
	// ==== Constants ====
	
	public static final short FLAGS_ALL = -1;
	public static final short FLAGS_NONE = 0;
	
	
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
			flags &= ~field.mask;
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
		String out = "Filters applied: ";
		for(int i=0; i<User.values().length;  i++)
		{
			if(getField(User.values()[i])) out += User.values()[i].toString() + (i < User.values().length - 1 ? ", " : "");
		}
		if(flags == FLAGS_NONE) out += "None";
		return out;
	}
}
