package core.campaigns.readers;

import java.io.File;
import java.time.LocalDateTime;

import core.records.Click;
import core.records.Impression;
import core.records.Server;
import util.DateProcessor;

public class ServerReader extends LogFileReader<Server> {
	
	// ==== Constructor ====
	
	public ServerReader(File impressionsLog) {		
		super(impressionsLog, 5);
	}
	
	
	// ==== LogFileReader implementation ====

	@Override
	Server processString(String[] data) {		
		return new Server(data);
	}	
}
