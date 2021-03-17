package domain;

import java.security.SecureRandom;
import java.time.LocalDate;

import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.enums.Timestamp;
import domain.enums.UserStatus;
import exceptions.InformationRequiredException;
import repository.GenericDao;
import repository.UserDaoJpa;

public class PopulateDB {
    public void run(UserDaoJpa userDao, GenericDao<ActemiumContractType> contractTypeDao) throws InformationRequiredException {
    	System.out.println("DBTester - creating and persisting multiple user objects");
        userDao.startTransaction();

        ActemiumCompany theWhiteHouse = new ActemiumCompany("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111");
        ActemiumCompany naessensNV = new ActemiumCompany("Construct Willy Naessens", "Belgium", "9700 Oudenaarde", "Bedrijvenpark Coupure 15", "055 61 98 19");
        ActemiumCompany google = new ActemiumCompany("Google", "United States", "Mountain View, CA 94043", "1600 Amphitheatre Parkway", "+1-650-253-0000");
        ActemiumCompany microsoft = new ActemiumCompany("Microsoft", "United States", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080");
        ActemiumCompany facebook = new ActemiumCompany("Facebook", "United States", "Menlo Park, CA 94025", "1 Hacker Way", "+1-650-308-7300");
        ActemiumCompany amazon = new ActemiumCompany("Amazon", "United States", "Seattle, WA 98109-5210", "410 Terry Avenue North", "+1-206-266-1000");
        ActemiumCompany tesla = new ActemiumCompany("Tesla", "United States", "Palo Alto, CA 94304", "3500 Deer Creek Road", "+31 20 365 0008");

        ActemiumContractType bCtype = new ActemiumContractType("BasicSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
        ActemiumContractType bCtype02 = new ActemiumContractType("BasicEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
        ActemiumContractType cCtype = new ActemiumContractType("BasicAppSupport", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.WORKINGHOURS, 5, 2, 1299.99);
        ActemiumContractType dCtype = new ActemiumContractType("FullEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.ALWAYS, 4, 1, 1999.99);
        ActemiumContractType eCtype = new ActemiumContractType("FullPhoneSupport", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.ALWAYS, 4, 1, 2999.99);
        ActemiumContractType fCtype = new ActemiumContractType("FullAppSupport", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.ALWAYS, 4, 1, 2299.99);
        ActemiumContractType gCtype = new ActemiumContractType("BasicAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 2, 2999.99);
        ActemiumContractType hCtype = new ActemiumContractType("FullAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 3999.99);
        ActemiumContractType jCtype = new ActemiumContractType("AmazonSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
        ActemiumContractType jCtype02 = new ActemiumContractType("MonopolySupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 6969.69);
        ActemiumContractType pCtype = new ActemiumContractType("BasicPhoneSupport", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.WORKINGHOURS, 5, 2, 1999.99);

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
        ActemiumEmployee tech = new ActemiumEmployee.EmployeeBuilder()
                .username("Technician")
                .password("Passwd123&")
                .firstName("Joe")
                .lastName("Biden")
                .address("Overwale 42")
                .phoneNumber("091354864")
                .emailAddress("thomas.dirven@hogent.be")
                .role(EmployeeRole.TECHNICIAN)
                .build();

        //("Technician", "Passwd123&", "Joe", "Biden","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN);
        ActemiumEmployee tech2 = new ActemiumEmployee.EmployeeBuilder()
                .username("Technician")
                .password("Passwd123&")
                .firstName("Donald")
                .lastName("Trump")
                .address("Overwale 42")
                .phoneNumber("091354864")
                .emailAddress("thomas.dirven@hogent.be")
                .role(EmployeeRole.TECHNICIAN)
                .build();
        //ActemiumEmployee tech2 = new ActemiumEmployee("Technician", "Passwd123&", "Donald", "Trump","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN);

        userDao.insert(tech);
        userDao.insert(tech2);
        ActemiumTicket ticket01 = new ActemiumTicket.TicketBuiler()
                                                    .ticketPriority(TicketPriority.P1)
                                                    .ticketType(TicketType.DATABASE)
                                                    .title("Ticket1")
                                                    .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                                                    .customer(barak)
                                                    .build();

        ActemiumTicket ticket02 = new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.HARDWARE)
                .title("Ticket2")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(jeff)
                .build();
        ActemiumTicket ticket03 = new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.INFRASTRUCTURE)
                .title("BBQ grill stopt working")
                .description("I was smoking these meats with sweet baby rays saus and suddenly my BBQ stopped working")
                .customer(mark)
                .build();
        ActemiumTicket ticket04 = new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.NETWORK)
                .title("Microchips in vaccine not working")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(bill)
                .build();
        ActemiumTicket ticket05 = new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.SOFTWARE)
                .title("Ticket5")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(larry)
                .build();
        ActemiumTicket ticket06 = new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.SOFTWARE)
                .title("Ticket6")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(elon)
                .build();

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
        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("Admin123")
                .password("Passwd123&")
                .firstName("Admin")
                .lastName("Administrator")
                .address("Overwale 42")
                .phoneNumber("091354864")
                .emailAddress("thomas.dirven@hogent.be")
                .role(EmployeeRole.ADMINISTRATOR)
                .build());
        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("Sup123")
                .password("Passwd123&")
                .firstName("Sup")
                .lastName("Support manager")
                .address("Overwale 42")
                .phoneNumber("091354864")
                .emailAddress("thomas.dirven@hogent.be")
                .role(EmployeeRole.SUPPORT_MANAGER)
                .build());
        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("Tech123")
                .password("Passwd123&")
                .firstName("tech")
                .lastName("technician")
                .address("Overwale 42")
                .phoneNumber("091354864")
                .emailAddress("thomas.dirven@hogent.be")
                .role(EmployeeRole.TECHNICIAN)
                .build());
        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("thomas123")
                .password("Passwd123&")
                .firstName("Thomas")
                .lastName("Dirven")
                .address("Overwale 42")
                .phoneNumber("091354864")
                .emailAddress("thomas.dirven@hogent.be")
                .role(EmployeeRole.ADMINISTRATOR)
                .build());

        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("isaac123")
                .password("Passwd123&")
                .firstName("Isaac")
                .lastName("Bauters")
                .address("Kerstraat 71")
                .phoneNumber("091354864")
                .emailAddress("isaac.bauters@hogent.be")
                .role(EmployeeRole.ADMINISTRATOR)
                .build());
        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("florian123")
                .password("Passwd123&")
                .firstName("Florian")
                .lastName("Goossens")
                .address("Groensstraat 103")
                .phoneNumber("091354864")
                .emailAddress("florian.goossens@hogent.be")
                .role(EmployeeRole.ADMINISTRATOR)
                .build());
        userDao.insert( new ActemiumEmployee.EmployeeBuilder()
                .username("SibianDG")
                .password("Passwd123&")
                .firstName("Sibian")
                .lastName("De Gussem")
                .address("Hoogstraat 89")
                .phoneNumber("091354864")
                .emailAddress("sibian.degussem@hogent.be")
                .role(EmployeeRole.ADMINISTRATOR)
                .build());

        //userDao.insert(new ActemiumEmployee("Admin123", "Passwd123&", "Admin", "Administrator","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        //userDao.insert(new ActemiumEmployee("Sup123", "Passwd123&", "Sup", "Support manager","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.SUPPORT_MANAGER));
        //userDao.insert(new ActemiumEmployee("Tech123", "Passwd123&", "tech", "technician","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.TECHNICIAN));
        //userDao.insert(new ActemiumEmployee("thomas123", "Passwd123&", "Thomas", "Dirven","Overwale 42","091354864","thomas.dirven@hogent.be", EmployeeRole.ADMINISTRATOR));
        //userDao.insert(new ActemiumEmployee("isaac123", "Passwd123&", "Isaac", "Bauters","Kerstraat 71","094812384","isaac.bauters@hogent.be", EmployeeRole.ADMINISTRATOR));
        //userDao.insert(new ActemiumEmployee("florian123", "Passwd123&", "Florian", "Goossens","Groensstraat 103","096248753","florian.goossens@hogent.be", EmployeeRole.ADMINISTRATOR));
        //userDao.insert(new ActemiumEmployee("sibianDG", "Passwd123&", "Sibian", "De Gussem","Hoogstraat 89","092564812","sibian.degussem@hogent.be", EmployeeRole.ADMINISTRATOR));
        ActemiumEmployee badguy = new ActemiumEmployee.EmployeeBuilder()
                .username("badGuy")
                .password("Passwd123&")
                .firstName("bad")
                .lastName("guy")
                .address("Hoogstraat 89")
                .phoneNumber("091354864")
                .emailAddress("bad.guy@hogent.be")
                .role(EmployeeRole.SUPPORT_MANAGER)
                .build();
        ActemiumEmployee inactive = new ActemiumEmployee.EmployeeBuilder()
                .username("fired")
                .password("Passwd123&")
                .firstName("bad")
                .lastName("guy")
                .address("Hoogstraat 89")
                .phoneNumber("091354864")
                .emailAddress("bad.guy@hogent.be")
                .role(EmployeeRole.SUPPORT_MANAGER)
                .build();
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
        userDao.insert(
                new ActemiumEmployee.EmployeeBuilder()
                        .username("tech01Donald")
                        .password("Passwd123&")
                        .firstName("Bob")
                        .lastName("The Builder")
                        .address("Stationstraat 56")
                        .phoneNumber("092548736")
                        .emailAddress("bob.thebuilder@hogent.be")
                        .role(EmployeeRole.TECHNICIAN)
                        .build());
        userDao.insert(
                new ActemiumEmployee.EmployeeBuilder()
                        .username("supman01John")
                        .password("Passwd123&")
                        .firstName("John")
                        .lastName("Smiths")
                        .address("Stationstraat 34")
                        .phoneNumber("09254875536")
                        .emailAddress("john.smiths@hogent.be")
                        .role(EmployeeRole.SUPPORT_MANAGER)
                        .build());
                //new ActemiumEmployee("tech01Donald", "Passwd123&", "Bob", "The Builder", "Stationstraat 56", "092548736", "donald.trump@hogent.be", EmployeeRole.TECHNICIAN));
        //userDao.insert(new ActemiumEmployee("supman01John", "Passwd123&", "John", "Smiths", "Stationstraat 34", "093504816", "john.smiths@hogent.be", EmployeeRole.SUPPORT_MANAGER));

        //String name, String country, String city, String address, String phoneNumber
        ActemiumCompany vatican = new ActemiumCompany("Vatican", "Vatican", "Vatican City", "00120 Vatican City", "666");
        ActemiumCustomer pope = new ActemiumCustomer("PopeFrancis", "Passwd123&", "Jorge Mario", "Bergoglio", vatican);
        SecureRandom randomGen = new SecureRandom();
        TicketPriority[] prios = TicketPriority.values();
        TicketType[] types = TicketType.values() ;

        TicketStatus[] status = TicketStatus.values();

        for (int i = 0; i < 20; i++) {
            ActemiumTicket t = new ActemiumTicket.TicketBuiler()
                                                    .ticketPriority(prios[randomGen.nextInt(3)])
                                                    .ticketType(types[randomGen.nextInt(types.length)])
                                                    .title("TitleRandom"+i)
                                                    .description("Description"+i)
                                                    .customer(bill)
                                                    .remarks("Remark"+i)
                                                    .attachments("screenshot"+i+".png")
                                                    .build();
            t.setStatus(status[randomGen.nextInt(status.length)]);
            mark.addTicket(t);
        }
        userDao.insert(bill);

        userDao.commitTransaction();

        contractTypeDao.startTransaction();
        contractTypeDao.insert(new ActemiumContractType("ExperimentalSupport", ContractTypeStatus.INACTIVE, true, false, true, Timestamp.ALWAYS, 4, 1, 2999.99));
		contractTypeDao.commitTransaction();
    }

}
