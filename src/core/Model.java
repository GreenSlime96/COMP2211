package core;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.Timer;

public class Model extends Observable implements ActionListener {

	// ==== Constants ====

	public static final String SERVER_LOG = "server_log.csv";
	public static final String IMPRESSION_LOG = "impression_log.csv";
	public static final String CLICK_LOG = "click_log.csv";
	
	public static final boolean DEBUG_MODE = true;

	// ==== Properties ====

	// We may use this Timer to fire events as they occur
	private final Timer timer = new Timer(1000, this);

	// The DateFormat of log entries
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// Time Granularity used to separate Chart Events, in milliseconds
	// Default value is set to 1 day
	private long timeGranularity = TimeUnit.MINUTES.toMillis(5);
	
	// Start and End dates to display on chart
	private Date startDate;
	private Date endDate;

	// Directory of Campaign
	private File campaignDirectory;

	// Bounce Settings
	private int bounceMinimumPagesViewed = 1;
	private int bounceMinimumSecondsOnPage = 60;

	// Store Data in Lists
	// TODO Create Classes to represent Impressions, Clicks, etc...
	private List<Impression> impressions = new ArrayList<Impression>();
	private List<Click> clicks = new ArrayList<Click>();
	private List<Server> servers = new ArrayList<Server>();

	// Rendering
	private BufferedImage image;

	// ==== Constructor ====

	public Model() {
		super();

		// TODO Toss this into Controller, testing here...
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Campaign Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			setCampaignDirectory(chooser.getSelectedFile());
		} else {
			System.out.println("No Selection ");
		}

		long startTime = System.currentTimeMillis();

//		getNumberOfImpressions();
//		getNumberOfClicks();
//		getNumberOfUniques();
//		getNumberOfBounces();
//		getNumberOfConversions();
//		
//		getTotalCost();
		
		this.getClickThroughRate();

