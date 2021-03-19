package domain.enums;

import languages.LanguageResource;

// TODO
// Only customer can have the status IN_REQUEST??
// Should we have separte enums for Employees and Customers?
public enum UserStatus {
	// Customer has registered an account
	// The new customer account needs to be activated by the Admin
	// Once ativated the customer can login and request a contract
	IN_REQUEST /*{
		@Override
		public String toString() {
			return LanguageResource.getString("IN_REQUEST");
		}
	}*/
	// User is able to login
	,ACTIVE /*{
		@Override
		public String toString() {
			return LanguageResource.getString("ACTIVE");
		}
	}*/
	// UserAccount has been blocked after 5 consecutive unsuccesfull login attempts
	// User must contact Administrator (SysAdmin) to unblock account
	,BLOCKED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("BLOCKED");
		}
	}*/
	// User account has been removed*
	// *Data is still kept in database
	,INACTIVE /*{
		@Override
		public String toString() {
			return LanguageResource.getString("INACTIVE");
		}
	}*/
	
}
