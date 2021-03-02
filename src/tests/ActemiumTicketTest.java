package tests;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumTicket;
import domain.Company;
import domain.Customer;
import domain.Employee;
import domain.EmployeeRole;
import domain.TicketPriority;

public class ActemiumTicketTest {
	
	private Employee technician = new Employee("jooKlein123", "PassWd123&", "Joost", "Klein", "Adress", "0470099874", "student@student.hogent.be", EmployeeRole.TECHNICIAN);
    private static Company theWhiteHouse = new Company("The White House", "America 420", "911");    
    private static Customer customer = new Customer("customer123", "PassWd123&", "John", "Smith", theWhiteHouse);
	private ActemiumTicket ticket01 = new ActemiumTicket(TicketPriority.P1, "Printer Broken", "Cannot print labels", customer);
	
    private static Stream<Arguments> validActemiumTicketAttributes04() {
        return Stream.of(
                Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels", customer),
                Arguments.of(TicketPriority.P2, "Printer Broken", "Cannot print labels", customer),
                Arguments.of(TicketPriority.P3, "Printer Broken", "Cannot print labels", customer)            
        		);
    }

    private static Stream<Arguments> invalidActemiumTicketAttributes04() {
        return Stream.of(
                // title: not null or empty or blank, but (special chars)? and digits are ok
        		Arguments.of(TicketPriority.P1, null, "Cannot print labels", customer),
                Arguments.of(TicketPriority.P1, "", "Cannot print labels", customer),
                Arguments.of(TicketPriority.P1, "   ", "Cannot print labels", customer),
                
                // description: not null or empty or blank, but special chars and digits are ok
                Arguments.of(TicketPriority.P1, "Printer Broken", null, customer),
                Arguments.of(TicketPriority.P1, "Printer Broken", "", customer),
                Arguments.of(TicketPriority.P1, "Printer Broken", "    ", customer),
                                
                // customer: not null
                Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels", null)
        		);
    }

    private static Stream<Arguments> validActemiumTicketAttributes06() {
    	return Stream.of(
    			Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels", customer, null, null),
    			Arguments.of(TicketPriority.P2, "Printer Broken", "Cannot print labels", customer, "", null),
    			Arguments.of(TicketPriority.P3, "Printer Broken", "Cannot print labels", customer, null, ""),
    			Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels", customer, "Remarks", "Attachment"),
    			Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels 123", customer, "Remarks 123", "Attachment 123"),
    			Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels 123", customer, "", ""),
    			Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels 123", customer, "    ", "    ")                
    			);
    }
    
    private static Stream<Arguments> invalidActemiumTicketAttributes06() {
    	return Stream.of(
    			// title: not null or empty or blank, but (special chars)? and digits are ok
    			Arguments.of(TicketPriority.P1, null, "Cannot print labels", customer, null, null),
    			Arguments.of(TicketPriority.P1, "", "Cannot print labels", customer, null, null),
    			Arguments.of(TicketPriority.P1, "   ", "Cannot print labels", customer, null, null),
    			
    			// description: not null or empty or blank, but special chars and digits are ok
    			Arguments.of(TicketPriority.P1, "Printer Broken", null, customer, null, null),
    			Arguments.of(TicketPriority.P1, "Printer Broken", "", customer, null, null),
    			Arguments.of(TicketPriority.P1, "Printer Broken", "    ", customer, null, null),
    			
    			// customer: not null
    			Arguments.of(TicketPriority.P1, "Printer Broken", "Cannot print labels", null, null, null)
    			);
    }

	@ParameterizedTest
	@MethodSource("validActemiumTicketAttributes04")
	public void createActemiumTicket_ValidAttributes04_DoesNotThrowException(TicketPriority ticketPriority,
			String title, String description, Customer customer) {
		Assertions.assertDoesNotThrow(() -> new ActemiumTicket(ticketPriority, title, description, customer));
	}

	@ParameterizedTest
	@MethodSource("invalidActemiumTicketAttributes04")
	public void createActemiumTicket_InValidAttributes04_ThrowsIllegalArgumentException(TicketPriority ticketPriority,
			String title, String description, Customer customer) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new ActemiumTicket(ticketPriority, title, description, customer));
	}

	@ParameterizedTest
	@MethodSource("validActemiumTicketAttributes06")
	public void createActemiumTicket_ValidAttributes06_DoesNotThrowException(TicketPriority ticketPriority,
			String title, String description, Customer customer, String remarks, String attachments) {
		Assertions.assertDoesNotThrow(
				() -> new ActemiumTicket(ticketPriority, title, description, customer, remarks, attachments));
	}

	@ParameterizedTest
	@MethodSource("invalidActemiumTicketAttributes06")
	public void createActemiumTicket_InValidAttributes06_ThrowsIllegalArgumentException(TicketPriority ticketPriority,
			String title, String description, Customer customer, String remarks, String attachments) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new ActemiumTicket(ticketPriority, title, description, customer, remarks, attachments));
	}

	// TODO Should this method be tested seperately like it is now
	// or will a constructor test be sufficient since the method is called in the
	// constructor?
	@Test
	public void addTechnician_TechniciansContainsNewTechnician() {
		ticket01.addTechnician(technician);
		Assertions.assertEquals(technician, ticket01.getTechnicians().get(0));
	}
    
}
