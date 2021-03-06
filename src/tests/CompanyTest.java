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

public class CompanyTest implements Attributes {

    private static Stream<Arguments> validCompanyAttributes() {
        return Stream.of(
                //names
                Arguments.of("Actemium", "Mainway 99", "0470099874"),
                Arguments.of("Microsoft1", "Mainway 99", "0470099874"),
                Arguments.of("Google_", "Mainway 99", "0470099874"),
                Arguments.of("Apple_123", "Mainway 99", "0470099874"),

                //addresses
                Arguments.of("Actemium", "Mainway99", "0470099874"),
                Arguments.of("Actemium", "Mainwayù99", "0470099874"),
                Arguments.of("Actemium", "Mainwayé99", "0470099874"),
                Arguments.of("Actemium", "Mainway", "0470099874"),

                //phonenumber
                Arguments.of("Actemium", "Mainway", "047009987"),
                Arguments.of("Actemium", "Mainway", "047009987400"),
                Arguments.of("Actemium", "Mainway", "04/700-99-874"),
                Arguments.of("Actemium", "Mainway", "047 0099 874")
        );
    }

    private static Stream<Arguments> invalidCompanyAttributes() {
        return Stream.of(
                //null
                Arguments.of(null, "Mainway 99", "0470099874"),
                Arguments.of("Actemium", null, "0470099874"),
                Arguments.of("Actemium", "Mainway 99", null),

                //empty
                Arguments.of("", "Mainway 99", "0470099874"),
                Arguments.of("Actemium", "", "0470099874"),
                Arguments.of("Actemium", "Mainway 99", ""),

                //blank
                Arguments.of("   ", "Mainway 99", "0470099874"),
                Arguments.of("Actemium", "   ", "0470099874"),
                Arguments.of("Actemium", "Mainway 99", "   "),

                //bad phonenumber
                Arguments.of("Actemium", "Mainway", "04°7009987"),
                Arguments.of("Actemium", "Mainway", "04\"7009987"),
                Arguments.of("Actemium", "Mainway", "047af009987"),
                Arguments.of("Actemium", "Mainway", "047009(&)987")
        );
    }

    @ParameterizedTest
    @MethodSource("validCompanyAttributes")
    public void createCompany_ValidCompanyAttributes_DoesNotThrowException(String companyName, String address, String phonenumber) {
        Assertions.assertDoesNotThrow(() -> new ActemiumCompany(companyName, address, phonenumber));
    }

    @ParameterizedTest
    @MethodSource("invalidCompanyAttributes")
    public void createActemiumTicket_InValidAttributes04_ThrowsIllegalArgumentException(String companyName, String address, String phonenumber) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ActemiumCompany(companyName, address, phonenumber));
    }

    @Test
    public void createCompany_correctdateOfCreation() {
        Assertions.assertEquals(LocalDate.now(), company.getRegistrationDate());
    }

    @Test
    public void addActemiumTicket_CompanyContainsTicket() {
    	ActemiumCompany company = new ActemiumCompany("Actemium", "Mainway 99", "0470099874");
    	company.addActemiumTicket(actemiumTicket);
    	Assertions.assertEquals(actemiumTicket, company.getActemiumTickets().get(0));
    	// cannot use static company, this does not reset before each test
    	// you will not get the right ActemiumTicket
//    	company.addActemiumTicket(actemiumTicket);
//      Assertions.assertEquals(actemiumTicket, company.getActemiumTickets().get(0));
    }

    @Test
    public void addActemiumTicket_CompanyContainsMultipleTickets() {
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE,"Ticket"+i, "Cannot print labels", customer);
            company.addActemiumTicket(ticket);
        }
        Assertions.assertEquals(MAX_NUMBER, company.getActemiumTickets().size());
    }

    // Customer is the contactPerson of a company
    // Company is the "real" Customer, but for ease of naming
    // we will use the name Customer for contactPerson
    // Could be changed if we realy have to
    @Test
    public void addContactPerson_CompanyContainsContactPerson() {
    	ActemiumCompany company = new ActemiumCompany("Actemium", "Mainway 99", "0470099874");
        company.addContactPerson(customer);
        Assertions.assertEquals(customer, company.getContactPersons().get(0));
    }

    @Test
    public void addContactPerson_CompanyContainsMultipleContactPerson() {
        for (int i = 0; i < MAX_NUMBER; i++) {
        	ActemiumCustomer contactPerson = new ActemiumCustomer("customer"+i, "PassWd123&", "John", "Smith", company);
            company.addContactPerson(contactPerson);
        }
        Assertions.assertEquals(MAX_NUMBER, company.getContactPersons().size());
    }

}
