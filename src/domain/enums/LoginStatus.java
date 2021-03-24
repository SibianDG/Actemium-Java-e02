package domain.enums;

import languages.LanguageResource;

/**
 * The enum Login status.
 */
public enum LoginStatus {
	/**
	 * The Success.
	 */
// Correct username and password combination for an ACTIVE userAccount
	SUCCESS

	// Incorrect password for existing username
	// or
	// Correct username and password combination for BLOCKED/INACTIVE userAccount
	,
	/**
	 * Failed login status.
	 */
	FAILED

	
	// LoginAttempts for nonExisting usernames are NOT logged in the database
}
