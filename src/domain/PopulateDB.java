package domain;

import repository.UserDaoJpa;

public class PopulateDB {
    public void run() {
        UserDaoJpa userDao = new UserDaoJpa();
        userDao.startTransaction();

        //Employee a = new Employee("thomas123", "Passwd123&", "Thomas", "Dirven","Kerstraat 18","PhoneNumber","emailadress@hogent.be", EmployeeRole.ADMINISTRATOR);
        //System.out.println(a.getStatus() + a.getPassword() + a.getEmailAddress() + a.getPhoneNumber() + a.getRole());
        ////userDao.insert(a);
        Customer c = new Customer("customer01", "Passwd123&", "Emma", "Dupont");
        userDao.insert(c);
        //userDao.insert(new Employee("isaac123", "Passwd123&", "Isaac", "Bauters","Kerstraat 18","PhoneNumber","emailadress@hogent.be", EmployeeRole.ADMINISTRATOR));
        //userDao.insert(new Employee("florian123", "Passwd123&", "Florian", "Goossens","Kerstraat 18","PhoneNumber","emailadress@hogent.be", EmployeeRole.ADMINISTRATOR));
        //userDao.insert(new Employee("sibianDG", "Passwd123&", "Sibian", "De Gussem","Kerstraat 18","PhoneNumber","emailadress@hogent.be", EmployeeRole.ADMINISTRATOR));
        ////userDao.insert/);
        ////userDao.insert(new Customer("customer02", "Passwd123&", "Barak", "Obama"));
        //userDao.insert(new Employee("employee01", "Passwd123&", "Donald", "Trump", "Straat", "PhoneNumber", "email", EmployeeRole.TECHNICIAN));
        //userDao.insert(new Employee("employee02", "Passwd123&", "John", "Smiths", "Straat", "PhoneNumber", "email", EmployeeRole.SUPPORT_MANAGER));
        
        userDao.commitTransaction();
        userDao.closePersistency();
    }
}