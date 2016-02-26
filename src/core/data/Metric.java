package core.data;

public enum Metric {
	NUMBER_OF_IMPRESSIONS,
	NUMBER_OF_CLICKS,
	NUMBER_OF_UNIQUES,
	NUMBER_OF_BOUNCES,
	NUMBER_OF_CONVERSIONS,
	TOTAL_COST,
	CLICK_THROUGH_RATE,
	COST_PER_ACQUISITION,
	COST_PER_CLICK,
	COST_PER_THOUSAND_IMPRESSIONS,
	BOUNCE_RATE,
	
	GENDER_DISTRIBUTION,
	AGE_DISTRIBUTION,
	INCOME_DISTRIBUTION,
	CONTEXT_DISTRIBUTION,
	
	CLICK_COST_HISTOGRAM;
	
	
	// ==== Constants ====
	
	final String name;
	
	
	// ==== Constructor ====
	
	Metric() {
		this.name = toTitleCase();
	}
	
	
	// ==== Accessors ====

	@Override
	public String toString() {
		return name;
	}
	
	
	// ==== Private Helper Functions ====
	
	private String toTitleCase() {
		String[] arr = name().toLowerCase().split("_");
		StringBuffer sb = new StringBuffer();
		
	    for (int i = 0; i < arr.length; i++) {
	        sb.append(Character.toUpperCase(arr[i].charAt(0)))
	            .append(arr[i].substring(1)).append(" ");
	    }          
	    
	    return sb.toString().trim();
	}
	
	
	// ==== Public Static Methods ====
	
	public static Metric toMetric(String string) {
		String data = string.toUpperCase().replace(" ", "_");
		
		return valueOf(data);
	}
}
