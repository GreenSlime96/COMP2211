package core.campaigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.campaigns.readers.ImpressionReader;
import core.fields.Gender;
import core.fields.Income;
import core.records.Click;
import core.records.Impression;
import core.records.Server;
import core.records.User;

public class Campaign {
	
	// ==== Constants ====
	
	// date format
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// file name references
	private static final String IMPRESSIONS_FILE = "impression_log.csv";
	private static final String SERVERS_FILE = "server_log.csv";
	private static final String CLICKS_FILE = "click_log.csv";	
	
	
	// ==== Properties ====
	
	private final File campaignDirectory;
	
	private final File impressionLog;
	private final File serverLog;
	private final File clickLog;
	
	private LocalDateTime campaignStartDate;
	private LocalDateTime campaignEndDate;
	
	private Map<Long, User> usersMap;
	
	private int numberOfImpressions;
	private int numberOfClicks;
	private int numberOfConversions;
	private int numberOfPagesViewed;
	
	private double costOfImpressions;
	private double costOfClicks;
	
	
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
		
		/* TODO
		 * Go through Impressions Log
		 * - Compute total cost
		 * - Start date, End date
		 * - Users map, how much memory?
		 */
		
		System.gc();
		
		long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long time = System.currentTimeMillis();
		
		processImpressions();
		processClicks();
		processServers();
		
		long end = System.currentTimeMillis();
		
