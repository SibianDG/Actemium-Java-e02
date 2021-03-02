package domain;

import repository.UserDaoJpa;

public class PopulateDB {
    public void run(UserDaoJpa userDao) {
    	System.out.println("DBTester - creating and persisting multiple user objects");        
        userDao.startTransaction();
        
        Company theWhiteHouse = new Company("The White House", "America 420", "911");
        Customer barak = new Customer("cust01Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        ActemiumTicket ticket01 = new ActemiumTicket(TicketPriority.P1, "Remove Trump From White House", "I forgot my diary in the white house, Trump doesn't let me in to retriev it", barak);
        barak.addTicket(ticket01);
        
        userDao.insert(new Employee("Admin123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("Tech123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN));
        userDao.insert(new Employee("thomas123", "Passwd123&", "Thomas", "Dirven","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("isaac123", "Passwd123&", "Isaac", "Bauters","Kerstraat 71","094812384","isaac.bauters@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("florian123", "Passwd123&", "Florian", "Goossens","Groensstraat 103","096248753","florian.goossens@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("sibianDG", "Passwd123&", "Sibian", "De Gussem","Hoogstraat 89","092564812","sibian.degussem@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(barak);
        userDao.insert(new Customer("cust02Johan", "Passwd123&", "Johan", "Vercauteren", theWhiteHouse));
        userDao.insert(new Customer("cust03Emma", "Passwd123&", "Emma", "Dupont", theWhiteHouse));
        userDao.insert(new Employee("tech01Donald", "Passwd123&", "Donald", "Trump", "Stationstraat 56", "092548736", "donald.trump@hogent.be", EmployeeRole.TECHNICIAN));
        userDao.insert(new Employee("supman01John", "Passwd123&", "John", "Smiths", "Stationstraat 34", "093504816", "john.smiths@hogent.be", EmployeeRole.SUPPORT_MANAGER));
        
        userDao.commitTransaction();
    }
}
