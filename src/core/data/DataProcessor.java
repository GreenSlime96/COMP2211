package core.data;

import java.time.LocalDateTime;
import java.util.List;

import core.campaigns.Campaign;

// TODO should we rename this to DataProcessor instead?
// Chart sounds like something the view should be handling
public class DataProcessor {
	
	// ==== Properties ====
	
	// the campaign we are working on for this chart
	private Campaign campaign;
	
	// the start and end dates of this dataprocessor
	private LocalDateTime dataStartDate;
	private LocalDateTime dataEndDate;
	
	// the time granularity of this dataprocessor
	
	// the list of filters to work with
	private List<DataFilter> filters;
	
	
	
	
	// ==== Constructor ====
	
	public DataProcessor(Campaign campaign) {
		this.campaign = campaign;
	}
	

	// ==== Accessors ====
	
	public final Campaign getCampaign() {
		return campaign;
	}
	
	public final void setCampaign(Campaign campaign) {
		final LocalDateTime campaignStartDate = campaign.getStartDate();
		final LocalDateTime campaignEndDate = campaign.getEndDate();
		
		if (dataStartDate == null || dataStartDate.isBefore(campaignStartDate))
			dataStartDate = campaignStartDate;
		
		if (dataEndDate == null || dataEndDate.isAfter(campaignEndDate))
			dataEndDate = campaignEndDate;
		
		this.campaign = campaign;
	}
	
	
	public final LocalDateTime getDataStartDate() {
		return dataStartDate;
	}
	
	public final void setDataStartDate(LocalDateTime dataStartDate) {
		// check if the input start date happens before the campaign start date
		if (dataStartDate.isBefore(campaign.getStartDate()))
			throw new IllegalArgumentException("cannot set data start date before campaign starts");
		
		// update the values if is valid
		this.dataStartDate = dataStartDate;
	}
	
	
	public final LocalDateTime getDataEndDate() {
		return dataEndDate;
	}
	
	public final void setDataEndDAte(LocalDateTime dataEndDate) {
		// check input is not after campaign end date
		if (dataEndDate.isAfter(campaign.getEndDate()))
			throw new IllegalArgumentException("cannot set data end date to after campaign ends");
		
		// update if valid
		this.dataEndDate = dataEndDate;
	}
	
	
	// TODO Compute Campaign metrics here
	

}
