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
import java.util.List;
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

	// ==== Properties ====

	// We may use this Timer to fire events as they occur
	private final Timer timer = new Timer(1000, this);

	// The DateFormat of log entries
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
			
			if (image != null)
				newImage.createGraphics().drawImage(image, 0, 0, size.width, size.height, null);
			
			// TODO You may think this is inefficient but there is a lot to optimise here
			image = newImage;
			
			// TODO Re-render with nicer scales?
			startDrawing();
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

	public synchronized int getNumberOfImpressions() {
		return impressions.size();
	}

	public synchronized int getNumberOfClicks() {
		return clicks.size();
	}

	public synchronized int getNumberOfUniques() {
		final Set<Long> userIDSet = new HashSet<Long>();

		// impressions.forEach(impression -> userIDSet.add(impression.userID));
		for (Impression impression : impressions)
			userIDSet.add(impression.userID);

		return userIDSet.size();
	}

	public synchronized int getNumberOfBounces() {
		int numberOfBounces = 0;

		for (Server server : servers)
			if (meetsBounceCriteria(server))
				numberOfBounces++;

		return numberOfBounces;
	}

	public synchronized int getNumberOfConversions() {
		// return (int) servers.stream().filter(server ->
		// server.conversion).count();

		int numberOfConversions = 0;

		for (Server server : servers)
			if (server.conversion)
				numberOfConversions++;

		return numberOfConversions;
	}

	public synchronized double getTotalCost() {
		// TODO Double's precision is lacking, use something else
		// TODO Look at BigDecimal maybe?

		double totalCost = 0;

		for (Impression impression : impressions)
			totalCost += impression.cost;

		for (Click click : clicks)
			totalCost += click.cost;

		return totalCost;
	}

	// ==== Private Helper Methods ====
	
	private void startDrawing() {
		
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
				final double cost = Double.parseDouble(data[6]);

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
				final double cost = Double.parseDouble(data[2]);

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
