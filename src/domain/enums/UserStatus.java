package domain.enums;

public enum UserStatus {
	// User is able to login
	ACTIVE, 
	// UserAccount has been blocked after 5 consecutive unsuccesfull login attempts
	// User must contact Administrator (SysAdmin) to unblock account
	BLOCKED, 
	// User account has been removed*
	// *Data is still kept in database
	INACTIVE
	
}
