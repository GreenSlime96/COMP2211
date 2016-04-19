package core.campaigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import core.data.DataFilter;
import core.tables.ClicksTable;
import core.tables.ImpressionsTable;
import core.tables.LogTable;
import core.users.InvalidUserException;
import core.users.User;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TLongHashSet;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import util.DateProcessor;

public class Campaign {
	
	// ==== JavaFX MVC Stuff ====
	
	public final DoubleProperty progress = new SimpleDoubleProperty(0);

	// ==== Constants ====

	// threading
	private static final int threads = Runtime.getRuntime().availableProcessors();
	
	// file name references
	private static final String IMPRESSIONS_FILE = "impression_log.csv";
	private static final String SERVERS_FILE = "server_log.csv";
	private static final String CLICKS_FILE = "click_log.csv";

	// ==== Properties ====

	private final File campaignDirectory;

	private LocalDateTime campaignStartDate;
	private LocalDateTime campaignEndDate;

	private ImpressionsTable impressionsTable;
	private ClicksTable clicksTable;
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
			
			long time = System.currentTimeMillis();
			processServers();
			long end = System.currentTimeMillis();
			
			totalTime += end - time;
			
			System.out.println("server_log:\t" + (end - time) + "ms");
			
			time = System.currentTimeMillis();
			processClicks();
			end = System.currentTimeMillis();
			
			totalTime += end - time;
			
			System.out.println("click_log:\t" + (end - time) + "ms");
			
			time = System.currentTimeMillis();
			processImpressions();
			end = System.currentTimeMillis();
			
			totalTime += end - time;
			
