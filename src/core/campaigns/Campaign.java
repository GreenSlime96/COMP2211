package core.campaigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import core.fields.Age;
import core.fields.Context;
import core.fields.Gender;
import core.fields.Income;
import core.records.User;

public class Campaign {
	
	// ==== Constants ====
	
	private static final String IMPRESSIONS_FILE = "impression_log.csv";
	private static final String SERVERS_FILE = "server_log.csv";
	private static final String CLICKS_FILE = "click_log.csv";
	
	
	// ==== Properties ====
	
	private final File campaignDirectory;
	
	private final File impressionLog;
	private final File serverLog;
	private final File clickLog;
	
	private final Map<Long, User> usersMap = new HashMap<Long, User>();
	
	private int numberOfImpressions;
	private int numberOfServers;
	private int numberOfClicks;
	
	
	// ==== Constructor ====
	
	public Campaign(File campaignDirectory) {
		if (!campaignDirectory.isDirectory())
			throw new IllegalArgumentException(campaignDirectory + " is not a directory!");
		
		impressionLog = new File(campaignDirectory, IMPRESSIONS_FILE);
		serverLog = new File(campaignDirectory, SERVERS_FILE);
		clickLog = new File(campaignDirectory, CLICKS_FILE);
		
		if (!impressionLog.exists() || !serverLog.exists() || !clickLog.exists())
			throw new IllegalArgumentException(campaignDirectory + " is not a valid campaign directory!");
				
		this.campaignDirectory = campaignDirectory;
		
		
		
		buildUserMap();
	}
	
	
	// ==== Accessors ====
	
	
	
	// ==== Private Helper Methods ====

	private void buildUserMap() {
		BufferedReader br = null;
		
		try {
			String line = null;
			br = new BufferedReader(new FileReader(impressionLog));
			
			line = br.readLine();
			
			if (line.split(",").length != 6)
				throw new IllegalArgumentException(impressionLog + " is not a valid impressions file!");
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				
				if (data.length != 6)
					throw new IllegalArgumentException(impressionLog + " is not a valid impressions file!");
				
				final long id = Long.parseLong(data[1]);
				
				if (!usersMap.containsKey(id)) {
					final Gender gender = Gender.valueOf(data[2]);
					final Age age = Age.valueOf(data[3]);
					final Income income = Income.valueOf(data[4]);
					final Context context = Context.valueOf(data[5].replace(" ", ""));
					
					final User user = new User(id, gender, age, income, context);
					
					usersMap.put(id, user);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	// ==== Object Override ====
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campaignDirectory == null) ? 0 : campaignDirectory.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Campaign)) {
			return false;
		}
		Campaign other = (Campaign) obj;
		if (campaignDirectory == null) {
			if (other.campaignDirectory != null) {
				return false;
			}
		} else if (!campaignDirectory.equals(other.campaignDirectory)) {
			return false;
		}
		return true;
	}

}
