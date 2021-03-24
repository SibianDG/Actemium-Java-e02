package tests;

import java.time.LocalDate;
import java.util.stream.Stream;

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
import exceptions.InformationRequiredException;

public class CompanyTest implements Attributes {
    private ActemiumCompany google ;

    private ActemiumCustomer cust;

    public CompanyTest() {
    }

    public void initializeAttributes(){
        try {
            google = new ActemiumCompany.CompanyBuilder()
                    .name("Google")
                    .country("United States")
                    .city("Mountain View, CA 94043")
                    .address("1600 Amphitheatre Parkway")
                    .phoneNumber("+1-650-253-0000")
                    .build();
            cust = new ActemiumCustomer.CustomerBuilder()
                    .username("customer123")
                    .password("PassWd123&")
                    .firstName("John")
                    .lastName("Smith")
                    .company(google)
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }
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
    public void createCompany_correctdateOfCreation() {
        ActemiumCompany google;
        try {
            google = new ActemiumCompany.CompanyBuilder()
                    .name("Google")
                    .country("United States")
                    .city("Mountain View, CA 94043")
                    .address("1600 Amphitheatre Parkway")
                    .phoneNumber("+1-650-253-0000")
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }

        Assertions.assertEquals(LocalDate.now(), google.getRegistrationDate());
    }

    @Test
    public void addActemiumTicket_CompanyContainsTicket() {
        ActemiumCompany facebook;
        try {
            facebook = new ActemiumCompany.CompanyBuilder()
                    .name("Facebook")
                    .country("United States")
                    .city("Menlo Park, CA 94025")
                    .address("1 Hacker Way")
                    .phoneNumber("+1-650-308-7300")
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }

    	ActemiumTicket ticket = getActemiumTicket();
    	facebook.addActemiumTicket(ticket);
    	Assertions.assertEquals(ticket, facebook.getActemiumTickets().get(0));
    }

    @Test
    public void addActemiumTicket_CompanyContainsMultipleTickets() {
        initializeAttributes();
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket;
            try {
                 ticket = new ActemiumTicket.TicketBuilder()
                        .ticketPriority(TicketPriority.P1)
                        .ticketType(TicketType.SOFTWARE)
                        .title("Ticket"+i)
                        .description("Cannot print labels")
                        .customer(cust)
                        .build();
            } catch (InformationRequiredException e) {
                throw new IllegalArgumentException("Problem with initialize variables before test.");
            }

            google.addActemiumTicket(ticket);
        }
        Assertions.assertEquals(MAX_NUMBER, google.getActemiumTickets().size());
    }

    // Customer is the contactPerson of a company
    // Company is the "real" Customer, but for ease of naming
    // we will use the name Customer for contactPerson
    @Test
    public void addContactPerson_CompanyContainsContactPerson() {
        initializeAttributes();

        ActemiumCompany facebook;
        try {
            facebook = new ActemiumCompany.CompanyBuilder()
                    .name("Facebook")
                    .country("United States")
                    .city("Menlo Park, CA 94025")
                    .address("1 Hacker Way")
                    .phoneNumber("+1-650-308-7300")
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }

    	facebook.addContactPerson(cust);
        Assertions.assertEquals(cust, facebook.getContactPersons().get(0));
    }

    @Test
    public void addContactPerson_CompanyContainsMultipleContactPerson() {
        initializeAttributes();

        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumCustomer contactPerson;

            try {
                contactPerson = new ActemiumCustomer.CustomerBuilder()
                        .username("customer"+i)
                        .password("PassWd123&")
                        .firstName("John")
                        .lastName("Smith")
                        .company(google)
                        .build();

            } catch (InformationRequiredException e) {
                throw new IllegalArgumentException("Problem with initialize variables before test.");
            }
            google.addContactPerson(contactPerson);

        }
        Assertions.assertEquals(MAX_NUMBER, google.getContactPersons().size());
    }

    @Override
    public ActemiumTicket getActemiumTicket() {
        initializeAttributes();

        try {
            return new ActemiumTicket.TicketBuilder()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Printer Broken")
                    .description("Cannot print labels")
                    .customer(cust)
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }
    }
}