			System.out.println("impression_log:\t" + (end - time) + "ms");
			
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
			throw new InvalidCampaignException("Invalid User Data in impression_log.csv");
		} catch (IOException e) {
			throw new InvalidCampaignException("Invalid Campaign Directory!");
		}		
	}

	// ==== Accessors ====

	// === DataProcessor Hooks ====
	
	public final ImpressionsTable getImpressions() {
		return impressionsTable;
	}

	public final ClicksTable getClicks() {
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
		return numberOfBounces;
	}

	public final int getNumberOfConversions() {
		return numberOfConversions;
	}
	
	public final double getTotalCostOfCampaign() {
		return costOfImpressions + costOfClicks;
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
		return (double) getNumberOfBounces() / getNumberOfClicks();
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
	private void processServers() throws IOException {
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
			final int exitDateTime = DateProcessor.toEpochSeconds(data[2]);
			final byte pagesViewed = Byte.parseByte(data[3]);
			final boolean conversion = data[4].equals("Yes");

			// update page views
			numberOfPagesViewed += pagesViewed;

			// increment conversions if Yes
			if (conversion) {
				numberOfConversions++;
			} else if (pagesViewed <= 1 && exitDateTime != DateProcessor.DATE_NULL && exitDateTime - dateTime <= 30) {
				numberOfBounces++;
			}

			// add to memory
			serversTable.add(dateTime, exitDateTime, pagesViewed, conversion);
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
	private void processClicks() throws IOException, InvalidCampaignException {
		final TLongHashSet usersSet = new TLongHashSet(serversTable.size());
		clicksTable = new ClicksTable(serversTable.size());
		
		BufferedReader br = new BufferedReader(new FileReader(new File(campaignDirectory, CLICKS_FILE)));
		
		// Initialise variables
		String line = br.readLine();

		// Reset counters etc
		costOfClicks = 0;

		while ((line = br.readLine()) != null) {
			final String[] data = line.split(",");

			final int dateTime = DateProcessor.toEpochSeconds(data[0]);
			final long userID = Long.parseLong(data[1]);
			final double cost = Double.parseDouble(data[2]);

			// increment these values
			costOfClicks += cost;

			// add to memory
			clicksTable.add(dateTime, userID, cost);
			usersSet.add(userID);
		}
		
		// Close the BufferedReader
		br.close();
		
		if (clicksTable.size() != serversTable.size()) {
			System.out.println(clicksTable.size() + "\t" + serversTable.size());
			throw new InvalidCampaignException("not 1-1 mapping of server to click");
		}

		// Trim list to save memory
		clicksTable.trimToSize();
		
		// Set variable
		numberOfUniques = usersSet.size();
	}
	
	private void processBytes(final byte barray[]) {
		
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
	private void processImpressions() throws IOException, InvalidUserException, InvalidCampaignException {
		final FileInputStream fis = new FileInputStream(new File(campaignDirectory, IMPRESSIONS_FILE));			
		final FileChannel fc = fis.getChannel();
		final MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		
		impressionsTable = new ImpressionsTable();
		
		// close this stream
		fis.close();

		// reset
		costOfImpressions = 0;
		
		long time = System.currentTimeMillis();

		// begin threading		
		final ExecutorService executor = Executors.newFixedThreadPool(threads);
		final List<Future<ImpressionsTable>> list = new ArrayList<Future<ImpressionsTable>>(threads);
		
		final ByteBuffer[] byteBuffers = new ByteBuffer[threads];
		final int size = byteBuffer.capacity() / threads;
		
		byteBuffer.position(50);
		byteBuffers[0] = byteBuffer.slice();
		
		for (int i = 1; i < threads; i++) {
			final ByteBuffer view = byteBuffers[i - 1];
			
			// move position
			byteBuffers[i - 1].position(size);
			
			// buffer until we get newline
			while (view.get() != '\n') {}
			
			byteBuffers[i] = view.slice();
			
			view.limit(view.position());
			view.position(0);
		}
		
		for (int i = 0; i < threads; i++) {
			final int j = i;			
			list.add(executor.submit(new Callable<ImpressionsTable>() {
				public ImpressionsTable call() throws Exception {
					final long startTime = System.currentTimeMillis();
					final ImpressionsTable table = processImpressions(byteBuffers[j]);
					final long endTime = System.currentTimeMillis();
					
					System.out.println("Thread " + j + ": " + (endTime - startTime) + "\t" + table.size());
					return table;
				}				
			}));
		}
		
		// TODO: slow
		for (Future<ImpressionsTable> future : list) {
			try {
				impressionsTable.append(future.get());
			} catch (InterruptedException e) {
				throw new InvalidCampaignException("Loading Thread Interrupted!");
			} catch (ExecutionException e) {
				e.printStackTrace();
				throw new InvalidUserException("Invalid Impressions File!");
			}
		}
		
		executor.shutdownNow();
		// end threading

		// trim the ArrayList to save capacity
		impressionsTable.trimToSize();
		
		// convert to something we understand
		costOfImpressions *= 10e-6;
		
		System.out.println("loading:\t" + (System.currentTimeMillis() - time) + "ms");
	}
	
	private ImpressionsTable processImpressions(ByteBuffer byteBuffer) throws InvalidUserException, InvalidCampaignException {
		final ImpressionsTable impressionsTable = new ImpressionsTable();
		final int numberOfClicks = clicksTable.size();
		
		long localCost = 0;
		int clicksProgress = 0;
		
		// preprocess
		int dateTime = DateProcessor.toEpochSeconds(byteBuffer);
		clicksProgress = clicksTable.indexOfDate(dateTime);
		byteBuffer.rewind();
		
//		// store the hour
//		int hour = dateTime - (dateTime % 3600) + 3600;
//		int hours = 0;
		
		// get the earliest dateTime
		int startDateTime = dateTime;
		
		// test arrays?
//		int[] count = new int[180 * 600];
//		int[] total = new int[180 * 600];
		
		while (byteBuffer.hasRemaining()) {		
			dateTime = DateProcessor.toEpochSeconds(byteBuffer);			
			long userID = ImpressionParser.parseUserID(byteBuffer);			
			short userData = User.encodeUser(byteBuffer);
			short cost = ImpressionParser.parseCost(byteBuffer);

			if (clicksProgress < numberOfClicks && clicksTable.getUserID(clicksProgress) == userID)
				clicksTable.setUserData(clicksProgress++, userData);
			
			if (byteBuffer.get() != '\n') 
				throw new InvalidCampaignException("invalid entry: " + impressionsTable.size());
			
//			if (dateTime > hour) {
//				hour += 3600;
//				hours += 180;
//			}
//			
//			int user = User.compress(userData);
//			
//			count[hours + user]++;
//			total[hours + user]+= cost;

			// add to my table
			impressionsTable.add(dateTime, userData, cost);
			
			// misc increment
			localCost += cost;
		}
		
		// set start/end date time -- this WILL cause synchronisation issues :(
		final LocalDateTime startDate = DateProcessor.toLocalDateTime(startDateTime);
		final LocalDateTime endDate = DateProcessor.toLocalDateTime(dateTime);
		
		if (campaignStartDate == null || campaignStartDate.isAfter(startDate))
			campaignStartDate = startDate;
		
		if (campaignEndDate == null || campaignEndDate.isBefore(endDate))
			campaignEndDate = endDate;
		
		costOfImpressions += localCost;
		
		return impressionsTable;
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