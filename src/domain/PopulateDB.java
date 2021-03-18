package domain;

import java.security.SecureRandom;
import java.time.LocalDate;

import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.enums.Timestamp;
import domain.enums.UserStatus;
import exceptions.InformationRequiredException;
import repository.GenericDao;
import repository.UserDaoJpa;

public class PopulateDB {
    public void run(UserDaoJpa userDao, GenericDao<ActemiumContractType> contractTypeDao,
    		GenericDao<ActemiumKbItem> kbItemDao) throws InformationRequiredException {
    	System.out.println("DBTester - creating and persisting multiple user objects");
        userDao.startTransaction();


        ActemiumCompany theWhiteHouse = new ActemiumCompany.CompanyBuilder()
                .name("The White House")
                .country("United States")
                .city("Washington, D.C. 20500")
                .address("1600 Pennsylvania Avenue NW")
                .phoneNumber("+1-202-456-1111")
                .build();


        ActemiumCompany naessensNV = new ActemiumCompany.CompanyBuilder()
                .name("Construct Willy Naessens")
                .country("Belgium")
                .city("9700 Oudenaarde")
                .address("Bedrijvenpark Coupure 15")
                .phoneNumber("055 61 98 19")
                .build();

        ActemiumCompany google = new ActemiumCompany.CompanyBuilder()
                .name("Google")
                .country("United States")
                .city("Mountain View, CA 94043")
                .address("1600 Amphitheatre Parkway")
                .phoneNumber("+1-650-253-0000")
                .build();

        ActemiumCompany microsoft = new ActemiumCompany.CompanyBuilder()
                .name("Microsoft")
                .country("United States")
                .city("Redmond, WA 98052")
                .address("1 Microsoft Way")
                .phoneNumber("+1-425-882-8080")
                .build();

        ActemiumCompany facebook = new ActemiumCompany.CompanyBuilder()
                .name("Facebook")
                .country("United States")
                .city("Menlo Park, CA 94025")
                .address("1 Hacker Way")
                .phoneNumber("+1-650-308-7300")
                .build();
        new ActemiumCompany.CompanyBuilder()
                .name("Amazon")
                .country("United States")
                .city("Seattle, WA 98109-5210")
                .address("410 Terry Avenue North")
                .phoneNumber("+1-206-266-1000")
                .build();
        ActemiumCompany amazon = new ActemiumCompany.CompanyBuilder()
                .name("Amazon")
                .country("United States")
                .city("Seattle, WA 98109-5210")
                .address("410 Terry Avenue North")
                .phoneNumber("+1-206-266-1000")
                .build();
        ActemiumCompany tesla = new ActemiumCompany.CompanyBuilder()
                .name("Tesla")
                .country("United States")
                .city("Palo Alto, CA 94304")
                .address("3500 Deer Creek Road")
                .phoneNumber("+31 20 365 0008")
                .build();

        ActemiumContractType bCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("BasicSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(false)
                .hasApplication(false)
                .timestamp(Timestamp.WORKINGHOURS)
                .maxHandlingTime(5)
                .minThroughputTime(2)
                .price(999.99)
                .build();

        ActemiumContractType bCtype02 = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("BasicEmailSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(false)
                .hasApplication(false)
                .timestamp(Timestamp.WORKINGHOURS)
                .maxHandlingTime(5)
                .minThroughputTime(2)
                .price(999.99)
                .build();
        ActemiumContractType cCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("BasicAppSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(false)
                .hasPhone(false)
                .hasApplication(true)
                .timestamp(Timestamp.WORKINGHOURS)
                .maxHandlingTime(5)
                .minThroughputTime(2)
                .price(1299.99)
                .build();

        ActemiumContractType dCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("FullEmailSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(false)
                .hasApplication(false)
                .timestamp(Timestamp.ALWAYS)
                .maxHandlingTime(4)
                .minThroughputTime(1)
                .price(1999.99)
                .build();
        ActemiumContractType eCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("FullPhoneSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(false)
                .hasPhone(true)
                .hasApplication(false)
                .timestamp(Timestamp.ALWAYS)
                .maxHandlingTime(4)
                .minThroughputTime(1)
                .price(2999.99)
                .build();

        ActemiumContractType fCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("FullAppSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(false)
                .hasPhone(false)
                .hasApplication(true)
                .timestamp(Timestamp.ALWAYS)
                .maxHandlingTime(4)
                .minThroughputTime(1)
                .price(2299.99)
                .build();

        ActemiumContractType gCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("BasicAllSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(true)
                .hasApplication(true)
                .timestamp(Timestamp.WORKINGHOURS)
                .maxHandlingTime(4)
                .minThroughputTime(2)
                .price(2999.99)
                .build();

        ActemiumContractType hCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("FullAllSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(true)
                .hasApplication(true)
                .timestamp(Timestamp.ALWAYS)
                .maxHandlingTime(4)
                .minThroughputTime(2)
                .price(3999.99)
                .build();

        ActemiumContractType jCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("AmazonSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(false)
                .hasApplication(false)
                .timestamp(Timestamp.WORKINGHOURS)
                .maxHandlingTime(5)
                .minThroughputTime(2)
                .price(999.99)
                .build();

        ActemiumContractType jCtype02 = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("MonopolySupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(true)
                .hasPhone(true)
                .hasApplication(true)
                .timestamp(Timestamp.ALWAYS)
                .maxHandlingTime(3)
                .minThroughputTime(1)
                .price(6969.69)
                .build();

        ActemiumContractType pCtype = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("BasicPhoneSupport")
                .contractTypeStatus(ContractTypeStatus.ACTIVE)
                .hasEmail(false)
                .hasPhone(true)
                .hasApplication(false)
                .timestamp(Timestamp.WORKINGHOURS)
                .maxHandlingTime(5)
                .minThroughputTime(2)
                .price(1999.99)
                .build();



        ActemiumCustomer barak = new ActemiumCustomer.CustomerBuilder()
                .username("cust01Barak")
                .password("Passwd123&")
                .firstName("Barak")
                .lastName("Obama")
                .company(theWhiteHouse)
                .build();
        ActemiumCustomer jeff = new ActemiumCustomer.CustomerBuilder()
                .username("cust02Jeff")
                .password("Passwd123&")
                .firstName("Jeff")
                .lastName("Bezos")
                .company(amazon)
                .build();
        ActemiumCustomer mark = new ActemiumCustomer.CustomerBuilder()
                .username("cust03Mark")
                .password("Passwd123&")
                .firstName("Mark")
                .lastName("Zuckerberg")
                .company(facebook)
                .build();
        ActemiumCustomer bill = new ActemiumCustomer.CustomerBuilder()
                .username("cust04Bill")
                .password("Passwd123&")
                .firstName("Bill")
                .lastName("Gates")
                .company(microsoft)
                .build();
        ActemiumCustomer larry = new ActemiumCustomer.CustomerBuilder()
                .username("cust05Larry")
                .password("Passwd123&")
                .firstName("Larry")
                .lastName("Page")
                .company(google)
                .build();
        ActemiumCustomer elon = new ActemiumCustomer.CustomerBuilder()
                .username("cust06Elon")
                .password("Passwd123&")
                .firstName("Elon")
                .lastName("Musk")
                .company(tesla)
                .build();

        ActemiumContract bContract = new ActemiumContract.ContractBuilder()
                                                            .contractType(bCtype)
                                                            .customer(barak)
                                                            .endDate(LocalDate.now().plusYears(1))
                                                            .build();
        System.out.println("POPDB END"+bContract.getEndDate());
        ActemiumContract bContract02 = new ActemiumContract.ContractBuilder()
                .contractType(bCtype02)
                .customer(barak)
                .endDate(LocalDate.now().plusYears(1))
                .build();
        ActemiumContract jContract = new ActemiumContract.ContractBuilder()
                .contractType(jCtype)
                .customer(jeff)
                .endDate(LocalDate.now().plusYears(3))
                .build();
        ActemiumContract jContract02 = new ActemiumContract.ContractBuilder()
                .contractType(jCtype02)
                .customer(jeff)
                .endDate(LocalDate.now().plusYears(3))
                .build();

        ActemiumContract mContract = new ActemiumContract.ContractBuilder()
                .contractType(cCtype)
                .customer(mark)
                .endDate(LocalDate.now().plusYears(1))
                .build();
        ActemiumContract mContract02 = new ActemiumContract.ContractBuilder()
                .contractType(gCtype)
                .customer(mark)
                .endDate(LocalDate.now().plusYears(1))
                .build();
        ActemiumContract gContract = new ActemiumContract.ContractBuilder()
                .contractType(dCtype)
                .customer(bill)
                .endDate(LocalDate.now().plusYears(1))
                .build();

        ActemiumContract gContract02 = new ActemiumContract.ContractBuilder()
                .contractType(dCtype)
                .customer(bill)
                .endDate(LocalDate.now().plusYears(1))
                .build();
        ActemiumContract lContract = new ActemiumContract.ContractBuilder()
                .contractType(eCtype)
                .customer(bill)
                .endDate(LocalDate.now().plusYears(3))
                .build();
        ActemiumContract lContract02 = new ActemiumContract.ContractBuilder()
                .contractType(eCtype)
                .customer(larry)
                .endDate(LocalDate.now().plusYears(3))
                .build();

        ActemiumContract eContract = new ActemiumContract.ContractBuilder()
                .contractType(fCtype)
                .customer(elon)
                .endDate(LocalDate.now().plusYears(2))
                .build();
        ActemiumContract eContract02 = new ActemiumContract.ContractBuilder()
                .contractType(pCtype)
                .customer(elon)
                .endDate(LocalDate.now().plusYears(2))
                .build();

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
        ActemiumTicket ticket01 = new ActemiumTicket.TicketBuilder()
                                                    .ticketPriority(TicketPriority.P1)
                                                    .ticketType(TicketType.DATABASE)
                                                    .title("Ticket1")
                                                    .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                                                    .customer(barak)
                                                    .build();

        ActemiumTicket ticket02 = new ActemiumTicket.TicketBuilder()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.HARDWARE)
                .title("Ticket2")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(jeff)
                .build();
        ActemiumTicket ticket03 = new ActemiumTicket.TicketBuilder()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.INFRASTRUCTURE)
                .title("BBQ grill stopt working")
                .description("I was smoking these meats with sweet baby rays saus and suddenly my BBQ stopped working")
                .customer(mark)
                .build();
        ActemiumTicket ticket04 = new ActemiumTicket.TicketBuilder()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.NETWORK)
                .title("Microchips in vaccine not working")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(bill)
                .build();
        ActemiumTicket ticket05 = new ActemiumTicket.TicketBuilder()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.SOFTWARE)
                .title("Ticket5")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .customer(larry)
                .build();
        ActemiumTicket ticket06 = new ActemiumTicket.TicketBuilder()
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

        userDao.insert(new ActemiumCustomer.CustomerBuilder()
                .username("cust02Johan")
                .password("Passwd123&")
                .firstName("Willy")
                .lastName("Naessens")
                .company(naessensNV)
                .build());
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
        ActemiumCompany vatican = new ActemiumCompany.CompanyBuilder()
                .name("Vatican")
                .country("Vatican")
                .city("Vatican City")
                .address("00120 Vatican City")
                .phoneNumber("666")
                .build();


        ActemiumCustomer pope = new ActemiumCustomer.CustomerBuilder()
                .username("PopeFrancis")
                .password("Passwd123&")
                .firstName("Jorge")
                .lastName("Bergoglio")
                .company(vatican)
                .build();
        userDao.insert(pope);

        SecureRandom randomGen = new SecureRandom();
        TicketPriority[] prios = TicketPriority.values();
        TicketType[] types = TicketType.values() ;

        TicketStatus[] status = TicketStatus.values();

        for (int i = 0; i < 20; i++) {
            ActemiumTicket t = new ActemiumTicket.TicketBuilder()
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

        contractTypeDao.insert(new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName("ExperimentalSupport")
                .contractTypeStatus(ContractTypeStatus.INACTIVE)
                .hasEmail(true)
                .hasPhone(true)
                .hasApplication(true)
                .timestamp(Timestamp.ALWAYS)
                .maxHandlingTime(4)
                .minThroughputTime(1)
                .price(2999.99)
                .build());        
        
		contractTypeDao.commitTransaction();
		
		kbItemDao.startTransaction();
		
		kbItemDao.insert(new ActemiumKbItem.KbItemBuilder()
				.title("Test title")
				.type(KbItemType.DATABASE)
				.text("some sample text here")
				.build());
		
		
        KbItemType[] kbItemTypes = KbItemType.values();

        for (int i = 0; i < 20; i++) {
        	kbItemDao.insert(new ActemiumKbItem.KbItemBuilder()
                                    .title("Random KB article Title"+i)
                                    .type(kbItemTypes[randomGen.nextInt(kbItemTypes.length)])
                                    .text("Sample Text"+i)
                                    .build());
        }
		kbItemDao.commitTransaction();
		
    }

}
