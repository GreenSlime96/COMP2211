package core.records;

import core.fields.Gender;
import core.fields.Income;

public class User {
	
	// ==== Properties ====
	
	public final long id;
	public final Gender gender;
	public final String age;
	public final Income income;
	public final String context;
	
	
	// ==== Constructor ====
	
	public User(long id, Gender gender, String age, Income income, String context) {
		this.id = id;
		this.gender = gender;
		this.age = age.intern();
		this.income = income;
		this.context = context.intern();
	}
	
	
	// ==== Object Overrides ====

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
