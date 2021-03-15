package domain.enums;

public enum ContractStatus {
	// Original UC - by HoGent lectors
//	CREATED, ACTIVE, COMPLETED, CANCELLED
	
	// Most recent update by Product Owner (Actemium)
	// All contracts created by the customer have status IN_REQUEST
	IN_REQUEST, 
	// After recieving "bestelbon" the support manager sets it to CURRENT
	CURRENT, 
	// End date of contract has been reached
	EXPIRED, 
	// Cancelled the contract request or customer didn't pay
	CANCELLED
	
}
