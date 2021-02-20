package main;

import domain.Customer;
import domain.DomainController;
import domain.Employee;
import domain.EmployeeRole;
import domain.PopulateDB;
import domain.UserModel;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.UserDaoJpa;

public class MainTester {
    public static void main(String[] args) {
        System.out.println("This class tests the same as the follow test in DomainTest:");
        System.out.println("loginAttempt_4InValidAdmin_3InValidTech_1InValidAdmin_AdminUserBlocked_1ValidTech_TechUserLoginSuccess_1ValidAdmin_AdminUserStillBlocked()\n");
        System.out.println("MainTester - creating and persisting 2 user objects");
        UserDaoJpa userDaoJpa = new UserDaoJpa();
        PopulateDB populateDB = new PopulateDB();
        //populateDB.run();
        //GenericDao genericDao = new GenericDaoJpa(UserModel.class);
        Employee a = new Employee("thomas123", "Passwd123&", "Thomas", "Dirven","Kerstraat 18","PhoneNumber","emailadress@hogent.be", EmployeeRole.ADMINISTRATOR);
        Employee b = new Employee("thomas123", "Passwd123&", "Thomas", "Dirven","Kerstraat 18","PhoneNumber","emailadress@hogent.be", EmployeeRole.ADMINISTRATOR);
        UserModel c = new Customer("customer01", "Passwd123&", "Emma", "Dupont");
        userDaoJpa.startTransaction();
        userDaoJpa.insert(a);
        userDaoJpa.insert(b);
        userDaoJpa.commitTransaction();
        System.out.println("finished");
         /*
        UserModel a = new Administrator("Admin123", "PassWd123&","Jan", "A");
        UserModel t = new Technician("Tech123", "PassWd123&","Pol", "T");

        
        //TODO or do we have to do something like this?

        userDaoJpa.insert(t);

        userDaoJpa.commitTransaction();

        DomainController dc = new DomainController(userDaoJpa);

        // 4 failed login attempts for Admin123
        for (int i = 0; i < 4; i++) {
            try {
            	System.out.println("\ndc.signIn(\"Admin123\", \"PassWd123\")");
                dc.signIn("Admin123", "PassWd123");
            } catch (Exception e) {
//              e.printStackTrace();
            	System.out.println(e.getMessage());
            }
        }
        // 3 failed login attempts for Tech123
        for (int i = 0; i < 3; i++) {
            try {
            	System.out.println("\ndc.signIn(\"Tech123\", \"PassWd123\")");
                dc.signIn("Tech123", "PassWd123");
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
        	System.out.println("\ndc.signIn(\"Admin123\", \"PassWd123\")");
            dc.signIn("Admin123", "PassWd123");	
		} catch (Exception e) {
//          e.printStackTrace();
			System.out.println(e.getMessage());
		}
        
        // 1 successful login attempt for Tech123
    	System.out.println("\ndc.signIn(\"Tech123\", \"PassWd123&\")");
        dc.signIn("Tech123", "PassWd123&");
        
        // Admin123 is already blocked but still tries to sign in
        // this time with the correct password but since the accout 
        // is blocked this will result in a FAILED login attempt
        try {
        	System.out.println("\ndc.signIn(\"Admin123\", \"PassWd123&\")");
            dc.signIn("Admin123", "PassWd123&");	
		} catch (Exception e) {
//          e.printStackTrace();
			System.out.println(e.getMessage());
		}
		*/

        userDaoJpa.closePersistency();
    }

}
