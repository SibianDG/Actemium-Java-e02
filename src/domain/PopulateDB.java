package domain;

import domain.enums.*;
import repository.GenericDao;
import repository.UserDaoJpa;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;

public class PopulateDB {
    public void run(UserDaoJpa userDao, GenericDao<ActemiumContractType> contractTypeDao) {
    	System.out.println("DBTester - creating and persisting multiple user objects");        
        userDao.startTransaction();
        
        ActemiumCompany theWhiteHouse = new ActemiumCompany("The White House", "America 420", "911");
        ActemiumCompany naessensNV = new ActemiumCompany("Construct Willy Naessens", "Bedrijvenpark Coupure 15, 9700 Oudenaarde", "911");
        ActemiumCompany google = new ActemiumCompany("Google", "1600 Amphitheatre Parkway, Mountain View, California, United States", "240217996");
        ActemiumCompany microsoft = new ActemiumCompany("Microsoft", "America 420", "411048476");
        ActemiumCompany facebook = new ActemiumCompany("Facebook", "America 420", "039234181");
        ActemiumCompany amazon = new ActemiumCompany("Amazon", "America 420", "475116936");
        ActemiumCompany tesla = new ActemiumCompany("Tesla", "America 420", "892645557");
        
        ActemiumContractType bCtype = new ActemiumContractType("BasicSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
        ActemiumContractType bCtype02 = new ActemiumContractType("BasicEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
        ActemiumContractType cCtype = new ActemiumContractType("BasisAppSupport", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.WORKINGHOURS, 5, 2, 1299.99);
        ActemiumContractType dCtype = new ActemiumContractType("FullEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.ALWAYS, 4, 1, 1999.99);
        ActemiumContractType eCtype = new ActemiumContractType("FullPhoneSupport", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.ALWAYS, 4, 1, 2999.99);
        ActemiumContractType fCtype = new ActemiumContractType("FullAppSupport", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.ALWAYS, 4, 1, 2299.99);
        ActemiumContractType gCtype = new ActemiumContractType("BasisAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 2, 2999.99);
        ActemiumContractType hCtype = new ActemiumContractType("FullAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 3999.99);
        ActemiumContractType jCtype = new ActemiumContractType("AmazonSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
        ActemiumContractType jCtype02 = new ActemiumContractType("MonopolySupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 6969.69);
        ActemiumContractType pCtype = new ActemiumContractType("BasisPhoneSupport", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.WORKINGHOURS, 5, 2, 1999.99);
        
        ActemiumCustomer barak = new ActemiumCustomer("cust01Barak", "Passwd123&", "Barak", "Obama", theWhiteHouse);
        ActemiumCustomer jeff = new ActemiumCustomer("cust02Jeff", "Passwd123&", "Jeff", "Bezos", amazon);
        ActemiumCustomer mark = new ActemiumCustomer("cust03Mark", "Passwd123&", "Mark", "Zuckerberg", facebook);
        ActemiumCustomer bill = new ActemiumCustomer("cust04Bill", "Passwd123&", "Bill", "Gates", microsoft);
        ActemiumCustomer larry = new ActemiumCustomer("cust05Larry", "Passwd123&", "Larry", "Page", google);
        ActemiumCustomer elon = new ActemiumCustomer("cust06Elon", "Passwd123&", "Elon", "Musk", tesla);        
        
        ActemiumContract bContract = new ActemiumContract(bCtype, barak, LocalDate.now().plusYears(1));
        ActemiumContract bContract02 = new ActemiumContract(bCtype02, barak, LocalDate.now().plusYears(1));
        ActemiumContract jContract = new ActemiumContract(jCtype, jeff, LocalDate.now().plusYears(3));
        ActemiumContract jContract02 = new ActemiumContract(jCtype02, jeff, LocalDate.now().plusYears(2));
        ActemiumContract mContract = new ActemiumContract(cCtype, mark, LocalDate.now().plusYears(1));
        ActemiumContract mContract02 = new ActemiumContract(gCtype, mark, LocalDate.now().plusYears(1));
        ActemiumContract gContract = new ActemiumContract(dCtype, bill, LocalDate.now().plusYears(1));
        ActemiumContract gContract02 = new ActemiumContract(dCtype, bill, LocalDate.now().plusYears(1));
        ActemiumContract lContract = new ActemiumContract(eCtype, larry, LocalDate.now().plusYears(3));
        ActemiumContract lContract02 = new ActemiumContract(hCtype, larry, LocalDate.now().plusYears(3));
        ActemiumContract eContract = new ActemiumContract(fCtype, elon, LocalDate.now().plusYears(2));
        ActemiumContract eContract02 = new ActemiumContract(pCtype, elon, LocalDate.now().plusYears(2));        
        
        barak.addContract(bContract);
        barak.addContract(bContract02);
        
        jeff.addContract(jContract);
        jeff.addContract(jContract02);
        
        mark.addContract(mContract);
        mark.addContract(mContract02);
        
        bill.addContract(gContract);
        bill.addContract(gContract02);
        
        larry.addContract(lContract);
        larry.addContract(lContract02);
        
        elon.addContract(eContract);
        elon.addContract(eContract02);
        
        mark.setStatus(UserStatus.BLOCKED);
        larry.setStatus(UserStatus.INACTIVE);
        ActemiumEmployee tech = new ActemiumEmployee("Technician", "Passwd123&", "Joe", "Biden","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN);
        ActemiumEmployee tech2 = new ActemiumEmployee("Technician", "Passwd123&", "Donald", "Trump","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN);

        userDao.insert(tech);
        userDao.insert(tech2);
        ActemiumTicket ticket01 = new ActemiumTicket(TicketPriority.P1,TicketType.DATABASE, "Ticket1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", barak);
        ActemiumTicket ticket02 = new ActemiumTicket(TicketPriority.P1, TicketType.HARDWARE,"Ticket2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", jeff);
        ActemiumTicket ticket03 = new ActemiumTicket(TicketPriority.P1, TicketType.INFRASTRUCTURE, "BBQ grill stopt working", "I was smoking these meats with sweet baby rays saus and suddenly my BBQ stopped working", mark);
        ActemiumTicket ticket04 = new ActemiumTicket(TicketPriority.P1, TicketType.NETWORK, "Microchips in vaccine not working", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", bill);
        ActemiumTicket ticket05 = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE, "Ticket5", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", larry);
        ActemiumTicket ticket06 = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE, "Ticket6", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", elon);
        ticket05.setStatus(TicketStatus.COMPLETED);
        barak.addTicket(ticket01);
        jeff.addTicket(ticket02);
        mark.addTicket(ticket03);
        bill.addTicket(ticket04);
        larry.addTicket(ticket05);
        elon.addTicket(ticket06);
        ticket01.addTechnician(tech);
        ticket02.addTechnician(tech);
        ticket03.addTechnician(tech);
        ticket04.addTechnician(tech2);
        ticket05.addTechnician(tech2);
        ticket06.addTechnician(tech);
        ticket06.addTechnician(tech2);
        userDao.insert(new ActemiumEmployee("Admin123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new ActemiumEmployee("Sup123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.SUPPORT_MANAGER));
        userDao.insert(new ActemiumEmployee("Tech123", "Passwd123&", "Johan", "Van Schoor","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN));
        userDao.insert(new ActemiumEmployee("thomas123", "Passwd123&", "Thomas", "Dirven","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new ActemiumEmployee("isaac123", "Passwd123&", "Isaac", "Bauters","Kerstraat 71","094812384","isaac.bauters@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new ActemiumEmployee("florian123", "Passwd123&", "Florian", "Goossens","Groensstraat 103","096248753","florian.goossens@hogent.be", EmployeeRole.ADMINISTRATOR));
        userDao.insert(new ActemiumEmployee("sibianDG", "Passwd123&", "Sibian", "De Gussem","Hoogstraat 89","092564812","sibian.degussem@hogent.be", EmployeeRole.ADMINISTRATOR));
        ActemiumEmployee badguy = new ActemiumEmployee("badGuy", "Passwd123&", "bad", "guy","Hoogstraat 89","092564812","bad.guy@hogent.be", EmployeeRole.SUPPORT_MANAGER);
        ActemiumEmployee inactive = new ActemiumEmployee("fired", "Passwd123&", "bad", "guy","Hoogstraat 89","092564812","bad.guy@hogent.be", EmployeeRole.SUPPORT_MANAGER);
        badguy.setStatus(UserStatus.BLOCKED);
        inactive.setStatus(UserStatus.INACTIVE);
        userDao.insert(badguy);
        userDao.insert(inactive);
        userDao.insert(barak);
        userDao.insert(jeff);
        userDao.insert(mark);
        userDao.insert(bill);
        userDao.insert(larry);
        userDao.insert(elon);
        userDao.insert(new ActemiumCustomer("cust02Johan", "Passwd123&", "Willy", "Naessens", naessensNV));
        userDao.insert(new ActemiumEmployee("tech01Donald", "Passwd123&", "Donald", "Trump", "Stationstraat 56", "092548736", "donald.trump@hogent.be", EmployeeRole.TECHNICIAN));
        userDao.insert(new ActemiumEmployee("supman01John", "Passwd123&", "John", "Smiths", "Stationstraat 34", "093504816", "john.smiths@hogent.be", EmployeeRole.SUPPORT_MANAGER));

        ActemiumCompany vatican = new ActemiumCompany("Vatican", "00120 Vatican City", "666");
        ActemiumCustomer pope = new ActemiumCustomer("PopeFrancis", "Passwd123&", "Jorge Mario", "Bergoglio", vatican);
        SecureRandom randomGen = new SecureRandom();
        TicketPriority[] prios = TicketPriority.values();
        TicketType[] types = TicketType.values() ;
        
        TicketStatus[] status = TicketStatus.values();
        
        for (int i = 0; i < 20; i++) {
            ActemiumTicket t = new ActemiumTicket(prios[randomGen.nextInt(3)], types[randomGen.nextInt(types.length)], "Title"+i, "Description"+i, pope, "Remark"+i, "screenshot"+i+".png");
            t.setStatus(status[randomGen.nextInt(status.length)]);
            pope.addTicket(t);
        }
        userDao.insert(pope);
        userDao.commitTransaction();

        contractTypeDao.startTransaction();
        contractTypeDao.insert(new ActemiumContractType("ExperimentalSupport", ContractTypeStatus.INACTIVE, true, false, true, Timestamp.ALWAYS, 4, 1, 2999.99));
		contractTypeDao.commitTransaction();

    }

}
