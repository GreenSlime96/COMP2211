package core;

import java.util.Date;

public class Impression {
	
	// ==== Properties ====
	
	// TODO Use Enums instead of String
	public final Date date;
	public final long userID;
	public final String gender;
	public final String ageGroup;
	public final String income;
	public final String context;
	public final double cost;
	
	
	// ==== Constructor ====
	
	public Impression(Date date, long userID, String gender, String ageGroup, String income, String context, double cost) {
		this.date = date;
		this.userID = userID;
		this.gender = gender;
		this.ageGroup = ageGroup;
		this.income = income;
		this.context = context;
		this.cost = cost;
	}
}
