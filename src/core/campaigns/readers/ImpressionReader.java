package core.campaigns.readers;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;

import core.campaigns.Campaign;
import core.records.Impression;

public class ImpressionReader extends IterableBufferedReader<Impression> {
	
	// ==== Constructor ====
	
	public ImpressionReader(File impressionsLog) throws IOException {		
		super(impressionsLog, "test");
	}


	@Override
	Impression processString(String[] data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
