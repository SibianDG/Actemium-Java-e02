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
		setFailedLoginAttempts(0);
		setStatus(UserStatus.ACTIVE);
	}

	public void resetLoginAttempts() {
		setFailedLoginAttempts(0);
	}

	public void increaseFailedLoginAttempts() {
		failedLoginAttempts++;
		if (failedLoginAttempts > 5) {
			throw new IllegalArgumentException("User has reached more than 5 failed login attempts, account has been blocked.");
		}		
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
		String usernameRegex = "[A-Za-z0-9]+";
		if(username == null || username.isEmpty() || !username.matches(usernameRegex)) {
			throw new IllegalArgumentException("Invalid Username");
		}
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$";
		if(password == null || password.isEmpty() || !password.matches(passwordRegex)){
			throw new IllegalArgumentException("Invalid Password");
		}
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		String firstNameRegex = "[^0-9]+";
		if(firstName == null || firstName.isEmpty() || firstName.matches(firstNameRegex)){
			throw new IllegalArgumentException("Invalid Password");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		String lastNameRegex = "[^0-9]+";
		if(lastName == null || lastName.isEmpty() || lastName.matches(lastNameRegex)){
			throw new IllegalArgumentException("Invalid Password");
		}
		this.lastName = lastName;
	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	private void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
