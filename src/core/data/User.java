package core.data;

import java.nio.MappedByteBuffer;

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
	
	public static int encodeUser(MappedByteBuffer mbb) throws InvalidUserException {
		byte temp;
		int index = mbb.position();
		int userData = 0;
		
		if ((temp = mbb.get()) == 'F') {
			userData |= User.GENDER_FEMALE.mask;
			mbb.position(index += 7);
		} else if (temp == 'M') {
			userData |= User.GENDER_MALE.mask;
			mbb.position(index += 5);
		} else {
			throw new InvalidUserException("invalid gender");
		}

		if ((temp = mbb.get()) == '2') {
			userData |= User.AGE_25_TO_34.mask;
			mbb.position(index += 6);
		} else if (temp == '4') {
			userData |= User.AGE_45_TO_54.mask;
			mbb.position(index += 6);
		} else if (temp == '3') {
			userData |= User.AGE_35_TO_44.mask;
			mbb.position(index += 6);
		} else if (temp == '>') {
			userData |= User.AGE_ABOVE_54.mask;
			mbb.position(index += 4);
		} else if (temp == '<') {
			userData |= User.AGE_BELOW_25.mask;
			mbb.position(index += 4);
		} else {
			throw new InvalidUserException("invalid age");
		}

		if ((temp = mbb.get()) == 'H') {
			userData |= User.INCOME_HIGH.mask;
			mbb.position(index += 5);
		} else if (temp == 'M') {
			userData |= User.INCOME_MEDIUM.mask;
			mbb.position(index += 7);
		} else if (temp == 'L') {
			userData |= User.INCOME_LOW.mask;
			mbb.position(index += 4);
		} else {
			throw new InvalidUserException("invalid income");
		}

		if ((temp = mbb.get()) == 'N') {
			userData |= User.CONTEXT_NEWS.mask;
			mbb.position(index += 5);
		} else if (temp == 'S') {
			if ((temp = mbb.get()) == 'o') {
				userData |= User.CONTEXT_SOCIAL_MEDIA.mask;
				mbb.position(index += 13);
			} else if (temp == 'h') {
				userData |= User.CONTEXT_SHOPPING.mask;
				mbb.position(index += 9);
			} else {
				throw new InvalidUserException("invalid context");
			}
		} else if (temp == 'B') {
			userData |= User.CONTEXT_BLOG.mask;
			mbb.position(index += 5);
		} else if (temp == 'T') {
			userData |= User.CONTEXT_TRAVEL.mask;
			mbb.position(index += 7);
		} else if (temp == 'H') {
			userData |= User.CONTEXT_HOBBIES.mask;
			mbb.position(index += 8);
		} else {
			throw new InvalidUserException("invalid context");
		}
		
		return userData;
	}

	public static int encodeUser(String user) throws InvalidUserException {
		final String[] data = user.split(",");

		// TODO handle exceptions
		if (data.length != 4)
			throw new InvalidUserException("wrong number of parameters");

		int mask = 0;

		switch (data[0]) {
		case "Male":
			mask |= GENDER_MALE.mask;
			break;
		case "Female":
			mask |= GENDER_FEMALE.mask;
			break;
		default:
			throw new InvalidUserException("invalid gender");
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
			throw new InvalidUserException("invalid age");
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
			throw new InvalidUserException("invalid income");
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
			throw new InvalidUserException("invalid context");
		}

		return mask;
	}
}
