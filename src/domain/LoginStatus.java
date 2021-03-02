package domain;

public enum LoginStatus {
	// Correct username and password combination for an ACTIVE userAccount
	SUCCESS, 
	// Incorrect password for existing username
	// or
	// Correct username and password combination for BLOCKED/INACTIVE userAccount
	FAILED
	
	// LoginAttempts for nonExisting usernames are NOT logged in the database
}
