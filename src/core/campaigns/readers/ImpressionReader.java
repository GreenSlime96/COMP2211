package core.campaigns.readers;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;

import core.campaigns.Campaign;
import core.records.Impression;

public class ImpressionReader implements Closeable, Iterable<Impression>, Iterator<Impression> {
	
	// ==== Properties ====
	
	private BufferedReader br;	
	private String line;
	
	
	// ==== Constructor ====
	
	public ImpressionReader(File impressionsLog) throws IOException {		
		br = new BufferedReader(new FileReader(impressionsLog));
		
		if (br.readLine().equals("Date,ID,Gender,Age,Income,Context,Impression Cost")) {	
			br.close();
			throw new IllegalArgumentException(impressionsLog + " invalid impressions log");
		}			
	}
	
	
	// ==== Closeable Implementation ====

	@Override
	public void close() throws IOException {
		br.close();
	}
	
	
	// ==== Iterator Implementation ====

	@Override
	public Iterator<Impression> iterator() {
		return this;
	}
	
	
	// ==== Iterable Implementation ====

	@Override
	public boolean hasNext() {
		try {
			line = br.readLine();
			
			if (line == null) {
				br.close();
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public Impression next() {
		final String[] data = line.split(",");
		
		final LocalDateTime date = LocalDateTime.parse(data[0], Campaign.formatter);
		final long userID = Long.parseLong(data[1]);
		final double cost = Double.parseDouble(data[6]);
		
		return new Impression(date, userID, cost);
	}
}
