package core.data;

import core.fields.Gender;
import core.fields.Income;
import core.records.User;

public class DataFilter {
	
	// ==== Properties ====
	
	final Gender gender;
	final String age;
	final Income income;
	final String context;
	
	
	// ==== Constructor ====
	
	public DataFilter(Gender gender, String age, Income income, String context) {
		this.gender = gender;
		this.age = age;
		this.income = income;
		this.context = context;
	}
	
	public boolean apply(User user) {
		return user.gender == gender && user.age == age && user.income == income && user.context == context;
	}
}