		System.out.println("Time Elapsed:\t" + (System.currentTimeMillis() - startTime));
	}

	// ==== Accessors ====

	public synchronized final BufferedImage getImage() {
		return image;
	}

	public synchronized final Dimension getSize() {
		return new Dimension(image.getWidth(), image.getHeight());
	}

	public synchronized final void setSize(Dimension size) {
		// Update only if there is a change
		if (image == null || !getSize().equals(size)) {
			BufferedImage newImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);

			System.out.println("resize called");

			if (image != null)
				newImage.createGraphics().drawImage(image, 0, 0, size.width, size.height, null);

			// TODO You may think this is inefficient but there is a lot to
			// optimise here
			image = newImage;

			// TODO Re-render with nicer scales?
			// startDrawing();
		}
	}

	public synchronized void setCampaignDirectory(File campaignDirectory) {
		// We first need to check whether the 3 required files exist in this
		// directory
		// We will do validation on BOTH Controller and Model, the Model in this
		// case serves as our API

		// Do not do anything if the directory isn't a directory
		if (!campaignDirectory.isDirectory()) {
			System.err.println(campaignDirectory + " is not directory!");
			return;
		}

		File serverLog = new File(campaignDirectory + File.separator + SERVER_LOG);
		File impressionLog = new File(campaignDirectory + File.separator + IMPRESSION_LOG);
		File clickLog = new File(campaignDirectory + File.separator + CLICK_LOG);

		// If neither of these files exist, we do nothing
		if (!serverLog.isFile() || !impressionLog.isFile() || !clickLog.isFile()) {
			System.err.println(campaignDirectory + " is not a valid campaign!");
			return;
		}

		this.campaignDirectory = campaignDirectory;

		long startTime = System.currentTimeMillis();

		refreshImpressions();
		refreshClicks();
		refreshServer();

		System.out.println(System.currentTimeMillis() - startTime);
	}

	public synchronized File getCampaignDirectory() {
		return campaignDirectory;
	}

	public synchronized void setBounceMinimumPagesViewed(int bounceMinimumPagesViewed) {
		if (bounceMinimumPagesViewed < 1)
			throw new IllegalArgumentException("You cannot view less than 1 page!");

		this.bounceMinimumPagesViewed = bounceMinimumPagesViewed;
	}

	public synchronized int getBounceMinimumPagesViewed() {
		return bounceMinimumPagesViewed;
	}

	public synchronized void setBounceMinimumSecondsOnPage(int bounceMinimumSecondsOnPage) {
		if (bounceMinimumSecondsOnPage < 1)
			throw new IllegalArgumentException("You cannot spend less than 1 second on page!");

		this.bounceMinimumSecondsOnPage = bounceMinimumSecondsOnPage;
	}

	// ==== Requirement 2 ====

	public synchronized Map<Date, Integer> getNumberOfImpressions() {
		Map<Date, Integer> impressionsPerDate = new LinkedHashMap<Date, Integer>();

		// Breaking out the Date
		Date currentDate = impressions.get(0).date;
		Date nextDate = new Date(currentDate.getTime() + timeGranularity);

		System.out.println("Starting Date:\t" + currentDate);
		System.out.println("Next Date:\t" + nextDate);

		int numberOfImpressions = 0;

		for (Impression impression : impressions) {

			// If impression date is beyond the granularity, we add a new
			// mapping
			if (impression.date.after(nextDate)) {

				// Before reset, store these elsewhere
				impressionsPerDate.put(currentDate, numberOfImpressions);

				// Reset these variables
				numberOfImpressions = 0;
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
			}

			numberOfImpressions++;
		}

		// We need to add the last Impression into the mapping
		impressionsPerDate.put(currentDate, numberOfImpressions);

		if (DEBUG_MODE) {
			numberOfImpressions = 0;
	
			for (Date date : impressionsPerDate.keySet()) {
				numberOfImpressions += impressionsPerDate.get(date);
				System.out.println(date + "\t" + impressionsPerDate.get(date));
			}
	
			System.out.println(numberOfImpressions == impressions.size());
		}

		return impressionsPerDate;
	}

	public synchronized Map<Date, Integer> getNumberOfClicks() {
		Map<Date, Integer> clicksPerDate = new LinkedHashMap<Date, Integer>();

		Date currentDate = clicks.get(0).date;
		Date nextDate = new Date(currentDate.getTime() + timeGranularity);

		int numberOfClicks = 0;

		for (Click click : clicks) {

			// If impression date is beyond the granularity, we add a new
			// mapping
			if (click.date.after(nextDate)) {

				// Before reset, store these elsewhere
				clicksPerDate.put(currentDate, numberOfClicks);

				// Reset these variables
				numberOfClicks = 0;
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
			}

			numberOfClicks++;
		}

		// Add the last Click into the mapping
		clicksPerDate.put(currentDate, numberOfClicks);

		if (DEBUG_MODE) {
			numberOfClicks = 0;

			for (Date date : clicksPerDate.keySet()) {
				numberOfClicks += clicksPerDate.get(date);
				System.out.println(date + "\t" + clicksPerDate.get(date));
			}

			System.out.println(numberOfClicks == clicks.size());
		}

		return clicksPerDate;
	}

	public synchronized Map<Date, Integer> getNumberOfUniques() {
		Map<Date, Integer> uniquesPerDate = new LinkedHashMap<Date, Integer>();

		Date currentDate = clicks.get(0).date;
		Date nextDate = new Date(currentDate.getTime() + timeGranularity);

		// Use the Set as a counter
		final Set<Long> userIDSet = new HashSet<Long>();

		for (Click click : clicks) {

			// If the click date is beyond the granularity, we create a new
			// mapping
			if (click.date.after(nextDate)) {

				// Store the values before we reset
				uniquesPerDate.put(currentDate, userIDSet.size());

				// Reset these variables
				userIDSet.clear();
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
			}

			userIDSet.add(click.userID);
		}

		uniquesPerDate.put(currentDate, userIDSet.size());
		
		if (DEBUG_MODE) {
			userIDSet.clear();

			for (Date date : uniquesPerDate.keySet())
				System.out.println(date + "\t" + uniquesPerDate.get(date));
		}

		return uniquesPerDate;
	}

	public synchronized Map<Date, Integer> getNumberOfBounces() {
		Map<Date, Integer> bouncesPerDate = new LinkedHashMap<Date, Integer>();

		Date currentDate = servers.get(0).entryDate;
		Date nextDate = new Date(currentDate.getTime() + timeGranularity);

		int numberOfBounces = 0;

		for (Server server : servers) {

			// If Entry Date is beyond granularity, add new mapping
			if (server.entryDate.after(nextDate)) {

				// Store variable before reset
				bouncesPerDate.put(currentDate, numberOfBounces);

				// Reset these variables
				numberOfBounces = 0;
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
			}

			if (meetsBounceCriteria(server))
				numberOfBounces++;
		}
		
		// Adds the last bounce into the mapping
		bouncesPerDate.put(currentDate, numberOfBounces);
		
		if (DEBUG_MODE) {
			numberOfBounces = 0;
			int bounces = 0;

			for (Date date : bouncesPerDate.keySet()) {
				bounces += bouncesPerDate.get(date);
				System.out.println(date + "\t" + bouncesPerDate.get(date));
			}

			for (Server server : servers)
				if (meetsBounceCriteria(server))
					numberOfBounces++;

			System.out.println(bounces == numberOfBounces);
		}

		return bouncesPerDate;
	}

	public synchronized Map<Date, Integer> getNumberOfConversions() {
		Map<Date, Integer> conversionsPerDate = new LinkedHashMap<Date, Integer>();
		
		Date currentDate = servers.get(0).entryDate;
		Date nextDate = new Date(currentDate.getTime() + timeGranularity);

		int numberOfConversions = 0;
		
		for (Server server : servers) {
			
			// If Entry Date is beyond... add new mapping
			if (server.entryDate.after(nextDate)) {
				
				// Store variable before reset
				conversionsPerDate.put(currentDate, numberOfConversions);
				
				// Reset these variables
				numberOfConversions = 0;
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
			}
			
			if (server.conversion)
				numberOfConversions++;
		}
		
		// Add last entry into mapping
		conversionsPerDate.put(currentDate, numberOfConversions);

		if (DEBUG_MODE) {
			int temp = 0;
			numberOfConversions = 0;

			for (Date date : conversionsPerDate.keySet()) {
				temp += conversionsPerDate.get(date);
				System.out.println(date + "\t" + conversionsPerDate.get(date));
			}

			for (Server server : servers)
				if (server.conversion)
					numberOfConversions++;

			// Testing
			System.out.println(temp == numberOfConversions);
		}

		return conversionsPerDate;
	}

	public synchronized Map<Date, Long> getTotalCost() {		
		Map<Date, Long> totalCostPerDate = new LinkedHashMap<Date, Long>();

		// Hypothesis: impression will happen before a click
		Date currentDate = impressions.get(0).date;
		Date nextDate = new Date(currentDate.getTime() + timeGranularity);

		long totalCost = 0;
		
		for (Impression impression : impressions) {
			if (impression.date.after(nextDate)) {
				totalCostPerDate.put(currentDate, totalCost);
				
				totalCost = 0;
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
			}
			
			totalCost += impression.cost;
		}
		
		// Add last entry into mapping
		totalCostPerDate.put(currentDate, totalCost);

		currentDate = impressions.get(0).date;
		nextDate = new Date(currentDate.getTime() + timeGranularity);
		
		totalCost = totalCostPerDate.get(currentDate);
		
		for (Click click : clicks) {
			if (click.date.after(nextDate)) {
				totalCostPerDate.put(currentDate, totalCost);
				
				currentDate = nextDate;
				nextDate = new Date(currentDate.getTime() + timeGranularity);
				totalCost = totalCostPerDate.get(currentDate);
			}
			
			totalCost += click.cost;
		}
		
		totalCostPerDate.put(currentDate, totalCost);
		
		if (DEBUG_MODE) {
			long temp = 0;

			for (Date date : totalCostPerDate.keySet()) {
				temp += totalCostPerDate.get(date);
				System.out.println(date + "\t" + totalCostPerDate.get(date));
			}

			// Tests
			totalCost = 0;

			for (Impression impression : impressions)
				totalCost += impression.cost;

			for (Click click : clicks)
				totalCost += click.cost;

			System.out.println(totalCost == temp);
			// End Tests
		}

		return totalCostPerDate;
	}
	
	public synchronized final void getClickThroughRate() {
//		clicks per impression
		final Map<Date, Double> clickThroughRate = new LinkedHashMap<Date, Double>();
		
		final Map<Date, Integer> clicksPerDate = getNumberOfClicks();
		final Map<Date, Integer> impressionsPerDate = getNumberOfImpressions();
		
		final Iterator<Entry<Date, Integer>> clicksIterator = clicksPerDate.entrySet().iterator();
		final Iterator<Entry<Date, Integer>> impressionsIterator = impressionsPerDate.entrySet().iterator();

		while (impressionsIterator.hasNext() && clicksIterator.hasNext()) {
			final Entry<Date, Integer> impressionsEntry = impressionsIterator.next();
			final Entry<Date, Integer> clicksEntry = clicksIterator.next();
			
			final int numberOfImpressions = impressionsEntry.getValue();
			final int numberOfClicks = clicksEntry.getValue();
			
			final double clicksPerImpression = (double) numberOfClicks / (double) numberOfImpressions;
			clickThroughRate.put(impressionsEntry.getKey(), clicksPerImpression);
			
			System.out.println(impressionsEntry.getKey() + "\t" + clicksEntry.getKey());
		}
		
		System.out.println(clicksPerDate.keySet().size() + "\t" + impressionsPerDate.keySet().size());
		
	}
	
	public synchronized final void getCostPerAcquisition() {
//		money spent on advertising per acquisition
	}
	
	public synchronized final void getCostPerClick() {
//		money spend overall per click
	}
	
	public synchronized final void getCostPerThousandImpressions() {
//		self-explanatory
	}
	
	public synchronized final void getBounceRate() {
//		bounces per click
	}

	// ==== Private Helper Methods ====

	private void startDrawing() {
		// JFXPanel fxPanel = new JFXPanel();

		System.out.println("fxCreated");

		setChanged();
		notifyAll();
	}

	private boolean meetsBounceCriteria(Server server) {
		if (server.pagesViewed <= bounceMinimumPagesViewed)
			return true;

		if (server.exitDate == null)
			return false;

		// Time Difference between Exit Time and Entry Time in Milliseconds
		final long timeDiff = server.exitDate.getTime() - server.entryDate.getTime();

		if (TimeUnit.MILLISECONDS.toSeconds(timeDiff) <= bounceMinimumSecondsOnPage)
			return true;

		return false;
	}

	private void refreshImpressions() {

		// TODO Investigate whether .clear() is more suitable
		impressions = new ArrayList<Impression>();

		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new FileReader(campaignDirectory + File.separator + IMPRESSION_LOG));

			// TODO improve on this, but we toss the first line as it is the
			// header
			br.readLine();

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",");

				// Process the Date
				final Date date = format.parse(data[0]);

				// Process the userID
				final long userID = Long.parseLong(data[1]);

				// Process these...
				// TODO Convert to use Enum
				final String gender = data[2];
				final String age = data[3];
				final String income = data[4];
				final String context = data[5];

				// TODO Double is a waste of space! Store as Int maybe?
				// Also Double lacks precision, which is crucial as we are
				// dealing with currency
				final int cost = Integer.parseInt(data[6].replace(".", ""));
				
				// Error checking
				final String[] s = data[6].split("\\.");
				if (s.length != 2 || s[1].length() != 6)
					throw new NumberFormatException("Malformed Entry in row 5, value " + data[6]);

				// Add to Impressions List
				impressions.add(new Impression(date, userID, gender, age, income, context, cost));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void refreshClicks() {

		// TODO Investigate whether .clear() is more suitable
		clicks = new ArrayList<Click>();

		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new FileReader(campaignDirectory + File.separator + CLICK_LOG));

			// TODO improve on this, but we toss the first line as it is the
			// header
			br.readLine();

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",");

				// Process the Date
				final Date date = format.parse(data[0]);

				// Process the userID
				final long userID = Long.parseLong(data[1]);

				// Process the Cost
				final int cost = Integer.parseInt(data[2].replace(".", ""));
				
				// Error checking
				// Error checking
				final String[] s = data[2].split("\\.");
				if (s.length != 2 || s[1].length() != 6)
					throw new NumberFormatException("Malformed Entry in row 5, value " + data[2]);

				// Add to Clicks List
				clicks.add(new Click(date, userID, cost));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void refreshServer() {

		// TODO Investigate whether .clear() is more suitable
		servers = new ArrayList<Server>();

		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new FileReader(campaignDirectory + File.separator + SERVER_LOG));

			// TODO improve on this, but we toss the first line as it is the
			// header
			br.readLine();

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",");

				// Process Entry Date
				final Date entryDate = format.parse(data[0]);

				// Process the userID
				final long userID = Long.parseLong(data[1]);

				// Process Exit Date: if n/a then there is not Exit Date
				final Date exitDate = data[2].equals("n/a") ? null : format.parse(data[2]);

				// Process Pages Viewed
				final int pagesViewed = Integer.parseInt(data[3]);

				// Process Conversion
				final boolean conversion = data[4].equals("Yes") ? true : false;

				// Add to Server List
				servers.add(new Server(entryDate, userID, exitDate, pagesViewed, conversion));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ==== ActionListener Implementation ====

	@Override
	public void actionPerformed(ActionEvent e) {

		// This block of code executes every time the Timer fires
		// Notifies all Observers of an event
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
		}

	}

}
