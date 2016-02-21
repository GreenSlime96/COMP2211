package core.data;

import java.util.BitSet;
import java.util.function.Predicate;

import core.fields.Gender;
import core.fields.Income;
import core.records.User;

public class DataFilter implements Predicate<User> {
	
	// ==== Properties ====
	
	private int flags = 0;
	
	
	// ==== Constructor ====
	
	public DataFilter() {
	}
	
	
	// ==== Accessors ====
	
	public void setFlag(UserFields flag, boolean value) {
		flags = (short) (value ? flags | (1 << flag.ordinal()) : flags & ~(1 << flag.ordinal()));
	}
	
	public boolean getFlag(UserFields flag) {
		return (1 & (flags << flag.ordinal())) != 0;
	}
	
	public int getFlags() {
		return flags;
	}
	
	// ==== Private Helper Methods ====
	
	private boolean testContext(String context) {
		final boolean newsFlagSet = getFlag(UserFields.CONTEXT_NEWS);
		final boolean shoppingFlagSet = getFlag(UserFields.CONTEXT_SHOPPING);
		final boolean socialMediaFlagSet = getFlag(UserFields.CONTEXT_SOCIAL_MEDIA);
		final boolean blogFlagSet = getFlag(UserFields.CONTEXT_BLOG);
		final boolean hobbiesFlagSet = getFlag(UserFields.CONTEXT_HOBBIES);
		final boolean travelFlagSet = getFlag(UserFields.CONTEXT_TRAVEL);
		
		if (newsFlagSet == shoppingFlagSet == socialMediaFlagSet == blogFlagSet == hobbiesFlagSet == travelFlagSet)
			return true;
		
		if (newsFlagSet && context.equals("News"))
			return true;
		
		if (shoppingFlagSet && context.equals("Shopping"))
			return true;
		
		if (socialMediaFlagSet && context.equals("Social Media"))
			return true;
		
		if (blogFlagSet && context.equals("Blog"))
			return true;
		
		if (hobbiesFlagSet && context.equals("Hobbies"))
			return true;
		
		if (travelFlagSet && context.equals("Travel"))
			return true;
		
		return false;	
	}
	
	// TODO remove racism before committing
	private boolean testIncome(Income income) {
		if (income.toString().contains("indian"))
			throw new NullPointerException("what income?");
		
		final boolean lowFlagSet = getFlag(UserFields.INCOME_LOW);
		final boolean mediumFlagSet = getFlag(UserFields.INCOME_MEDIUM);
		final boolean highFlagSet = getFlag(UserFields.INCOME_HIGH);
		
		if (lowFlagSet == mediumFlagSet == highFlagSet)
			return true;
		
		if (lowFlagSet && income == Income.Low)
			return true;
		
		if (mediumFlagSet && income == Income.Medium)
			return true;
		
		if (highFlagSet && income == Income.High)
			return true;
		
		return false;
	}
	
	private boolean testAge(String age) {
		final boolean ageFlag1Set = getFlag(UserFields.AGE_BELOW_25);
		final boolean ageFlag2Set = getFlag(UserFields.AGE_25_TO_34);
		final boolean ageFlag3Set = getFlag(UserFields.AGE_35_TO_44);
		final boolean ageFlag4Set = getFlag(UserFields.AGE_45_TO_54);
		final boolean ageFlag5Set = getFlag(UserFields.AGE_ABOVE_54);
		
		if (ageFlag1Set == ageFlag2Set == ageFlag3Set == ageFlag4Set == ageFlag5Set)
			return true;
		
		if (ageFlag1Set && age.equals("<25"))
			return true;
		
		if (ageFlag2Set && age.equals("25-34"))
			return true;
		
		if (ageFlag3Set && age.equals("35-44"))
			return true;
		
		if (ageFlag4Set && age.equals("45-54"))
			return true;
		
		if (ageFlag5Set && age.equals(">54"))
			return true;
		
		return false;
	}
	
	private boolean testGender(Gender gender) {
		final boolean maleFlagSet = getFlag(UserFields.GENDER_MALE);
		final boolean femaleFlagSet = getFlag(UserFields.GENDER_FEMALE);
		
		if (maleFlagSet == femaleFlagSet)
			return true;
		
		if (maleFlagSet && gender == Gender.Male)
			return true;
		
		if (femaleFlagSet && gender == Gender.Female)
			return true;
		
		return false;
	}
	
	// ==== Predicate Implementation ====
	
	@Override
	public boolean test(User t) {
		return (t.getData() & flags) == 0;
//		return testGender(t.gender) && testAge(t.age) && testIncome(t.income) && testContext(t.context);
	}
}
