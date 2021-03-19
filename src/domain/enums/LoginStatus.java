package domain.enums;

import languages.LanguageResource;

public enum LoginStatus {
	// Correct username and password combination for an ACTIVE userAccount
	SUCCESS /*{
		@Override
		public String toString() {
			return LanguageResource.getString("SUCCESS");
		}
	}*/
	// Incorrect password for existing username
	// or
	// Correct username and password combination for BLOCKED/INACTIVE userAccount
	, FAILED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("FAILED");
		}
	}*/
	
	// LoginAttempts for nonExisting usernames are NOT logged in the database
}
