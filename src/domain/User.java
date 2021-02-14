package domain;

public abstract class User {

	private LoginAttempt loginAttempts;
	private int userId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private int failedLoginAttempts;
	private UserStatus status;

	public User(String username, String password, String firstName, String lastName) {
		throw new UnsupportedOperationException();
	}

	public void resetLoginAttempts() {
		throw new UnsupportedOperationException();
	}

	public void increaseFailedLoginAttempts() {
		throw new UnsupportedOperationException();
	}
}
