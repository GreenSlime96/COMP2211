package core.campaigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;

import core.tables.CostTable;
import core.tables.LogTable;
import core.users.InvalidUserException;
import core.users.User;
import gnu.trove.map.hash.TLongShortHashMap;
import util.DateProcessor;

public class Campaign {

	// ==== Constants ====

	// file name references
	private static final String IMPRESSIONS_FILE = "impression_log.csv";
	private static final String SERVERS_FILE = "server_log.csv";
	private static final String CLICKS_FILE = "click_log.csv";

	// ==== Properties ====

	private final File campaignDirectory;

	private LocalDateTime campaignStartDate;
	private LocalDateTime campaignEndDate;

	private CostTable impressionsTable;
	private CostTable clicksTable;
	private LogTable serversTable;

	private int numberOfConversions;
	private int numberOfPagesViewed;
	private int numberOfUniques;
	private int numberOfBounces;

	private double costOfImpressions;
	private double costOfClicks;
	
//	https://www.dropbox.com/s/otrfjuqzzw923yu/2_month_campaign.zip?dl=0

	// ==== Constructor ====

	public Campaign(File campaignDirectory) {
		this.campaignDirectory = campaignDirectory;
	}
	
	public final void loadData() throws InvalidCampaignException {
		try {
			System.out.println("--------------------------------------");

			System.gc();

			long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			long totalTime = 0;
			
			// usersMap = HashLongIntMaps.newUpdatableMap((int) (5000000 / .75));
			TLongShortHashMap usersMap = new TLongShortHashMap((int) (25000 / .75));
			
			long time = System.currentTimeMillis();
			processServers(usersMap);
			long end = System.currentTimeMillis();
			
			totalTime += end - time;
			
			System.out.println("server_log:\t" + (end - time) + "ms");
			
			time = System.currentTimeMillis();
			processImpressions(usersMap);
			end = System.currentTimeMillis();
			
			totalTime += end - time;
			
			System.out.println("impression_log:\t" + (end - time) + "ms");
			
			time = System.currentTimeMillis();
			processClicks(usersMap);
			end = System.currentTimeMillis();
			
			totalTime += end - time;
			
			System.out.println("click_log:\t" + (end - time) + "ms");
			
			time = System.currentTimeMillis();
			updateServers(usersMap);
			end = System.currentTimeMillis();
			
			System.out.println("server_log:\t" + (end - time) + "ms");
			
			usersMap = null;

			System.gc();
			
			long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

			System.out.println("--------------------------------------");
			System.out.println("Campaign:\t" + campaignDirectory.getName());
			System.out.println("Load Time:\t" + totalTime + "ms");
			System.out.println("Memory Used:\t" + (endMem - startMem) / (1024 * 1024) + "MB");
			System.out.println("Processed:\t" + getNumberOfRecords() + " records");
			System.out.println("Start Date:\t" + campaignStartDate);
			System.out.println("End Date:\t" + campaignEndDate);
			System.out.println("Impressions:\t" + getNumberOfImpressions());
			System.out.println("Uniques:\t" + numberOfUniques);
			System.out.println("Cost(I):\t" + costOfImpressions);
			System.out.println("Cost(C):\t" + costOfClicks);
			System.out.println("Cost(T):\t" + (costOfImpressions + costOfClicks));
			System.out.println("Conversions:\t" + numberOfConversions);
			System.out.println("Bounces:\t" + numberOfBounces);
			System.out.println("Page Views:\t" + numberOfPagesViewed);
			System.out.println("--------------------------------------");		
		} catch (InvalidUserException e) {
			throw new InvalidCampaignException("invalid user data in impression_log");
		} catch (IOException e) {
			throw new InvalidCampaignException("something happened when reading files");
		}		
	}

	// ==== Accessors ====

	// === DataProcessor Hooks ====
	
	public final CostTable getImpressions() {
		return impressionsTable;
	}

	public final CostTable getClicks() {
		return clicksTable;
	}

	public final LogTable getServers() {
		return serversTable;
	}
	
	
	// ==== Model Hooks ====	

	public final LocalDateTime getStartDateTime() {
		return campaignStartDate;
	}

	public final LocalDateTime getEndDateTime() {
		return campaignEndDate;
	}
	
	// ==== Metric Implementations =====
	
