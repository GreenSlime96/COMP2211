package core.records;

import core.fields.Age;
import core.fields.Context;
import core.fields.Gender;
import core.fields.Income;

public class User {
	
	// ==== Properties ====
	
	public final long id;
	public final Gender gender;
	public final Age age;
	public final Income income;
	public final Context context;
	
	
	// ==== Constructor ====
	
	public User(long id, Gender gender, Age age, Income income, Context context) {
		this.id = id;
		this.gender = gender;
		this.age = age;
		this.income = income;
		this.context = context;
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
