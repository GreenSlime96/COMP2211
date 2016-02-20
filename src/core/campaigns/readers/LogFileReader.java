package core.campaigns.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* TODO:
 * - Automatically closing handles
 * - Finalize method call on GC to close handles
 */

abstract class LogFileReader<E> implements Iterable<E> {
	
	// ==== Properties ====
	
	private BufferedReader br = null;

	
	// ==== Constructor ====
	
	public LogFileReader(File file, int expectedColumns) {
		try {
			br = new BufferedReader(new FileReader(file));
			
			if (br.readLine().split(",").length != expectedColumns)
				throw new IllegalArgumentException("invalid log file");
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	
	// ==== Abstract ====
	
	abstract E processString(String[] data);


	// ==== Iterable Implementation ====

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
            String nextLine = null;
            
            @Override
            public boolean hasNext() {
                if (nextLine != null) {
                    return true;
                } else {
                    try {
                        nextLine = br.readLine();
                        
                        if (nextLine != null) {
                        	return true;
                        } else {
                        	br.close();
                        	return false;
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public E next() {
                if (nextLine != null || hasNext()) {
                    String line = nextLine;
                    nextLine = null;
                    return processString(line.split(","));
                } else {
                    throw new NoSuchElementException();
                }
            }
			
		};
	}
}
