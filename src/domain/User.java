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
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
	}

	public void resetLoginAttempts() {
		throw new UnsupportedOperationException();
	}

	public void increaseFailedLoginAttempts() {
		throw new UnsupportedOperationException();
	}

	public LoginAttempt getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(LoginAttempt loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
