package core;

import core.fields.Gender;
import core.fields.Income;

public class Filter {
	
	// ==== Properties ====
	
	final Gender gender;
	final String age;
	final Income income;
	final String context;
	
	
	// ==== Constructor ====
	
	public Filter(Gender gender, String age, Income income, String context) {
		this.gender = gender;
		this.age = age;
		this.income = income;
		this.context = context;
	}
}
