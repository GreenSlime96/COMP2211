package core.campaigns.readers;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GenericReader implements Closeable {
	
	// ==== Properties ====
	
	private final File CSVFile;
	
	private BufferedReader in;
	
	
	// ==== Constructor ====
	
	public GenericReader(File CSVFile) {
		if (!CSVFile.exists())
			throw new IllegalArgumentException(CSVFile + " does not exist!");
		
		this.CSVFile = CSVFile;
		
		try {
			in = new BufferedReader(new FileReader(CSVFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	// ==== Closeable Implementation ====
	
	@Override
	public void close() throws IOException {
        if (in == null)
            return;
        try {
            in.close();
        } finally {
            in = null;
        }
	}

}
