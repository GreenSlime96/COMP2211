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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.campaigns.readers.ClickReader;
import core.campaigns.readers.ImpressionReader;
import core.campaigns.readers.ServerReader;
import core.data.UserFields;
import core.fields.Gender;
import core.fields.Income;
import core.records.Click;
import core.records.Impression;
import core.records.Server;
import core.records.User;
import gnu.trove.map.TLongIntMap;
import gnu.trove.map.hash.TLongIntHashMap;
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

	// map users via their userID to their data, 500,000 is a safe bet
	private TLongIntMap usersMap = new TLongIntHashMap(5000000);

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
		
		System.out.println("--------------------------------------");

		System.gc();

		long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long totalTime = 0;
		
		long time = System.currentTimeMillis();
		processImpressions();
		long end = System.currentTimeMillis();
		totalTime += end - time;
		System.out.println("impression_log:\t" + (end - time) + "ms");
		time = System.currentTimeMillis();
		processClicks();
		end = System.currentTimeMillis();
		totalTime += end - time;
		System.out.println("click_log:\t" + (end - time) + "ms");
		processServers();
		end = System.currentTimeMillis();
		totalTime += end - time;
		System.out.println("server_log:\t" + (end - time) + "ms");

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
		System.out.println("Uniques:\t" + usersMap.size());
		System.out.println("Cost(I):\t" + costOfImpressions);
		System.out.println("Cost(C):\t" + costOfClicks);
		System.out.println("Cost(T):\t" + (costOfImpressions + costOfClicks));
		System.out.println("Conversions:\t" + numberOfConversions);
		System.out.println("Page Views:\t" + numberOfPagesViewed);
		System.out.println("--------------------------------------");
		
		
		time = System.currentTimeMillis();
		double tcost = 0;
		final LocalDateTime endDate = LocalDateTime.of(2015, 02, 01, 12, 0, 0);
		int mask = UserFields.GENDER_MALE.mask | UserFields.INCOME_HIGH.mask | UserFields.CONTEXT_TRAVEL.mask;
		
		System.out.println(impressionsList.parallelStream().filter(x -> (usersMap.get(x.getUserID()) & mask) == mask).mapToDouble(x -> x.getCost()).sum());
		
		System.out.println(System.currentTimeMillis() - time);

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

	public final int getUserFromID(long id) {
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
//		final IllegalArgumentException invalid = new IllegalArgumentException("invalid impression_log " + lineNumber + " " + colNumber);
		final ArrayList<Impression> impressionsList = new ArrayList<Impression>(500000);

		try (FileInputStream fis = new FileInputStream(impressionLog)) {
			FileChannel fc = fis.getChannel();

			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			final byte newLine = '\n';
			final byte comma = ',';

			// finish processing header line
			while (mbb.get() != newLine) {
			}
			
			// reset
			costOfImpressions = 0;
			usersMap.clear();

			while (mbb.hasRemaining()) {				
				int index = mbb.position();

				long dateTime = 567174281526586368L;
				long userID = 0;
				int userData = 0;
				double cost = 0;

				// process the date
				final char[] data = new char[19];

				for (int i = 0; i < 19; i++) {
					data[i] = (char) mbb.get();
				}

				dateTime = DateProcessor.charArrayToLong(data);

				// buffer
				if (mbb.get() != comma)
					throw new IllegalArgumentException("invalid impression_log " + index);

				// process userID
				for (;;) {
					final char c = (char) mbb.get();

					if (c == comma)
						break;

					final int digit = Character.digit(c, 10);

					if (digit < 0)
						throw new NumberFormatException("not valid userID");

					userID *= 10;
					userID += c & digit;
				}
				
				if (!usersMap.containsKey(userID)) {
					// process gender
					index = mbb.position();

					switch (mbb.get()) {
					case 'M':
						userData |= UserFields.GENDER_MALE.mask;
						mbb.position(index + 4);
						break;
					case 'F':
						userData |= UserFields.GENDER_FEMALE.mask;
						mbb.position(index + 6);
						break;
					default:
						throw new IllegalArgumentException("invalid gender " + index);
					}

					if (mbb.get() != comma)
						throw new IllegalArgumentException("invalid impression_log " + index);

					// process age
					index = mbb.position();

					switch (mbb.get()) {
					case '<':
						userData |= UserFields.AGE_BELOW_25.mask;
						mbb.position(index + 3);
						break;
					case '2':
						userData |= UserFields.AGE_25_TO_34.mask;
						mbb.position(index + 5);
						break;
					case '3':
						userData |= UserFields.AGE_35_TO_44.mask;
						mbb.position(index + 5);
						break;
					case '4':
						userData |= UserFields.AGE_45_TO_54.mask;
						mbb.position(index + 5);
						break;
					case '>':
						userData |= UserFields.AGE_ABOVE_54.mask;
						mbb.position(index + 3);
						break;
					default:
						throw new IllegalArgumentException("invalid age " + index);
					}

					if (mbb.get() != comma)
						throw new IllegalArgumentException("invalid impression_log " + index);

					// process income
					index = mbb.position();

					switch (mbb.get()) {
					case 'L':
						userData |= UserFields.INCOME_LOW.mask;
						mbb.position(index + 3);
						break;
					case 'M':
						userData |= UserFields.INCOME_MEDIUM.mask;
						mbb.position(index + 6);
						break;
					case 'H':
						userData |= UserFields.INCOME_HIGH.mask;
						mbb.position(index + 4);
						break;
					default:
						throw new IllegalArgumentException("invalid income " + index);
					}

					if (mbb.get() != comma)
						throw new IllegalArgumentException("invalid impression_log " + index);

					// process context
					index = mbb.position();

					switch (mbb.get()) {
					case 'N':
						userData |= UserFields.CONTEXT_NEWS.mask;
						mbb.position(index + 4);
						break;
					case 'S':
						switch (mbb.get()) {
						case 'h':
							userData |= UserFields.CONTEXT_SHOPPING.mask;
							mbb.position(index + 8);
							break;
						case 'o':
							userData |= UserFields.CONTEXT_SOCIAL_MEDIA.mask;
							mbb.position(index + 12);
							break;
						default:
							throw new IllegalArgumentException("invalid context (S) " + index);
						}
						break;
					case 'B':
						userData |= UserFields.CONTEXT_BLOG.mask;
						mbb.position(index + 4);
						break;
					case 'H':
						userData |= UserFields.CONTEXT_HOBBIES.mask;
						mbb.position(index + 7);
						break;
					case 'T':
						userData |= UserFields.CONTEXT_TRAVEL.mask;
						mbb.position(index + 6);
						break;
					default:
						throw new IllegalArgumentException("invalid context " + index);
					}

					if (mbb.get() != comma)
						throw new IllegalArgumentException("invalid impression_log " + index);
					
					usersMap.put(userID, userData);
				} else {
					// skip by 4 commas
					for (int i = 0; i < 4;) {
						if (mbb.get() == comma)
							i++;
					}
				}
				
				// process cost				
				char[] ch = new char[10];
				for (int i = 0;; i++) {
					final char c = (char) mbb.get();
					
					if (c == newLine) {
						// TODO: optimise via multiply/adding then division
						cost = Double.parseDouble(String.valueOf(ch, 0, i));
						break;
					}
					
					if (ch.length == i)
						ch = Arrays.copyOf(ch, i * 2);						
	
					ch[i] = c;
				}
				
				// add to list
				impressionsList.add(new Impression(dateTime, userID, cost));
				
				// misc increment
				costOfImpressions += cost;
			}
			
			// trim the ArrayList to save capacity
			impressionsList.trimToSize();
			
			// transfer references
			this.impressionsList = impressionsList;
			
			// compute size of impressions
			numberOfImpressions = impressionsList.size();
			
			// compute dates
			campaignStartDate = impressionsList.get(0).getDateTime();
			campaignEndDate = impressionsList.get(numberOfImpressions - 1).getDateTime();

		} catch (FileNotFoundException e) {
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