package core.users;

public class InvalidUserException extends Exception {

	private static final long serialVersionUID = -4737051332069393977L;

	// ==== Constructor ====
	
	public InvalidUserException() {
	}

	public InvalidUserException(String string) {
		super(string);
	}
}
