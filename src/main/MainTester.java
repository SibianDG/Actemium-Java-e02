package main;

import domain.Administrator;
import domain.DomainController;
import domain.Technician;
import domain.UserModel;

import repository.GenericDaoJpa;
import repository.UserDaoJpa;

import javax.persistence.EntityManager;

public class MainTester {
    public static void main(String[] args) {
        System.out.println("Main1 - creating and persisting 3 docent objects");
        UserModel a = new Administrator("Admin123", "PassWd123&","Jan", "A");
        UserModel t = new Technician("Tech123", "PassWd123&","Pol", "T");

        UserDaoJpa userDaoJpa = new UserDaoJpa();


        UserDaoJpa.startTransaction();
        userDaoJpa.insert(a);
        userDaoJpa.insert(t);

        UserDaoJpa.commitTransaction();

        DomainController dc = new DomainController();

        for (int i = 0; i < 7; i++) {
            try {
                dc.signIn("Admin123", "PassWd123");
            } catch (Exception e) {
//                e.printStackTrace();
            	System.out.println(e.getMessage());
            }
        }
        try {
            dc.signIn("Admin123", "PassWd123&");	
		} catch (Exception e) {
//          e.printStackTrace();
      	System.out.println(e.getMessage());
		}
        for (int i = 0; i < 3; i++) {
            try {
                dc.signIn("Tech123", "PassWd123");
            } catch (Exception e) {
//              e.printStackTrace();
            	System.out.println(e.getMessage());
            }
        }
        dc.signIn("Tech123", "PassWd123&");


        UserDaoJpa.closePersistency();
    }

}
