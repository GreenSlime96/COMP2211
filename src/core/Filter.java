package core;

import core.fields.Gender;
import core.fields.Income;

import java.util.HashSet;

public class Filter {
	
	// ==== Properties ====
	
	final HashSet<Gender> genders;
	final HashSet<String> ages;
	final HashSet<Income> incomes;
	final HashSet<String> contexts;
	
	
	// ==== Constructor ====
	
	public Filter(HashSet<Gender> genders, HashSet<String> ages, HashSet<Income> incomes, HashSet<String> contexts) {
		this.genders = genders;
		this.ages = ages;
		this.incomes = incomes;
		this.contexts = contexts;
	}
}
