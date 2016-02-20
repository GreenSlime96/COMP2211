package core.records;

import core.data.DataFilterFlags;
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
			mask |= 1 << DataFilterFlags.GENDER_MALE.ordinal();
			break;
		case "Female":
			mask |= 1 << DataFilterFlags.GENDER_FEMALE.ordinal();
			break;
		default:
			throw new IllegalArgumentException("unrecognised gender");
		}

		switch (age) {
		case "<25":
			mask |= 1 << DataFilterFlags.AGE_BELOW_25.ordinal();
			break;
		case "25-34":
			mask |= 1 << DataFilterFlags.AGE_25_TO_34.ordinal();
			break;
		case "35-44":
			mask |= 1 << DataFilterFlags.AGE_35_TO_44.ordinal();
			break;
		case "45-54":
			mask |= 1 << DataFilterFlags.AGE_45_TO_54.ordinal();
			break;
		case ">54":
			mask |= 1 << DataFilterFlags.AGE_ABOVE_54.ordinal();
			break;
		default:
			throw new IllegalArgumentException("unrecognised age");
		}

		switch (income) {
		case "Low":
			mask |= 1 << DataFilterFlags.INCOME_LOW.ordinal();
			break;
		case "Medium":
			mask |= 1 << DataFilterFlags.INCOME_MEDIUM.ordinal();
			break;
		case "High":
			mask |= 1 << DataFilterFlags.INCOME_HIGH.ordinal();
			break;
		default:
			throw new IllegalArgumentException("unrecognised income");
		}

		switch (context) {
		case "News":
			mask |= 1 << DataFilterFlags.CONTEXT_NEWS.ordinal();
			break;
		case "Shopping":
			mask |= 1 << DataFilterFlags.CONTEXT_SHOPPING.ordinal();
			break;
		case "Social Media":
			mask |= 1 << DataFilterFlags.CONTEXT_SOCIAL_MEDIA.ordinal();
			break;
		case "Blog":
			mask |= 1 << DataFilterFlags.CONTEXT_BLOG.ordinal();
			break;
		case "Hobbies":
			mask |= 1 << DataFilterFlags.CONTEXT_HOBBIES.ordinal();
			break;
		case "Travel":
			mask |= 1 << DataFilterFlags.CONTEXT_TRAVEL.ordinal();
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