	public final int getNumberOfImpressions() {
		return impressionsTable.size();
	}

	public final int getNumberOfClicks() {
		return clicksTable.size();
	}
	
	public final int getNumberOfUniques() {
		return numberOfUniques;
	}
	
	// TODO: bounces
	public final int getNumberOfBounces() {
		return -1;
	}

	public final int getNumberOfConversions() {
		return numberOfConversions;
	}
	
	public final double getTotalCostOfCampaign() {
		return getCostOfImpressions() + getCostOfClicks();
	}
	
	public final double getClickThroughRate() {
		return (double) getNumberOfClicks() / (double) getNumberOfImpressions();
	}
	
	public final double getCostPerAcquision() {
		return getTotalCostOfCampaign() / getNumberOfConversions();
	}
	
	public final double getCostPerClick() {
		return getTotalCostOfCampaign() / getNumberOfClicks();
	}
	
	public final double getCostPerThousandImpressions() {
		return 1000d * getTotalCostOfCampaign() / getNumberOfImpressions();
	}
	
	public final double getBounceRate() {
		return getNumberOfBounces() / getNumberOfClicks();
	}
	
	// ==== Miscellaneous Metrics ====

	public final int getNumberOfPagesViewed() {
		return numberOfPagesViewed;
	}

	public final int getNumberOfRecords() {
		return getNumberOfImpressions() + 2 * getNumberOfClicks();
	}

	public final double getCostOfImpressions() {
		return costOfImpressions;
	}

	public final double getCostOfClicks() {
		return costOfClicks;
	}
	

	// ==== Private Helper Methods ====

	/**
	 * @throws IOException 
	 * 
	 */
	private void processServers(TLongShortHashMap usersMap) throws IOException {
		serversTable = new LogTable(20000);
		
		BufferedReader br = new BufferedReader(new FileReader(new File(campaignDirectory, SERVERS_FILE)));
		// Initialise variables
		String line = br.readLine();

		// Reset variables
		numberOfPagesViewed = 0;
		numberOfConversions = 0;
		numberOfBounces = 0;

		while ((line = br.readLine()) != null) {
			final String[] data = line.split(",");

			final int dateTime = DateProcessor.toEpochSeconds(data[0]);
			final long userID = Long.parseLong(data[1]);
			final short userData = -1;
			final int exitDateTime = DateProcessor.toEpochSeconds(data[2]);
			final byte pagesViewed = Byte.parseByte(data[3]);
			final boolean conversion = data[4].equals("Yes");
			
			// say to them that this user exists
			usersMap.put(userID, userData);

			// update page views
			numberOfPagesViewed += pagesViewed;

			// increment conversions if Yes
			if (conversion) {
				numberOfConversions++;
			} else if (pagesViewed <= 1 && exitDateTime != DateProcessor.DATE_NULL && exitDateTime - dateTime <= 30) {
				numberOfBounces++;
			}

			// add to memory
			serversTable.add(dateTime, userID, userData, exitDateTime, pagesViewed, conversion);
		}

		// Close the BufferedReader
		br.close();

		// Trim to size
		serversTable.trimToSize();
	}

	/**
	 * This method processes the click log It checks that User IDs in the clicks
	 * file matches with the User IDs in the impressions Computes the total cost
	 * of clicks in this campaign
	 * @throws IOException 
	 * @throws InvalidCampaignException 
	 */
	private void processClicks(TLongShortHashMap usersMap) throws IOException, InvalidCampaignException {
		clicksTable = new CostTable(serversTable.size());
		
		BufferedReader br = new BufferedReader(new FileReader(new File(campaignDirectory, CLICKS_FILE)));
		
		// Initialise variables
		String line = br.readLine();

		// Reset counters etc
		costOfClicks = 0;

		while ((line = br.readLine()) != null) {
			final String[] data = line.split(",");

			final int dateTime = DateProcessor.toEpochSeconds(data[0]);
			final long userID = Long.parseLong(data[1]);
			final short userData = usersMap.get(userID);
			final double cost = Double.parseDouble(data[2]);

			// increment these values
			costOfClicks += cost;

			// add to memory
			clicksTable.add(dateTime, userID, userData, cost);
		}
		
		// Close the BufferedReader
		br.close();
		
		if (clicksTable.size() != serversTable.size()) {
			System.out.println(clicksTable.size() + "\t" + serversTable.size());
			throw new InvalidCampaignException("not 1-1 mapping of server to click");
		}

		// Trim list to save memory
		clicksTable.trimToSize();
	}

