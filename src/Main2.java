import java.io.File;

import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;

public class Main2 {
	public static void main(String[] args) throws InvalidCampaignException {
		final Campaign c = new Campaign(new File("/Users/khengboonpek/Downloads/2_month_campaign"));
		c.loadData();
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
		
		System.out.println(c.getBounceRate());
	}
}
