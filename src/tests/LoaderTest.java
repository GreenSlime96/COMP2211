package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;

public class LoaderTest {
	
	@Test
	public void loadValidCampaign() throws InvalidCampaignException {
		
		Campaign campaign = new Campaign(new File("/Users/khengboonpek/Downloads/2_month_campaign"));
		
		// try to load campaign
		campaign.loadData();
		
		assertEquals("number of impressions should be 8828248", 8828248, campaign.getNumberOfImpressions());
		assertEquals("number of clicks and server should be same", campaign.getClicks().size(), campaign.getServers().size());
	}

	@Test(expected = InvalidCampaignException.class)
	public void loadInvalidDirectoryThrowsException() throws InvalidCampaignException {
		
		// test with known invalid campaign directory
		Campaign campaign = new Campaign(new File("/"));
		
		// load invalid campaign
		campaign.loadData();
	}
	
	@Test(expected = InvalidCampaignException.class)
	public void loadBrokenImpressionsLog() throws InvalidCampaignException {
		
		// test with known broken impressions_log.csv
		Campaign campaign = new Campaign(new File("/home/khengboonpek/Desktop/tests/invaliduser"));
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {}
		
		// try to load campaign
		campaign.loadData();
	}
	
	@Test(expected = InvalidCampaignException.class)
	public void loadUnmatchedClickServer() throws InvalidCampaignException {
		
		// test with known unmapped server_log and click_log
		Campaign campaign = new Campaign(new File("/home/khengboonpek/Desktop/tests/unmatched"));
		
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {}
		
		// try to load campaign
		campaign.loadData();
	}
	
	@Test(expected = InvalidCampaignException.class)
	public void loadUnmatchedUserIDs() throws InvalidCampaignException {
		
		// test with known broken impressions_log.csv
		Campaign campaign = new Campaign(new File("/home/khengboonpek/Desktop/tests/nouseridmaps"));
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {}
		
		// try to load campaign
		campaign.loadData();
	}
	
	@Test(expected = InvalidCampaignException.class)
	public void loadInvalidCostData() throws InvalidCampaignException {
		
		// test with known broken impressions_log.csv
		Campaign campaign = new Campaign(new File("/home/khengboonpek/Desktop/tests/costBelow0"));
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {}
		
		// try to load campaign
		campaign.loadData();
	}
}
