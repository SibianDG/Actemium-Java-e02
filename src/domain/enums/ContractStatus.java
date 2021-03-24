package domain.enums;

import languages.LanguageResource;

/**
 * The enum Contract status.
 */
public enum ContractStatus {
	// Original UC - by HoGent lectors
//	CREATED, ACTIVE, COMPLETED, CANCELLED

	/**
	 * The In request.
	 */
// Most recent update by Product Owner (Actemium)
	// All contracts created by the customer have status IN_REQUEST
	IN_REQUEST

	// After recieving "bestelbon" the support manager sets it to CURRENT
	,
	/**
	 * Current contract status.
	 */
	CURRENT

	// End date of contract has been reached
	,
	/**
	 * Expired contract status.
	 */
	EXPIRED

	// Cancelled the contract request or customer didn't pay
	,
	/**
	 * Cancelled contract status.
	 */
	CANCELLED

}
