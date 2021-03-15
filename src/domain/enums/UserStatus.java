package domain.enums;

// TODO
// Only customer can have the status IN_REQUEST??
// Should we have separte enums for Employees and Customers?
public enum UserStatus {
	// Customer has registered an account
	// The new customer account needs to be activated by the Admin
	// Once ativated the customer can login and request a contract
	IN_REQUEST,
	// User is able to login
	ACTIVE, 
	// UserAccount has been blocked after 5 consecutive unsuccesfull login attempts
	// User must contact Administrator (SysAdmin) to unblock account
	BLOCKED, 
	// User account has been removed*
	// *Data is still kept in database
	INACTIVE
	
}
