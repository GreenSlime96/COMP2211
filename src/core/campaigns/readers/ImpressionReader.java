package core.campaigns.readers;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import core.records.Impression;
import util.DateProcessor;

public class ImpressionReader {
	
	// ==== Constructor ====
	
	public ImpressionReader(File impressionsLog) {		

	}
	
	public static final List<Impression> buildImpressionList(File impressionLog) {
		return list;
	}
}
