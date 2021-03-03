package domain;

import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.UserStatus;
import repository.UserDaoJpa;

import java.security.SecureRandom;
import java.util.ArrayList;

public class PopulateDB {
    public void run(UserDaoJpa userDao) {
    	System.out.println("DBTester - creating and persisting multiple user objects");        
        userDao.startTransaction();
        
        Company theWhiteHouse = new Company("The White House", "America 420", "911");
        Customer barak = new Customer("cust01Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        Customer barak2 = new Customer("cust02Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        Customer barak3 = new Customer("cust03Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        Customer barak4 = new Customer("cust04Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        Customer barak5 = new Customer("cust05Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        barak4.setStatus(UserStatus.BLOCKED);
        barak5.setStatus(UserStatus.INACTIVE);
        Employee tech = new Employee("Technician", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN);
        Employee tech2 = new Employee("Technician", "Passwd123&", "Donald", "Trump","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN);

        userDao.insert(tech);
        userDao.insert(tech2);
        ActemiumTicket ticket01 = new ActemiumTicket(TicketPriority.P1, "Remove Trump From White House", "I forgot my diary in the white house, Trump doesn't let me in to retriev it", barak);
        ActemiumTicket ticket02 = new ActemiumTicket(TicketPriority.P1, "Remove Biden From White House", "I forgot my diary in the white house, Trump doesn't let me in to retriev it", barak);
        ActemiumTicket ticket03 = new ActemiumTicket(TicketPriority.P1, "Get Trump back", "I forgot my diary in the white house, Trump doesn't let me in to retriev it", barak);
        ActemiumTicket ticket04 = new ActemiumTicket(TicketPriority.P1, "Make America Great Again", "I forgot my diary in the white house, Trump doesn't let me in to retriev it", barak);
        ActemiumTicket ticket05 = new ActemiumTicket(TicketPriority.P1, "Corona is a HOAX", "I forgot my diary in the white house, Trump doesn't let me in to retriev it", barak);
        barak.addTicket(ticket01);
        barak2.addTicket(ticket02);
        barak3.addTicket(ticket03);
        barak4.addTicket(ticket04);
        barak5.addTicket(ticket05);
        ticket01.addTechnician(tech);
        ticket02.addTechnician(tech);
        ticket03.addTechnician(tech);
        ticket04.addTechnician(tech2);
        ticket05.addTechnician(tech2);
        userDao.insert(new Employee("Admin123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("Tech123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN));
        userDao.insert(new Employee("thomas123", "Passwd123&", "Thomas", "Dirven","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("isaac123", "Passwd123&", "Isaac", "Bauters","Kerstraat 71","094812384","isaac.bauters@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("florian123", "Passwd123&", "Florian", "Goossens","Groensstraat 103","096248753","florian.goossens@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new Employee("sibianDG", "Passwd123&", "Sibian", "De Gussem","Hoogstraat 89","092564812","sibian.degussem@hogent.be", EmployeeRole.ADMINISTRATOR));
        Employee badguy = new Employee("badGuy", "Passwd123&", "bad", "guy","Hoogstraat 89","092564812","bad.guy@hogent.be", EmployeeRole.SUPPORT_MANAGER);
        Employee inactive = new Employee("fired", "Passwd123&", "bad", "guy","Hoogstraat 89","092564812","bad.guy@hogent.be", EmployeeRole.SUPPORT_MANAGER);
        badguy.setStatus(UserStatus.BLOCKED);
        inactive.setStatus(UserStatus.INACTIVE);
        userDao.insert(badguy);
        userDao.insert(inactive);
        userDao.insert(barak);
        userDao.insert(barak2);
        userDao.insert(barak3);
        userDao.insert(barak4);
        userDao.insert(barak5);
        userDao.insert(new Customer("cust02Johan", "Passwd123&", "Johan", "Vercauteren", theWhiteHouse));
        userDao.insert(new Customer("cust03Emma", "Passwd123&", "Emma", "Dupont", theWhiteHouse));
        userDao.insert(new Employee("tech01Donald", "Passwd123&", "Donald", "Trump", "Stationstraat 56", "092548736", "donald.trump@hogent.be", EmployeeRole.TECHNICIAN));
        userDao.insert(new Employee("supman01John", "Passwd123&", "John", "Smiths", "Stationstraat 34", "093504816", "john.smiths@hogent.be", EmployeeRole.SUPPORT_MANAGER));

        Company vatican = new Company("Vatican", "00120 Vatican City", "666");
        Customer pope = new Customer("PopeFrancis", "Passwd123&", "Jorge Mario", "Bergoglio", vatican);
        SecureRandom randomGen = new SecureRandom();
        TicketPriority[] prios = TicketPriority.values();

        for (int i = 0; i < 10; i++) {
            ActemiumTicket t = new ActemiumTicket(prios[randomGen.nextInt(3)], "Title"+i, "Description"+i, pope, "Remark"+i, "screenshot"+i+".png");
            pope.addTicket(t);
        }
        userDao.insert(pope);


        userDao.commitTransaction();
    }
}
