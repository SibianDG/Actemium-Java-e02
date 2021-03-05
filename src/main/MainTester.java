package main;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumTicket;
import domain.PopulateDB;
import domain.User;
import domain.enums.UserStatus;
import domain.facades.UserFacade;
import domain.manager.Actemium;
import repository.GenericDaoJpa;
import repository.UserDaoJpa;

public class MainTester {
    public static void main(String[] args) {
        System.out.println("This class tests the same as the follow test in DomainTest:");
        System.out.println("loginAttempt_4InValidAdmin_3InValidTech_1InValidAdmin_AdminUserBlocked_1ValidTech_TechUserLoginSuccess_1ValidAdmin_AdminUserStillBlocked()\n");
        UserDaoJpa userDaoJpa = new UserDaoJpa();
        GenericDaoJpa ticketDaoJpa = new GenericDaoJpa<>(ActemiumTicket.class);
        PopulateDB populateDB = new PopulateDB();
        populateDB.run(userDaoJpa);
        System.out.println("populateDB successful");
        
//        UserModel a = new Administrator("Admin123", "PassWd123&","Jan", "A");
//        UserModel t = new Technician("Tech123", "PassWd123&","Pol", "T");
        
        //TODO or do we have to do something like this?
        //GenericDao genericDao = new GenericDaoJpa(UserModel.class);
        Actemium actemium = new Actemium(userDaoJpa, ticketDaoJpa);
        UserFacade dc = new UserFacade(actemium);

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

        ActemiumCompany theWhiteHouse = new ActemiumCompany("The White House", "America 420", "911");
        // Can't register if username is already taken
        try {
        	System.out.println("\ndc.registerCustomer(\"Admin123\", \"Passwd123&\", \"Thierry\", \"Kempens\")");
        	dc.registerCustomer("Admin123", "Passwd123&", "Thierry", "Kempens", theWhiteHouse);
		} catch (Exception e) {
//          e.printStackTrace();
			System.out.println(e.getMessage());
		}
        
        ActemiumCustomer customer = (ActemiumCustomer) userDaoJpa.findByUsername("cust01Barak");
        
        System.out.printf("%nCustomer before modifyCustomer:%nFirst name: %s%nLast name: %s%n", customer.getFirstName(), customer.getLastName());
        
        dc.modifyCustomer(customer, "cust01Barak", "Passwd123&", "Thierry", "Kempens", theWhiteHouse, UserStatus.ACTIVE);
        
        System.out.printf("%nCustomer after modifyCustomer:%nFirst name: %s%nLast name: %s%n%n", customer.getFirstName(), customer.getLastName());
        
        System.out.println(actemium.giveActemiumCustomers());
        actemium.giveActemiumCustomers().stream().map(User::getUsername).forEach(System.out::println);
        System.out.println();
        System.out.println(actemium.giveActemiumEmployees());
        actemium.giveActemiumEmployees().stream().map(User::getUsername).forEach(System.out::println);

        userDaoJpa.closePersistency();
    }

}
