package domain.enums;

import languages.LanguageResource;

public enum ContractStatus {
	// Original UC - by HoGent lectors
//	CREATED, ACTIVE, COMPLETED, CANCELLED
	
	// Most recent update by Product Owner (Actemium)
	// All contracts created by the customer have status IN_REQUEST
	IN_REQUEST /* {
		@Override
		public String toString() {
			return LanguageResource.getString("IN_REQUEST");
		}
	} */

	// After recieving "bestelbon" the support manager sets it to CURRENT
	,CURRENT /*{
		@Override
		public String toString() {
			return LanguageResource.getString("CURRENT");
		}
	}*/
	// End date of contract has been reached
	,EXPIRED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("EXPIRED");
		}
	}*/
	// Cancelled the contract request or customer didn't pay
	,CANCELLED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("CANCELLED");
		}
	}*/
	
}
