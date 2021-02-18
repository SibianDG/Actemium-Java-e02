package domain;

import repository.UserDaoJpa;

public class PopulateDB {
    public void run() {
        UserDaoJpa userDao = new UserDaoJpa();
        userDao.startTransaction();

        userDao.insert(new Administrator("thomas123", "Passwd123&", "Thomas", "Dirven"));
        userDao.insert(new Administrator("sibian123", "Passwd123&", "Sibian", "De Gussem"));
        userDao.insert(new Administrator("isaac123", "Passwd123&", "Isaac", "Bauters"));
        userDao.insert(new Administrator("florian123", "Passwd123&", "Florian", "Goossens"));
        userDao.insert(new Technician("technician01", "Passwd123&", "Joost", "Jansens"));
        userDao.insert(new SupportManager("supportman01", "Passwd123&", "Joe", "Biden"));
        userDao.insert(new Customer("customer01", "Passwd123&", "Emma", "Dupont"));
        userDao.insert(new Customer("customer02", "Passwd123&", "Barak", "Obama"));                                                 
        userDao.insert(new Employee("employee01", "Passwd123&", "Donald", "Trump", "Straat", "PhoneNumber", "email", "role")); 
        userDao.insert(new Employee("employee02", "Passwd123&", "John", "Smiths", "Straat", "PhoneNumber", "email", "role"));
        
        userDao.commitTransaction();
    }
}