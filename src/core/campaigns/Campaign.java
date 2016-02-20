package core.campaigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.campaigns.readers.ClickReader;
import core.campaigns.readers.ImpressionReader;
import core.campaigns.readers.ServerReader;
import core.fields.Gender;
import core.fields.Income;
import core.records.Click;
import core.records.Impression;
import core.records.Server;
import core.records.User;
import util.DateProcessor;

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

	private Map<Long, Integer> usersMap;
	
	private List<Impression> impressionsList;
	private List<Click> clicksList;
	private List<Server> serversList;

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

		/*
		 * TODO Go through Impressions Log - Compute total cost - Start date,
		 * End date - Users map, how much memory?
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
		return impressionsList;
	}

	public final Iterable<Click> getClicks() {
		return new ClickReader(clickLog);
	}

	public final Iterable<Server> getServer() {
		return new ServerReader(serverLog);
	}

	public final Integer getUserFromID(long id) {
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
		try (BufferedReader br = new BufferedReader(new FileReader(serverLog))) {
			// Check variables
			ArrayList<Server> serversList = new ArrayList<Server>();

			// Initialise variables
			String line = br.readLine();

			// Reset variables
			numberOfPagesViewed = 0;
			numberOfConversions = 0;

			while ((line = br.readLine()) != null) {
				final Server server = new Server(line);

				// update page views
				numberOfPagesViewed += server.getPagesViewed();

				// increment conversions if Yes
				if (server.getConversion())
					numberOfConversions++;
				
				// add to memory
				serversList.add(server);
			}
			
			// Trim to size
			serversList.trimToSize();
			
			// assign reference
			this.serversList = serversList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method processes the click log It checks that User IDs in the clicks
	 * file matches with the User IDs in the impressions Computes the total cost
	 * of clicks in this campaign TODO: - Check that start/end dates are in-form
	 * with Impressions
	 */
	private void processClicks() {
		try (BufferedReader br = new BufferedReader(new FileReader(clickLog))) {			
			
			// reset clicks
			ArrayList<Click> clicksList = new ArrayList<Click>(20000);

			// Initialise variables
			String line = br.readLine();

			// Reset counters etc
			numberOfClicks = 0;
			costOfClicks = 0;

			while ((line = br.readLine()) != null) {				
				final Click click = new Click(line);
				
				// increment these values
				costOfClicks += click.getCost();
				
				// add to memory
				clicksList.add(click);
			}
			
			// Trim list to save memory
			clicksList.trimToSize();
			
			// Set these variables
			numberOfClicks = clicksList.size();
			this.clicksList = clicksList;			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is called when to process the impressions log file It checks
	 * that the impressions file is valid and counts the number of impressions
	 * It is also responsible for the handling of unique users in the campaign
	 * We compute the cost of impressions here too, as well as start and end
	 * dates
	 * 
	 * TODO: - find a way to record start and end dates - make exceptions more
	 * understandable v.s. stacktrace e.g. FileNotFound, NumberFormatException
	 * (print out line number for easy debug), IOException - consider User, use
	 * of Enum v.s. String.intern()
	 */
	private void processImpressions() {
		/*
		try (FileInputStream fis = new FileInputStream(impressionLog)) {
			FileChannel fc = fis.getChannel();
			
			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			
			long time = System.nanoTime();
			
			byte b;
			while ((b = mbb.get()) != 10) {
				System.out.print((char) b);
			}
			
			int index = 0;
			
			long dateTime;
			long userID;
			
			while (mbb.hasRemaining()) {				
				switch (index) {
				case 0:
					final char[] data = new char[19];
					mbb.get(data, mbb.position(), 19);
					dateTime = DateProcessor.charArrayToLong(data)
				}
			}
			
			System.out.println(System.nanoTime() - time);

			
			int pos = 0;
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		try (BufferedReader br = new BufferedReader(new FileReader(impressionLog))) {			
			// Temporary ArrayList - prevent unnecessary resizing
			ArrayList<Impression> impressionsList = new ArrayList<Impression>(400000);

			// Buffer the first line
			String line = br.readLine();

			// Rebuild the Users Map
			usersMap = new HashMap<Long, Integer>();

			// Reset dates
			campaignStartDate = null;
			campaignEndDate = null;

			// Reset number of Impressions; we are counting it here
			numberOfImpressions = 0;
			costOfImpressions = 0;

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",");
				final Impression impression = new Impression(data);
				final long userID = impression.getUserID();

				if (!usersMap.containsKey(userID)) 
					usersMap.put(userID, User.of(data[2], data[3], data[4], data[5]));
		
				costOfImpressions += impression.getCost();
				
				impressionsList.add(impression);
			}
			
			// Resize ArrayList to reduce memory use
			impressionsList.trimToSize();
			
			// Set Start Date and End Dates
			campaignEndDate = impressionsList.get(impressionsList.size() - 1).getDateTime();
			campaignStartDate = impressionsList.get(0).getDateTime();
			
			// List Size is Number of Impressions
			numberOfImpressions = impressionsList.size();
			
			// Transfer reference of object
			this.impressionsList = impressionsList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
