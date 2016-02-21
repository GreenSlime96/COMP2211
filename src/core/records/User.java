package core.records;

import core.data.UserFields;
import core.fields.Gender;
import core.fields.Income;

public class User {

	// ==== Properties ====

//	private final long id;
	private final int data;

	
	// ==== Constructor ====

	public User(long id, String gender, String age, String income, String context) {
//		this.id = id;

		int mask = 0;

		switch (gender) {
		case "Male":
			mask |= UserFields.GENDER_MALE.mask;
			break;
		case "Female":
			mask |= UserFields.GENDER_FEMALE.mask;
			break;
		default:
			throw new IllegalArgumentException("unrecognised gender");
		}

		switch (age) {
		case "<25":
			mask |= UserFields.AGE_BELOW_25.mask;
			break;
		case "25-34":
			mask |= UserFields.AGE_25_TO_34.mask;
			break;
		case "35-44":
			mask |= UserFields.AGE_35_TO_44.mask;
			break;
		case "45-54":
			mask |= UserFields.AGE_45_TO_54.mask;
			break;
		case ">54":
			mask |= UserFields.AGE_ABOVE_54.mask;
			break;
		default:
			throw new IllegalArgumentException("unrecognised age");
		}

		switch (income) {
		case "Low":
			mask |= UserFields.INCOME_LOW.mask;
			break;
		case "Medium":
			mask |= UserFields.INCOME_MEDIUM.mask;
			break;
		case "High":
			mask |= UserFields.INCOME_HIGH.mask;
			break;
		default:
			throw new IllegalArgumentException("unrecognised income");
		}

		switch (context) {
		case "News":
			mask |= UserFields.CONTEXT_NEWS.mask;
			break;
		case "Shopping":
			mask |= UserFields.CONTEXT_SHOPPING.mask;
			break;
		case "Social Media":
			mask |= UserFields.CONTEXT_SOCIAL_MEDIA.mask;
			break;
		case "Blog":
			mask |= UserFields.CONTEXT_BLOG.mask;
			break;
		case "Hobbies":
			mask |= UserFields.CONTEXT_HOBBIES.mask;
			break;
		case "Travel":
			mask |= UserFields.CONTEXT_TRAVEL.mask;
			break;
		default:
			throw new IllegalArgumentException("unrecognised context");
		}

		data = mask;
	}
	
	
	// ==== Accessors ====
	
	public long getID() {
		return 0;
	}
	
	public int getData() {
		return data;
	}
	
	
	// ==== Static Methods ====
	
	public static final int of(String gender, String age, String income, String context) {
		return new User(0, gender, age, income, context).data;
	}
}
