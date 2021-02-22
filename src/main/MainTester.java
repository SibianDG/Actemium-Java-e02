package main;

import domain.DomainController;
import domain.PopulateDB;
import repository.UserDaoJpa;

public class MainTester {
    public static void main(String[] args) {
        System.out.println("This class tests the same as the follow test in DomainTest:");
        System.out.println("loginAttempt_4InValidAdmin_3InValidTech_1InValidAdmin_AdminUserBlocked_1ValidTech_TechUserLoginSuccess_1ValidAdmin_AdminUserStillBlocked()\n");
        UserDaoJpa userDaoJpa = new UserDaoJpa();
        PopulateDB populateDB = new PopulateDB();
        populateDB.run(userDaoJpa);
        System.out.println("populateDB successful");
        
//        UserModel a = new Administrator("Admin123", "PassWd123&","Jan", "A");
//        UserModel t = new Technician("Tech123", "PassWd123&","Pol", "T");
        
        //TODO or do we have to do something like this?
        //GenericDao genericDao = new GenericDaoJpa(UserModel.class);

        DomainController dc = new DomainController(userDaoJpa);

        // 4 failed login attempts for Admin123
        for (int i = 0; i < 4; i++) {
            try {
            	System.out.println("\ndc.signIn(\"Admin123\", \"Passwd123\")");
                dc.signIn("Admin123", "Passwd123");
            } catch (Exception e) {
//              e.printStackTrace();
            	System.out.println(e.getMessage());
            }
        }
        // 3 failed login attempts for Tech123
        for (int i = 0; i < 3; i++) {
            try {
            	System.out.println("\ndc.signIn(\"Tech123\", \"Passwd123\")");
                dc.signIn("Tech123", "Passwd123");
            } catch (Exception e) {
//              e.printStackTrace();
            	System.out.println(e.getMessage());
            }
        }
        // 1 failed login attempts for Admin123
        // the 4 previous failed login attempts have been saved in the DB
        // the system knows this will be the 5th failed login attempt
        // the system blocks the user
        try {
        	System.out.println("\ndc.signIn(\"Admin123\", \"Passwd123\")");
            dc.signIn("Admin123", "Passwd123");	
		} catch (Exception e) {
//          e.printStackTrace();
			System.out.println(e.getMessage());
		}
        
        // 1 successful login attempt for Tech123
    	System.out.println("\ndc.signIn(\"Tech123\", \"Passwd123&\")");
        dc.signIn("Tech123", "Passwd123&");
        
        // Admin123 is already blocked but still tries to sign in
        // this time with the correct password but since the accout 
        // is blocked this will result in a FAILED login attempt
        try {
        	System.out.println("\ndc.signIn(\"Admin123\", \"Passwd123&\")");
            dc.signIn("Admin123", "Passwd123&");	
		} catch (Exception e) {
//          e.printStackTrace();
			System.out.println(e.getMessage());
		}		

        userDaoJpa.closePersistency();
    }

}
