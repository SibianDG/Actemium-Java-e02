package tests;

import java.time.LocalDate;
import java.util.stream.Stream;

import domain.UserModel;
import exceptions.InformationRequiredException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;

public class CompanyTest implements Attributes {
    private ActemiumCompany google = new ActemiumCompany.CompanyBuilder()
            .name("Google")
            .country("United States")
            .city("Mountain View, CA 94043")
            .address("1600 Amphitheatre Parkway")
            .phoneNumber("+1-650-253-0000")
            .build();

    private final ActemiumCustomer cust = new ActemiumCustomer.CustomerBuilder()
            .username("customer123")
            .password("PassWd123&")
            .firstName("John")
            .lastName("Smith")
            .company(google)
            .build();

    public CompanyTest() throws InformationRequiredException {
    }

    private static Stream<Arguments> validCompanyAttributes() {
        return Stream.of(
                //names
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111"),
                Arguments.of("Microsoft", "United States", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("M1-cr0s_ft", "United States", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", "3500 Deer Creek Road", "+31 20 365 0008"),

                //addresses
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111"),
                Arguments.of("Microsoft", "United States", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("Construct Willy Naessens", "Belgium", "9700 Oudenaarde", "Bedrijvenpark Coupure 15", "055 61 98 19"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", "3500 Deer Creek Road", "+31 20 365 0008"),

                //phonenumber
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111"),
                Arguments.of("Microsoft", "United States", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("Construct Willy Naessens", "Belgium", "9700 Oudenaarde", "Bedrijvenpark Coupure 15", "055 61 98 19"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", "3500 Deer Creek Road", "+31 20 365 0008")
        );
    }

    private static Stream<Arguments> invalidCompanyAttributes() {
        return Stream.of(
                //null
        		Arguments.of(null, "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111"),
                Arguments.of("Microsoft", null, "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("Construct Willy Naessens", "Belgium", null, "Bedrijvenpark Coupure 15", "055 61 98 19"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", null, "+31 20 365 0008"),
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", null),

                //empty
                Arguments.of("", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111"),
                Arguments.of("Microsoft", "", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("Construct Willy Naessens", "Belgium", "", "Bedrijvenpark Coupure 15", "055 61 98 19"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", "", "+31 20 365 0008"),
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", ""),

                //blank
                Arguments.of("   ", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "+1-202-456-1111"),
                Arguments.of("Microsoft", "    ", "Redmond, WA 98052", "1 Microsoft Way", "+1-425-882-8080"),
                Arguments.of("Construct Willy Naessens", "Belgium", "    ", "Bedrijvenpark Coupure 15", "055 61 98 19"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", "      ", "+31 20 365 0008"),
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "  "),

                //TODO
                //bad phonenumber
                Arguments.of("The White House", "United States", "Washington, D.C. 20500", "1600 Pennsylvania Avenue NW", "phoneNumber"),
                Arguments.of("Microsoft", "United States", "Redmond, WA 98052", "1 Microsoft Way", "phoneNumber"),
                Arguments.of("Construct Willy Naessens", "Belgium", "9700 Oudenaarde", "Bedrijvenpark Coupure 15", "phoneNumber"),
                Arguments.of("Tesla, Inc.", "United States", "Palo Alto, CA 94304", "3500 Deer Creek Road", "phoneNumber")
        );
    }

    @ParameterizedTest
    @MethodSource("validCompanyAttributes")
    public void createCompany_ValidCompanyAttributes_DoesNotThrowException(String companyName,
			String companyCountry, String companyCity, String companyAddress, String companyPhone) {

        Assertions.assertDoesNotThrow(() -> new ActemiumCompany.CompanyBuilder()
                .name(companyName)
                .country(companyCountry)
                .city(companyCity)
                .address(companyAddress)
                .phoneNumber(companyPhone)
                .build());
    }

    @ParameterizedTest
    @MethodSource("invalidCompanyAttributes")
    public void createActemiumTicket_InValidAttributes04_ThrowsIllegalArgumentException(String companyName,
			String companyCountry, String companyCity, String companyAddress, String companyPhone) {
        Assertions.assertThrows(InformationRequiredException.class,
                () -> new ActemiumCompany.CompanyBuilder()
                        .name(companyName)
                        .country(companyCountry)
                        .city(companyCity)
                        .address(companyAddress)
                        .phoneNumber(companyPhone)
                        .build());
    }

    @Test
    public void createCompany_correctdateOfCreation() throws InformationRequiredException {
        ActemiumCompany google = new ActemiumCompany.CompanyBuilder()
                .name("Google")
                .country("United States")
                .city("Mountain View, CA 94043")
                .address("1600 Amphitheatre Parkway")
                .phoneNumber("+1-650-253-0000")
                .build();

        Assertions.assertEquals(LocalDate.now(), google.getRegistrationDate());
    }

    @Test
    public void addActemiumTicket_CompanyContainsTicket() throws InformationRequiredException {

    	ActemiumCompany facebook = new ActemiumCompany.CompanyBuilder()
                .name("Facebook")
                .country("United States")
                .city("Menlo Park, CA 94025")
                .address("1 Hacker Way")
                .phoneNumber("+1-650-308-7300")
                .build();
    	ActemiumTicket ticket = getActemiumTicket();
    	facebook.addActemiumTicket(ticket);
    	Assertions.assertEquals(ticket, facebook.getActemiumTickets().get(0));
    	// cannot use static company, this does not reset before each test
    	// you will not get the right ActemiumTicket
//    	company.addActemiumTicket(actemiumTicket);
//      Assertions.assertEquals(actemiumTicket, company.getActemiumTickets().get(0));
    }

    @Test
    public void addActemiumTicket_CompanyContainsMultipleTickets() throws InformationRequiredException {
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket = new ActemiumTicket.TicketBuiler()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Ticket"+i)
                    .description("Cannot print labels")
                    .customer(cust)
                    .build();
            google.addActemiumTicket(ticket);
        }
        Assertions.assertEquals(MAX_NUMBER, google.getActemiumTickets().size());
    }

    // Customer is the contactPerson of a company
    // Company is the "real" Customer, but for ease of naming
    // we will use the name Customer for contactPerson
    // Could be changed if we realy have to
    @Test
    public void addContactPerson_CompanyContainsContactPerson() throws InformationRequiredException {

    	ActemiumCompany facebook = new ActemiumCompany.CompanyBuilder()
                .name("Facebook")
                .country("United States")
                .city("Menlo Park, CA 94025")
                .address("1 Hacker Way")
                .phoneNumber("+1-650-308-7300")
                .build();
    	facebook.addContactPerson(cust);
        Assertions.assertEquals(cust, facebook.getContactPersons().get(0));
    }

    @Test
    public void addContactPerson_CompanyContainsMultipleContactPerson() throws InformationRequiredException {
        for (int i = 0; i < MAX_NUMBER; i++) {

        	ActemiumCustomer contactPerson = new ActemiumCustomer.CustomerBuilder()
                    .username("customer"+i)
                    .password("PassWd123&")
                    .firstName("John")
                    .lastName("Smith")
                    .company(google)
                    .build();
        	google.addContactPerson(contactPerson);
        }
        Assertions.assertEquals(MAX_NUMBER, google.getContactPersons().size());
    }

    @Override
    public ActemiumTicket getActemiumTicket() throws InformationRequiredException {
        return new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.SOFTWARE)
                .title("Printer Broken")
                .description("Cannot print labels")
                .customer(cust)
                .build();
    }
}
