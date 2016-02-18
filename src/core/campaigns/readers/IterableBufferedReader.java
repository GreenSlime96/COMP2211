package core.campaigns.readers;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/* TODO:
 * - Implement Iterable Buffered Reader
 * - Automatically closing handles
 * - Finalize method call on GC to close handles
 * - Handles exceptions
 */

public abstract class IterableBufferedReader<E> implements Iterable<E>, Iterator<E> {
	
	// ==== Properties ====
	
	private BufferedReader br = null;
	private String line = null;
	
	
	// ==== Constructor ====
	
	public IterableBufferedReader(File file, String headers) throws IOException {
		br = new BufferedReader(new FileReader(file));
		
		if (!br.readLine().equals(headers))
			throw new IllegalArgumentException(file + " is not a valid CSV");
	}
	
	
	// ==== Abstract ====
	
	abstract E processString(String[] data);


	// ==== Iterable Implementation ====

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return this;
	}


	// ==== Iterator Implementation ====

	@Override
	public boolean hasNext() {
		try {
			line = br.readLine();
		} catch (IOException e) {
			return false;
		}
		
		return line != null;
	}

	@Override
	public E next() {
		return processString(line.split(","));
	}

}