	/**
	 * @throws InvalidCampaignException 
	 * This method is called when to process the impressions log file It checks
	 * that the impressions file is valid and counts the number of impressions
	 * It is also responsible for the handling of unique users in the campaign
	 * We compute the cost of impressions here too, as well as start and end
	 * dates
	 * @throws IOException 
	 * @throws InvalidUserException 
	 * @throws  
	 */
	private void processImpressions(TLongShortHashMap usersMap) throws IOException, InvalidUserException, InvalidCampaignException {
		final FileInputStream fis = new FileInputStream(new File(campaignDirectory, IMPRESSIONS_FILE));			
		final FileChannel fc = fis.getChannel();
		final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		
		final int expectedRecords = (int) fc.size() / 70;
		
		impressionsTable = new CostTable(expectedRecords);
		
		// close this stream
		fis.close();

		// reset
		costOfImpressions = 0;

		long time = System.currentTimeMillis();
//		mbb.load();
		System.out.println("loading:\t" + (System.currentTimeMillis() - time) + "ms");

		time = System.currentTimeMillis();

		// skip the header -- precomputed
		mbb.position(50);
		
		while (mbb.hasRemaining()) {
			byte temp;

			/*
			 * BEGIN DATE PROCESSING SECTION
			 */

			int dateTime = DateProcessor.toEpochSeconds(mbb);

			/*
			 * END DATE PROCESSING SECTION
			 */

			/*
			 * BEGIN USERID PROCESSING SECTION
			 */

			// we know MIN(id).length = 12
			// skip first multiplication by 0
			long userID = mbb.get() & 0xF;

			while ((temp = mbb.get()) != ',') {				
				userID *= 10;
				userID += temp & 0xF;
			}

			/*
			 * END USERID PROCESSING SECTION
			 */

			/*
			 * BEGIN USERDATA PROCESSING SECTION
			 */

			short userData = User.encodeUser(mbb);
			
			if (usersMap.get(userID) == -1)
				usersMap.put(userID, userData);
			
			/*
			 * END USERDATA PROCESSING SECTION
			 */

			/*
			 * BEGIN COST PROCESSING SECTION
			 */

			int costTemp = mbb.get() & 0xF;

			while ((temp = mbb.get()) != '.') {
				costTemp *= 10;
				costTemp += temp & 0xF;
			}

			costTemp *= 10;
			costTemp += mbb.get() & 0xF;

			costTemp *= 10;
			costTemp += mbb.get() & 0xF;

			costTemp *= 10;
			costTemp += mbb.get() & 0xF;

			costTemp *= 10;
			costTemp += mbb.get() & 0xF;

			costTemp *= 10;
			costTemp += mbb.get() & 0xF;

			costTemp *= 10;
			costTemp += mbb.get() & 0xF;

			double cost = costTemp * 0.000001;

			/*
			 * END COST PROCESSING SECTION
			 */
			
			if (mbb.get() != '\n')
				throw new InvalidUserException("invalid entry: " + impressionsTable.size());
			
			impressionsTable.add(dateTime, userID, userData, cost);
			
			// misc increment
			costOfImpressions += cost;
		}

		System.out.println("processing:\t" + (System.currentTimeMillis() - time) + "ms");

		// trim the ArrayList to save capacity
		impressionsTable.trimToSize();

		// compute size of impressions
		numberOfUniques = usersMap.size();

		// compute dates
		campaignStartDate = DateProcessor.toLocalDateTime(impressionsTable.getDateTime(0));
		campaignEndDate = DateProcessor.toLocalDateTime(impressionsTable.getDateTime(impressionsTable.size() - 1));
	}
	
	/**
	 * post-processes the server logs and updates unfilled userDatas
	 * 
	 * @param usersMap
	 */
	private void updateServers(TLongShortHashMap usersMap) {
		for (int i = 0; i < serversTable.size(); i++) {
			serversTable.setUserData(i, usersMap.get(serversTable.getUserID(i)));
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
	public final String toString() {
		return campaignDirectory.getName();
	}

}