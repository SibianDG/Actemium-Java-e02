package start;

import domain.Administrator;
import domain.DomainController;
import domain.UserModel;

import repository.GenericDaoJpa;
import repository.UserDaoJpa;

import javax.persistence.EntityManager;

public class MainTester {
    public static void main(String[] args) {
        System.out.println("Main1 - aanmaak van 3 docent objecten en persisteren");
        UserModel a = new Administrator("Admin123", "PassWd123&","Jan", "A");

        UserDaoJpa userDaoJpa = new UserDaoJpa();


        UserDaoJpa.startTransaction();
        userDaoJpa.insert(a);

        UserDaoJpa.commitTransaction();

        DomainController dc = new DomainController();
        dc.signIn("Admin123", "PassWd123&");

        UserDaoJpa.closePersistency();
    }

}
