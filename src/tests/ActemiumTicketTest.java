package tests;

import java.util.stream.Stream;

import exceptions.InformationRequiredException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketType;

public class ActemiumTicketTest implements Attributes {
	
	private final ActemiumEmployee technician = new ActemiumEmployee.EmployeeBuilder()
			.username("jooKlein123")
			.password("Passwd123&")
			.firstName("Joost")
			.lastName("Klein")
			.address("Adress")
			.phoneNumber("0470099874")
			.emailAddress("student@student.hogent.be")
			.role(EmployeeRole.TECHNICIAN)
			.build();

	private static ActemiumCompany google;

	static {
		try {
			google = new ActemiumCompany.CompanyBuilder()
					.name("Google")
					.country("United States")
					.city("Mountain View, CA 94043")
					.address("1600 Amphitheatre Parkway")
					.phoneNumber("+1-650-253-0000")
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
	}

	private static ActemiumCustomer customer;

	static {
		try {
			customer = new ActemiumCustomer.CustomerBuilder()
					.username("customer123")
					.password("PassWd123&")
					.firstName("John")
					.lastName("Smith")
					.company(google)
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
	}

	public ActemiumTicketTest() throws InformationRequiredException {
	}

	private static Stream<Arguments> validActemiumTicketAttributes04() {
        return Stream.of(
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer),
                Arguments.of(TicketPriority.P2, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer),
                Arguments.of(TicketPriority.P3, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer),
				Arguments.of(TicketPriority.P1, TicketType.HARDWARE, "Printer Broken", "Cannot print labels", customer),
				Arguments.of(TicketPriority.P1, TicketType.NETWORK, "Printer Broken", "Cannot print labels", customer),
				Arguments.of(TicketPriority.P1, TicketType.INFRASTRUCTURE, "Printer Broken", "Cannot print labels", customer)
				);
    }

    private static Stream<Arguments> invalidActemiumTicketAttributes04() {
        return Stream.of(
                // title: not null or empty or blank, but (special chars)? and digits are ok
        		Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, null, "Cannot print labels", customer),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "", "Cannot print labels", customer),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "   ", "Cannot print labels", customer),
                
                // description: not null or empty or blank, but special chars and digits are ok
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", null, customer),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "", customer),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "    ", customer),
                                
                // customer: not null
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE,"Printer Broken", "Cannot print labels", null)
        		);
    }

    private static Stream<Arguments> validActemiumTicketAttributes06() {
    	return Stream.of(
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer, null, null),
    			Arguments.of(TicketPriority.P2, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer, "", null),
    			Arguments.of(TicketPriority.P3, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer, null, ""),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer, "Remarks", "Attachment"),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels 123", customer, "Remarks 123", "Attachment 123"),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels 123", customer, "", ""),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels 123", customer, "    ", "    ")
    			);
    }
    
    private static Stream<Arguments> invalidActemiumTicketAttributes06() {
    	return Stream.of(
    			// title: not null or empty or blank, but (special chars)? and digits are ok
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, null, "Cannot print labels", customer, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "", "Cannot print labels", customer, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "   ", "Cannot print labels", customer, null, null),
    			
    			// description: not null or empty or blank, but special chars and digits are ok
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", null, customer, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "", customer, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "    ", customer, null, null),
    			
    			// customer: not null
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", null, null, null)
    			);
    }

	@ParameterizedTest
	@MethodSource("validActemiumTicketAttributes04")
	public void createActemiumTicket_ValidAttributes04_DoesNotThrowException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCustomer customer) {
		Assertions.assertDoesNotThrow(() -> new ActemiumTicket.TicketBuilder()
																.ticketPriority(ticketPriority)
																.ticketType(ticketType)
																.title(title)
																.description(description)
																.customer(customer)
																.build());

	}

	@ParameterizedTest
	@MethodSource("invalidActemiumTicketAttributes04")
	public void createActemiumTicket_InValidAttributes04_ThrowsIllegalArgumentException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCustomer customer) {
		Assertions.assertThrows(InformationRequiredException.class,
				() -> new ActemiumTicket.TicketBuilder()
										.ticketPriority(ticketPriority)
										.ticketType(ticketType)
										.title(title)
										.description(description)
										.customer(customer)
										.build());
	}

	@ParameterizedTest
	@MethodSource("validActemiumTicketAttributes06")
	public void createActemiumTicket_ValidAttributes06_DoesNotThrowException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCustomer customer, String remarks, String attachments) {
		Assertions.assertDoesNotThrow(
				() -> new ActemiumTicket.TicketBuilder()
										.ticketPriority(ticketPriority)
										.ticketType(ticketType)
										.title(title)
										.description(description)
										.customer(customer)
										.build());
	}

	@ParameterizedTest
	@MethodSource("invalidActemiumTicketAttributes06")
	public void createActemiumTicket_InValidAttributes06_ThrowsIllegalArgumentException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCustomer customer, String remarks, String attachments) {
		Assertions.assertThrows(InformationRequiredException.class,
				() -> new ActemiumTicket.TicketBuilder()
										.ticketPriority(ticketPriority)
										.ticketType(ticketType)
										.title(title)
										.description(description)
										.customer(customer)
										.build());
	}

	// TODO Should this method be tested seperately like it is now
	// or will a constructor test be sufficient since the method is called in the
	// constructor?
	@Test
	public void addTechnician_TechniciansContainsNewTechnician() throws InformationRequiredException {
		ActemiumTicket actemiumTicket = getActemiumTicket();
		actemiumTicket.addTechnician(technician);
		Assertions.assertEquals(technician, actemiumTicket.getTechnicians().get(0));
	}

	@Override
	public ActemiumTicket getActemiumTicket() throws InformationRequiredException {
			return new ActemiumTicket.TicketBuilder()
					.ticketPriority(TicketPriority.P1)
					.ticketType(TicketType.SOFTWARE)
					.title("Printer Broken")
					.description("Cannot print labels")
					.customer(customer)
					.build();

	}
}
