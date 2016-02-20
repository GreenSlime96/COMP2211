package core.campaigns.readers;

import java.io.File;
import java.time.LocalDateTime;

import core.records.Click;
import core.records.Impression;
import util.DateProcessor;

public class ClickReader extends LogFileReader<Click> {
	
	// ==== Constructor ====
	
	public ClickReader(File impressionsLog) {		
		super(impressionsLog, 3);
	}
	
	
	// ==== LogFileReader implementation ====

	@Override
	Click processString(String[] data) {		
		return new Click(data);
	}	
}
