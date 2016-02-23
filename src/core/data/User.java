package core.data;

public enum User {
	GENDER_MALE, GENDER_FEMALE,
	AGE_BELOW_25, AGE_25_TO_34, AGE_35_TO_44, AGE_45_TO_54, AGE_ABOVE_54,
	INCOME_LOW, INCOME_MEDIUM, INCOME_HIGH,
	CONTEXT_NEWS, CONTEXT_SHOPPING, CONTEXT_SOCIAL_MEDIA, CONTEXT_BLOG, CONTEXT_HOBBIES, CONTEXT_TRAVEL;

	// ==== Constants ====

	public final int mask;

	
	// ==== Constructor ====

	User() {
		mask = 1 << ordinal();
	}
	

	// ==== Static Helper Methods ====

	public static int encodeUser(String user) {
		final String[] data = user.split(",");

		// TODO handle exceptions
		if (data.length != 4)
			return -1;

		int mask = 0;

		switch (data[0]) {
		case "Male":
			mask |= GENDER_MALE.mask;
			break;
		case "Female":
			mask |= GENDER_FEMALE.mask;
			break;
		default:
			return -1;
		}

		switch (data[1]) {
		case "<25":
			mask |= AGE_BELOW_25.mask;
			break;
		case "25-34":
			mask |= AGE_25_TO_34.mask;
			break;
		case "35-44":
			mask |= AGE_35_TO_44.mask;
			break;
		case "45-54":
			mask |= AGE_45_TO_54.mask;
			break;
		case ">54":
			mask |= AGE_ABOVE_54.mask;
			break;
		default:
			return -1;
		}

		switch (data[2]) {
		case "Low":
			mask |= INCOME_LOW.mask;
			break;
		case "Medium":
			mask |= INCOME_MEDIUM.mask;
			break;
		case "High":
			mask |= INCOME_HIGH.mask;
			break;
		default:
			return -1;
		}

		switch (data[3]) {
		case "News":
			mask |= CONTEXT_NEWS.mask;
			break;
		case "Shopping":
			mask |= CONTEXT_SHOPPING.mask;
			break;
		case "Social Media":
			mask |= CONTEXT_SOCIAL_MEDIA.mask;
			break;
		case "Blog":
			mask |= CONTEXT_BLOG.mask;
			break;
		case "Hobbies":
			mask |= CONTEXT_HOBBIES.mask;
			break;
		case "Travel":
			mask |= CONTEXT_TRAVEL.mask;
			break;
		default:
			return -1;
		}

		return mask;
	}
}