		System.gc();
		long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		System.out.println("--------------------------------------");
		System.out.println("Campaign:\t" + campaignDirectory.getName());
		System.out.println("Load Time:\t" + (end - time) + "ms");
		System.out.println("Memory Used:\t" + (endMem - startMem) / (1024 * 1024) + "MB");
		System.out.println("Processed:\t" + getNumberOfRecords() + " records");
		System.out.println("Start Date:\t" + campaignStartDate);
		System.out.println("End Date:\t" + campaignEndDate);
		System.out.println("Impressions:\t" + numberOfImpressions);
		System.out.println("Uniques:\t" + usersMap.size());
		System.out.println("Cost(I):\t" + costOfImpressions);
		System.out.println("Cost(C):\t" + costOfClicks);
		System.out.println("Cost(T):\t" + (costOfImpressions + costOfClicks));
		System.out.println("Conversions:\t" + numberOfConversions);
		System.out.println("Page Views:\t" + numberOfPagesViewed);
		System.out.println("--------------------------------------");
	}
	
	
	// ==== Accessors ====
	
	public final Iterable<Impression> getImpressions() {
		try {
			return new ImpressionReader(impressionLog);
		} catch (IOException e) {
			return null;
		}
	}
	
	public final Iterable<Click> getClicks() {
		return null;
	}
	
	public final Iterable<Server> getServer() {
		return null;
	}
	
	
	public final User getUserFromID(long id) {
		return usersMap.get(id);
	}
	
	
	public final LocalDateTime getStartDate() {
		return campaignStartDate;
	}
	
	public final LocalDateTime getEndDate() {
		return campaignEndDate;
	}
	
	
	public final int getNumberOfImpressions() {
		return numberOfImpressions;
	}	

	public final int getNumberOfClicks() {
		return numberOfClicks;
	}
	
	public final int getNumberOfConversions() {
		return numberOfConversions;
	}
	
	public final int getNumberOfPagesViewed() {
		return numberOfPagesViewed;
	}
	
	public final int getNumberOfRecords() {
		return numberOfImpressions + 2 * numberOfClicks;
	}
	
	
	public final double getTotalCostOfCampaign() {
		return costOfImpressions + costOfClicks;
	}
	
	public final double getCostOfImpressions() {
		return costOfImpressions;
	}
	
	public final double getCostOfClicks() {
		return costOfClicks;
	}
	
	public final String getDirectoryPath() {
		return campaignDirectory.getName();
	}
	
	
	
	// ==== Private Helper Methods ====
	
	/**
	 * 
	 */
	private void processServers() {
		LocalDateTime firstEntryDate = null;
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(serverLog));
			
			// Check variables
			int numberOfServerEntries = 0;
			
			// Initialise variables
			String line = br.readLine();
			String data[] = line.split(",");
			
			// Reset variables
			numberOfPagesViewed = 0;
			numberOfConversions = 0;
			
			if (data.length != 5)
				throw new IllegalArgumentException(serverLog + " doesn't have the right number of columns");
			
			while ((line = br.readLine()) != null) {
				data = line.split(",");
				
				if (data.length != 5)
					throw new IllegalArgumentException(serverLog + " doesn't have the right number of columns");
				
				// TODO check if dates are valid (not before campaign start)
				// TODO check if start date and end date are valid (doesn't end before start)
				// TODO are these checks even needed!?
				if (firstEntryDate == null) {
					firstEntryDate = LocalDateTime.parse(data[0], formatter);
					
					if (firstEntryDate.isBefore(campaignStartDate))
						throw new IllegalArgumentException(clickLog + " start date of campaign is after first entry");
				}
				
				// check for user key mismatch
				final Long id = Long.valueOf(data[1]);
				
				if (!usersMap.containsKey(id))
					throw new IllegalArgumentException(serverLog + " contains an unrecognised user id on line " + numberOfServerEntries);
				
				// update page views
				numberOfPagesViewed += Integer.parseInt(data[3]);

				// increment conversions if Yes				
				if (data[4].equals("Yes"))
					numberOfConversions++;
				
				// increment number of entries
				numberOfServerEntries++;
			}
			
			if (numberOfServerEntries != numberOfClicks)
				throw new IllegalArgumentException(serverLog + " doesn't have matching entries with " + clickLog);
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
	
	/**
	 * This method processes the click log
	 * It checks that User IDs in the clicks file matches with the User IDs in the impressions
	 * Computes the total cost of clicks in this campaign
	 * TODO:
	 * - Check that start/end dates are in-form with Impressions
	 */
	private void processClicks() {
		LocalDateTime firstClickDate = null;
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(clickLog));
			
			// Initialise variables
			String line = br.readLine();
			String data[] = line.split(",");
			
			// Reset counters etc
			numberOfClicks = 0;
			costOfClicks = 0;
			
			if (data.length != 3)
				throw new IllegalArgumentException(clickLog + " doesn't have the right number of columns");
			
			while ((line = br.readLine()) != null) {
				data = line.split(",");
			
				if (data.length != 3)
					throw new IllegalArgumentException(clickLog + " malformed entry on line " + numberOfClicks);
				
				// checks if first click occurs before campaign start
				// TODO I'm not even sure if we need this
				if (firstClickDate == null) {
					firstClickDate = LocalDateTime.parse(data[0], formatter);
					
					if (firstClickDate.isBefore(campaignStartDate))
						throw new IllegalArgumentException(clickLog + " start date of campaign is after first click");
				}					
				
				// check for key mismatch
				final Long id = Long.valueOf(data[1]);
				
				if (!usersMap.containsKey(id))
					throw new IllegalArgumentException(clickLog + " contains an unrecognised user id on line " + numberOfClicks);
				
				// increment these values
				costOfClicks += Double.parseDouble(data[2]);
				numberOfClicks++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
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

	/**
	 * This method is called when to process the impressions log file
	 * It checks that the impressions file is valid and counts the number of impressions
	 * It is also responsible for the handling of unique users in the campaign
	 * We compute the cost of impressions here too, as well as start and end dates
	 * 
	 * TODO:
	 * - find a way to record start and end dates
	 * - make exceptions more understandable v.s. stacktrace
	 *   e.g. FileNotFound, NumberFormatException (print out line number for easy debug), IOException
	 * - consider User, use of Enum v.s. String.intern()
	 */
	private void processImpressions() {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(impressionLog));
			
			// Initialise variables	
			String line = br.readLine();
			String data[] = line.split(",");
			
			// Rebuild the Users Map
			usersMap = new HashMap<Long, User>();
			
			// Reset dates
			campaignStartDate = null;
			campaignEndDate = null;
			
			// Reset number of Impressions; we are counting it here
			numberOfImpressions = 0;
			costOfImpressions = 0;
			
			if (data.length != 7)
				throw new IllegalArgumentException(impressionLog + " doesn't have the right number of columns");
			
			while ((line = br.readLine()) != null) {
				data = line.split(",");
				
				if (data.length != 7)
					throw new IllegalArgumentException(impressionLog + " malformed entry on line " + numberOfImpressions);
				
				// TODO we have a more efficient method for parsing dates, consider using that instead?
				// downside to that is that formatting the date would be a bitch if the client changes date formats
				if (campaignStartDate == null)
					campaignStartDate = LocalDateTime.parse(data[0], formatter);
				
				// use valueOf as it returns a boxed value; save on autoboxing overhead and
				// is potentially cached (implementation-dependent)
				final Long id = Long.valueOf(data[1]);
				
				// we want this as a primitive this time :)
				final double cost = Double.parseDouble(data[6]);
				
				// TODO Discuss whether we use Enums or Strings
				if (!usersMap.containsKey(id)) {
					final Gender gender = Gender.valueOf(data[2]);
//					final Age age = Age.valueOf(data[3]);
					final Income income = Income.valueOf(data[4]);
//					final Context context = Context.valueOf(data[5]);
					
					final User user = new User(id, gender, data[3], income, data[5]);
					
					usersMap.put(id, user);
				}
				
				// increment the cost by the cost of this impression
				costOfImpressions += cost;
				numberOfImpressions++;
			}
			
			// Process the last read data to get final date
			campaignEndDate = LocalDateTime.parse(data[0], formatter);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
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


	@Override
	public String toString() {
		return "Campaign [campaignDirectory=" + campaignDirectory + "]";
	}

}
