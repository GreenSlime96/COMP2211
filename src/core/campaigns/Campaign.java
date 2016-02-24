package core.campaigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import core.data.User;
import core.records.Click;
import core.records.Impression;
import core.records.Server;
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
//	private HashLongIntMap usersMap = HashLongIntMaps.newUpdatableMap();  // 8062
	private TLongIntHashMap usersMap = new TLongIntHashMap(); // 6000

	private List<Impression> impressionsList;
	private List<Click> clicksList;
	private List<Server> serversList;

	private int numberOfImpressions;
	private int numberOfClicks;
	private int numberOfConversions;
	private int numberOfPagesViewed;
	private int numberOfUniques;

	private double costOfImpressions;
	private double costOfClicks;

	// ==== Constructor ====

	public Campaign(File campaignDirectory) throws FileNotFoundException {
		if (!campaignDirectory.isDirectory())
			throw new FileNotFoundException(campaignDirectory + " is not a directory!");

		impressionLog = new File(campaignDirectory, IMPRESSIONS_FILE);
		serverLog = new File(campaignDirectory, SERVERS_FILE);
		clickLog = new File(campaignDirectory, CLICKS_FILE);

		if (!impressionLog.exists() || !serverLog.exists() || !clickLog.exists())
			throw new FileNotFoundException(campaignDirectory + " is not a valid campaign directory!");

		this.campaignDirectory = campaignDirectory;
	}
	
	public final void loadData() {
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
	}

	// ==== Accessors ====

	public final Collection<Impression> getImpressions() {
		return impressionsList;
	}

	public final Collection<Click> getClicks() {
		return clicksList;
	}

	public final Collection<Server> getServers() {
		return serversList;
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
	 * 
	 */
	private void processServers() {
		final ArrayList<Server> serversList = new ArrayList<Server>(numberOfClicks);
		
		try (BufferedReader br = new BufferedReader(new FileReader(serverLog))) {
			// Initialise variables
			String line = br.readLine();

			// Reset variables
			numberOfPagesViewed = 0;
			numberOfConversions = 0;

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",");
				
				final long dateTime = DateProcessor.stringToEpoch(data[0]);
				final long userID = Long.valueOf(data[1]);
				final int userData = usersMap.get(userID);
				final long exitDateTime = DateProcessor.stringToEpoch(data[2]);
				final int pagesViewed = Integer.valueOf(data[3]);
				final boolean conversion = data[4].equals("No") ? false : true;
				
				final Server server = new Server(dateTime, userID, userData, exitDateTime, pagesViewed, conversion);

				// update page views
				numberOfPagesViewed += pagesViewed;

				// increment conversions if Yes
				if (conversion)
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
		final ArrayList<Click> clicksList = new ArrayList<Click>(20000);
		
		try (BufferedReader br = new BufferedReader(new FileReader(clickLog))) {
			// Initialise variables
			String line = br.readLine();

			// Reset counters etc
			costOfClicks = 0;

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",");
				
				final long dateTime = DateProcessor.stringToEpoch(data[0]);
				final long userID = Long.valueOf(data[1]);
				final int userData = usersMap.get(userID);
				final double cost = Double.parseDouble(data[2]);
				
				final Click click = new Click(dateTime, userID, userData, cost);

				// increment these values
				costOfClicks += cost;

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
		final ArrayList<Impression> impressionsList = new ArrayList<Impression>();

		try (FileInputStream fis = new FileInputStream(impressionLog)) {
			
			final FileChannel fc = fis.getChannel();

			final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			final int nullEntry = usersMap.getNoEntryValue();			
			final byte newLine = '\n';
			final byte comma = ',';
			
			long time = System.currentTimeMillis();
			mbb.load();
			System.out.println("Load time:\t" + (System.currentTimeMillis() - time) + "ms");
			time = System.currentTimeMillis();
			
			byte[] buffer = new byte[(int) fc.size()];
			mbb.get(buffer);
			
			System.out.println("To buffer:\t" + (System.currentTimeMillis() - time) + "ms");
			time = System.currentTimeMillis();
			
			int index = 50;
			
			// reset
			costOfImpressions = 0;

			
			while (index < buffer.length) {				
				long dateTime = 0;
				long userID = 0;
				double cost = 0;
				
				byte temp;

				/*
				 * BEGIN DATE PROCESSING SECTION
				 */

				int year = buffer[index++] & 0xF;
				year *= 10;
				year += buffer[index++] & 0xF;
				year *= 10;
				year += buffer[index++] & 0xF;
				year *= 10;
				year += buffer[index++] & 0xF;
				
				index++;

				int month = buffer[index++] & 0xF;
				month *= 10;
				month += buffer[index++] & 0xF;
				
				index++;

				int day = buffer[index++] & 0xF;
				day *= 10;
				day += buffer[index++] & 0xF;
				
				index++;
				
				int hour = buffer[index++] & 0xF;
				hour *= 10;
				hour += buffer[index++] & 0xF;
				
				index++;
				
				int minute = buffer[index++] & 0xF;
				minute *= 10;
				minute += buffer[index++] & 0xF;
				
				index++;
				
				int second = buffer[index++] & 0xF;
				second *= 10;
				second += buffer[index++] & 0xF;

		        long total = 0;
		        total += 365 * year;
		        total += (year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400;
		        total += ((367 * month - 362) / 12);
		        total += day - 1;
		        if (month > 2) {
		            total--;
		            // if is a leap year
		            if (!((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0)) {
		                total--;
		            }
		        }
		        
		        dateTime = (total - 719528) * 86400 + hour * 3600 + minute * 60 + second;
				
				/*
				 * END DATE PROCESSING SECTION
				 */

				// buffer
//				if (buffer[index++] != comma)
//					throw new IllegalArgumentException("invalid impression_log " + index);
		        index++;
				
				/*
				 * BEGIN USERID PROCESSING SECTION
				 */

				// we know MIN(id).length = 12
				// skip first multiplication by 0
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				userID *= 10;
				userID += buffer[index++] & 0xF;
				
				while (buffer[index] != comma) {
					userID *= 10;
					userID += buffer[index++] & 0xF;
				}
				
				index++;
				
				/*
				 * END USERID PROCESSING SECTION
				 */
				
				
				/*
				 * BEGIN USERDATA PROCESSING SECTION
				 */
				
				int userData = 0; //usersMap.get(userID);
				
//				if (userData == nullEntry) {					
					// process gender
//					index = mbb.position();
				
				if ((temp = buffer[index]) == 'F') {
					userData |= User.GENDER_FEMALE.mask;
					index += 6;
				} else if (temp == 'M') {
					userData |= User.GENDER_MALE.mask;
					index += 4;
				} else {
					throw new IllegalArgumentException("invalid gender " + temp);
				}
				
				index++;
				
//					if (buffer[index++] != comma)
//						throw new IllegalArgumentException("invalid impression_log gendah" + index);

				// process age
//					index = mbb.position();
				
				if ((temp = buffer[index]) == '2') {
					userData |= User.AGE_25_TO_34.mask;
					index += 5;
				} else if (temp == '4') {
					userData |= User.AGE_45_TO_54.mask;
					index += 5;
				} else if (temp == '3') {
					userData |= User.AGE_35_TO_44.mask;
					index += 5;
				} else if (temp == '>') {
					userData |= User.AGE_ABOVE_54.mask;
					index += 3;
				} else if (temp == '<') {
					userData |= User.AGE_BELOW_25.mask;
					index += 3;
				} else {
					throw new IllegalArgumentException("invalid age " + temp);
				}

				index++;
//					if (buffer[index++] != comma)
//						throw new IllegalArgumentException("invalid impression_log " + index);

				// process income
//					index = mbb.position();

				if ((temp = buffer[index]) == 'H') {
					userData |= User.INCOME_HIGH.mask;
					index += 4;
				} else if (temp == 'M') {
					userData |= User.INCOME_MEDIUM.mask;
					index += 6;						
				} else if (temp == 'L') {
					userData |= User.INCOME_LOW.mask;
					index += 3;
				} else {
					throw new IllegalArgumentException("invalid income " + temp);
				}

				index++;
//					if (buffer[index++] != comma)
//						throw new IllegalArgumentException("invalid impression_log " + index);

				// process context
//					index = mbb.position();
				
				if ((temp = buffer[index]) == 'N') {
					userData |= User.CONTEXT_NEWS.mask;
					index += 4;
				} else if (temp == 'S') {
					if ((temp = buffer[index + 1]) == 'o') {
						userData |= User.CONTEXT_SOCIAL_MEDIA.mask;
						index += 12;
					} else if (temp == 'h') {
						userData |= User.CONTEXT_SHOPPING.mask;
						index += 8;
					} else {
						throw new IllegalArgumentException("invalid context S" + (char) temp);
					}
				} else if (temp == 'B') {
					userData |= User.CONTEXT_BLOG.mask;
					index += 4;
				} else if (temp == 'T') {
					userData |= User.CONTEXT_TRAVEL.mask;
					index += 6;
				} else if (temp == 'H') {
					userData |= User.CONTEXT_HOBBIES.mask;
					index += 7;
				} else {
					throw new IllegalArgumentException("invalid context " + temp);
				}

				index++;
//					if (buffer[index] != comma)
//						throw new IllegalArgumentException("invalid impression_log " + index);
					
//					usersMap.put(userID, userData);
//				} else {
//					// skip by 4 commas
//					for (int i = 0; i < 4;) {
//						if (mbb.get() == comma)
//							i++;
//					}
//				}
					
				/*
				 * END USERDATA PROCESSING SECTION
				 */
				
				/*
				 * BEGIN COST PROCESSING SECTION
				 */
				
				int costTemp = buffer[index++] & 0xF;
				
				while (buffer[index] != '.') {
					costTemp *= 10;
					costTemp += buffer[index++];
				}
				
				index++;
				
				costTemp *= 10;
				costTemp += buffer[index++] & 0xF;
				
				costTemp *= 10;
				costTemp += buffer[index++] & 0xF;
				
				costTemp *= 10;
				costTemp += buffer[index++] & 0xF;
				
				costTemp *= 10;
				costTemp += buffer[index++] & 0xF;
				
				costTemp *= 10;
				costTemp += buffer[index++] & 0xF;
				
				costTemp *= 10;
				costTemp += buffer[index++] & 0xF;
				
//				// process cost
//				while ((temp = mbb.get()) != '.') {
//					cost *= 10;
//					cost += temp & 0xF;
//				}
//				
//				
//				// do this 6 times
//				cost *= 10;
//				cost += mbb.get() & 0xF;
//				
//				cost *= 10;
//				cost += mbb.get() & 0xF;
//				
//				cost *= 10;
//				cost += mbb.get() & 0xF;
//				
//				cost *= 10;
//				cost += mbb.get() & 0xF;
//				
//				cost *= 10;
//				cost += mbb.get() & 0xF;
//				
//				cost *= 10;
//				cost += mbb.get() & 0xF;
//								
//				// divide by 1 million -- long arithmetic -> double is faster
//				cost *= 0.000001;
				
//				if (buffer[index++] != newLine)
//					throw new IllegalArgumentException("invalid impression log " + index);
				index++;
				
				// add to list
//				impressionsList.add(new Impression(dateTime, userID, userData, cost));
				
				// misc increment
//				costOfImpressions += cost;
			}
			System.out.println("Processing:\t" + (System.currentTimeMillis() - time) + "ms");
			System.exit(0);
			// trim the ArrayList to save capacity
			impressionsList.trimToSize();
			
			// transfer references
			this.impressionsList = impressionsList;
			
			// compute size of impressions
			numberOfImpressions = impressionsList.size();
			numberOfUniques = usersMap.size();
			
			// compute dates
			campaignStartDate = impressionsList.get(0).getLocalDateTime();
			campaignEndDate = impressionsList.get(numberOfImpressions - 1).getLocalDateTime();

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
	public final String toString() {
		return campaignDirectory.getName();
	}

}