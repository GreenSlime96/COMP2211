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
import gnu.trove.map.hash.TLongIntHashMap;
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

	private int numberOfImpressions;
	private int numberOfClicks;
	private int numberOfConversions;
	private int numberOfPagesViewed;
	private int numberOfUniques;

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
			TLongIntHashMap usersMap = new TLongIntHashMap((int) (20000 / .75));
			
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
			System.out.println("Impressions:\t" + numberOfImpressions);
			System.out.println("Uniques:\t" + numberOfUniques);
			System.out.println("Cost(I):\t" + costOfImpressions);
			System.out.println("Cost(C):\t" + costOfClicks);
			System.out.println("Cost(T):\t" + (costOfImpressions + costOfClicks));
			System.out.println("Conversions:\t" + numberOfConversions);
			System.out.println("Page Views:\t" + numberOfPagesViewed);
			System.out.println("--------------------------------------");			
		} catch (InvalidUserException e) {
			throw new InvalidCampaignException("invalid user data in impression_log");
		} catch (IOException e) {
			throw new InvalidCampaignException("something happened when reading files");
		}		
	}

	// ==== Accessors ====

	public final CostTable getImpressions() {
		return impressionsTable;
	}

	public final CostTable getClicks() {
		return clicksTable;
	}

	public final LogTable getServers() {
		return serversTable;
	}

	public final LocalDateTime getStartDateTime() {
		return campaignStartDate;
	}

	public final LocalDateTime getEndDateTime() {
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
	

	// ==== Private Helper Methods ====

	/**
	 * @throws IOException 
	 * 
	 */
	private void processServers(TLongIntHashMap usersMap) throws IOException {
		final LogTable serversList = new LogTable(20000);
		
		BufferedReader br = new BufferedReader(new FileReader(new File(campaignDirectory, SERVERS_FILE)));
		// Initialise variables
		String line = br.readLine();

		// Reset variables
		numberOfPagesViewed = 0;
		numberOfConversions = 0;

		while ((line = br.readLine()) != null) {
			final String[] data = line.split(",");

			final long dateTime = DateProcessor.toEpochSeconds(data[0]);
			final long userID = Long.valueOf(data[1]);
			final int userData = -1;
			final long exitDateTime = DateProcessor.toEpochSeconds(data[2]);
			final int pagesViewed = Integer.valueOf(data[3]);
			final boolean conversion = data[4].equals("Yes");
			
			// say to them that this user exists
			usersMap.put(userID, -1);

			// update page views
			numberOfPagesViewed += pagesViewed;

			// increment conversions if Yes
			if (conversion)
				numberOfConversions++;

			// add to memory
			serversList.add(dateTime, userID, userData, exitDateTime, pagesViewed, conversion);
		}
		
		// Close the BufferedReader
		br.close();

		// Trim to size
		serversList.trimToSize();
		
		// Assign this, they should be analogous
		numberOfClicks = serversList.size();

		// assign reference
		this.serversTable = serversList;
	}

	/**
	 * This method processes the click log It checks that User IDs in the clicks
	 * file matches with the User IDs in the impressions Computes the total cost
	 * of clicks in this campaign TODO: - Check that start/end dates are in-form
	 * with Impressions
	 * @throws IOException 
	 */
	private void processClicks(TLongIntHashMap usersMap) throws IOException {
		final CostTable clicksList = new CostTable(numberOfClicks);
		
		BufferedReader br = new BufferedReader(new FileReader(new File(campaignDirectory, CLICKS_FILE)));
		
		// Initialise variables
		String line = br.readLine();

		// Reset counters etc
		costOfClicks = 0;

		while ((line = br.readLine()) != null) {
			final String[] data = line.split(",");

			final long dateTime = DateProcessor.toEpochSeconds(data[0]);
			final long userID = Long.valueOf(data[1]);
			final int userData = usersMap.get(userID);
			final double cost = Double.parseDouble(data[2]);

			// increment these values
			costOfClicks += cost;

			// add to memory
			clicksList.add(dateTime, userID, userData, cost);
		}
		
		// Close the BufferedReader
		br.close();

		// Trim list to save memory
		clicksList.trimToSize();

		// Set these variables
		this.clicksTable = clicksList;
	}

	/**
	 * This method is called when to process the impressions log file It checks
	 * that the impressions file is valid and counts the number of impressions
	 * It is also responsible for the handling of unique users in the campaign
	 * We compute the cost of impressions here too, as well as start and end
	 * dates
	 * 
	 * TODO: - find a way to record start and end dates - make exceptions more
	 * understandable v.s. stacktrace e.g. FileNotFound, NumberFormatException Chr
	 * (print out line number for easy debug), IOException - consider User, use
	 * of Enum v.s. String.intern()
	 * @throws IOException 
	 * @throws InvalidUserException 
	 * @throws  
	 */
	private void processImpressions(TLongIntHashMap usersMap) throws IOException, InvalidUserException {
		final FileInputStream fis = new FileInputStream(new File(campaignDirectory, IMPRESSIONS_FILE));			
		final FileChannel fc = fis.getChannel();
		final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		
		final int expectedRecords = (int) fc.size() / 70;
		
		// store in ByteBuffer
		final CostTable impressionsTable = new CostTable(expectedRecords);
		
		// close this stream
		fis.close();
		
		final byte newLine = '\n';
		final byte comma = ',';

		// reset
		costOfImpressions = 0;
		numberOfImpressions = 0;

		long time = System.currentTimeMillis();
//		mbb.load();
//		System.out.println("loading:\t" + (System.currentTimeMillis() - time) + "ms");
//
//		time = System.currentTimeMillis();

		// skip the header -- precomputed
		mbb.position(50);
		// int index = 50;
		
		while (mbb.hasRemaining()) {
			byte temp;

			/*
			 * BEGIN DATE PROCESSING SECTION
			 */

			long dateTime = DateProcessor.toEpochSeconds(mbb);

			/*
			 * END DATE PROCESSING SECTION
			 */

			/*
			 * BEGIN USERID PROCESSING SECTION
			 */

			// we know MIN(id).length = 12
			// skip first multiplication by 0
			long userID = mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;
			
			userID *= 10;
			userID += mbb.get() & 0xF;

			while ((temp = mbb.get()) != comma) {
				userID *= 10;
				userID += temp & 0xF;
			}

			/*
			 * END USERID PROCESSING SECTION
			 */

			/*
			 * BEGIN USERDATA PROCESSING SECTION
			 */

			int userData = User.encodeUser(mbb);
			
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
			
			if (mbb.get() != newLine)
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
		numberOfImpressions = impressionsTable.size();
		
		// transfer reference
		this.impressionsTable = impressionsTable;

		// compute dates
		campaignStartDate = DateProcessor.toLocalDateTime(impressionsTable.getDateTime(0));
		campaignEndDate = DateProcessor.toLocalDateTime(impressionsTable.getDateTime(numberOfImpressions - 1));
	}
	
	private void updateServers(TLongIntHashMap usersMap) {
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