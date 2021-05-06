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

public class TicketTest implements Attributes {
	
	private static ActemiumEmployee technician;

	private static ActemiumCompany google;

	private static ActemiumCustomer customer;

	private static void setAttributes(){
		try {
			technician = new ActemiumEmployee.EmployeeBuilder()
					.username("jooKlein123")
					.password("Passwd123&")
					.firstName("Joost")
					.lastName("Klein")
					.address("Adress")
					.phoneNumber("0470099874")
					.emailAddress("student@student.hogent.be")
					.role(EmployeeRole.TECHNICIAN)
					.build();
			google = new ActemiumCompany.CompanyBuilder()
					.name("Google")
					.country("United States")
					.city("Mountain View, CA 94043")
					.address("1600 Amphitheatre Parkway")
					.phoneNumber("+1-650-253-0000")
					.build();
			customer = new ActemiumCustomer.CustomerBuilder()
					.username("customer123")
					.password("PassWd123&")
					.firstName("John")
					.lastName("Smith")
					.emailAddress("john.smith@student.hogent.be")
					.company(google)
					.build();
		} catch (InformationRequiredException e) {
			throw new IllegalArgumentException("Problem with initialize variables before test.");
		}
	}

	private static Stream<Arguments> validActemiumTicketAttributes04() {
		setAttributes();
        return Stream.of(
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google),
                Arguments.of(TicketPriority.P2, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google),
                Arguments.of(TicketPriority.P3, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google),
				Arguments.of(TicketPriority.P1, TicketType.HARDWARE, "Printer Broken", "Cannot print labels", google),
				Arguments.of(TicketPriority.P1, TicketType.NETWORK, "Printer Broken", "Cannot print labels", google),
				Arguments.of(TicketPriority.P1, TicketType.INFRASTRUCTURE, "Printer Broken", "Cannot print labels", google)
				);
    }

    private static Stream<Arguments> invalidActemiumTicketAttributes04() {
		setAttributes();
		return Stream.of(
                // title: not null or empty or blank, but (special chars)? and digits are ok
        		Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, null, "Cannot print labels", google),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "", "Cannot print labels", google),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "   ", "Cannot print labels", google),
                
                // description: not null or empty or blank, but special chars and digits are ok
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", null, google),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "", google),
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "    ", google),
                                
                // customer: not null
                Arguments.of(TicketPriority.P1, TicketType.SOFTWARE,"Printer Broken", "Cannot print labels", null),

				//Enums null
				Arguments.of(null, TicketType.HARDWARE, "Printer Broken", "Cannot print labels", google),
				Arguments.of(TicketPriority.P1, null, "Printer Broken", "Cannot print labels", google)
		);

	}

    private static Stream<Arguments> validActemiumTicketAttributes06() {
		setAttributes();
		return Stream.of(
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google, null, null),
    			Arguments.of(TicketPriority.P2, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google, "", null),
    			Arguments.of(TicketPriority.P3, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google, null, ""),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", google, "Remarks", "Attachment"),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels 123", google, "Remarks 123", "Attachment 123"),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels 123", google, "", ""),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels 123", google, "    ", "    ")
    			);
    }
    
    private static Stream<Arguments> invalidActemiumTicketAttributes06() {
		setAttributes();
		return Stream.of(
    			// title: not null or empty or blank, but (special chars)? and digits are ok
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, null, "Cannot print labels", google, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "", "Cannot print labels", google, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "   ", "Cannot print labels", google, null, null),
    			
    			// description: not null or empty or blank, but special chars and digits are ok
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", null, google, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "", google, null, null),
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "    ", google, null, null),
    			
    			// customer: not null
    			Arguments.of(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", null, null, null)
    			);
    }

	@ParameterizedTest
	@MethodSource("validActemiumTicketAttributes04")
	public void createActemiumTicket_ValidAttributes04_DoesNotThrowException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCompany company) {
		Assertions.assertDoesNotThrow(() -> new ActemiumTicket.TicketBuilder()
																.ticketPriority(ticketPriority)
																.ticketType(ticketType)
																.title(title)
																.description(description)
												                .company(company)
																.build());
	}

	@ParameterizedTest
	@MethodSource("invalidActemiumTicketAttributes04")
	public void createActemiumTicket_InValidAttributes04_ThrowsIllegalArgumentException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCompany company) {
		Assertions.assertThrows(InformationRequiredException.class,
				() -> new ActemiumTicket.TicketBuilder()
										.ticketPriority(ticketPriority)
										.ticketType(ticketType)
										.title(title)
										.description(description)
						                .company(company)
										.build());
	}

	@ParameterizedTest
	@MethodSource("validActemiumTicketAttributes06")
	public void createActemiumTicket_ValidAttributes06_DoesNotThrowException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCompany company, String remarks, String attachments) {
		Assertions.assertDoesNotThrow(
				() -> new ActemiumTicket.TicketBuilder()
										.ticketPriority(ticketPriority)
										.ticketType(ticketType)
										.title(title)
										.description(description)
						                .company(company)
										.attachments(attachments)
										.build());
	}

	@ParameterizedTest
	@MethodSource("invalidActemiumTicketAttributes06")
	public void createActemiumTicket_InValidAttributes06_ThrowsIllegalArgumentException(TicketPriority ticketPriority, TicketType ticketType,
			String title, String description, ActemiumCompany company, String remarks, String attachments) {
		Assertions.assertThrows(InformationRequiredException.class,
				() -> new ActemiumTicket.TicketBuilder()
										.ticketPriority(ticketPriority)
										.ticketType(ticketType)
										.title(title)
										.description(description)
						                .company(company)
										.attachments(attachments)
										.build());
	}

	@Test
	public void addTechnician_TechniciansContainsNewTechnician() {
		setAttributes();
		ActemiumTicket actemiumTicket = getActemiumTicket();
		actemiumTicket.addTechnician(technician);
		Assertions.assertEquals(technician, actemiumTicket.getTechnicians().get(0));
	}

	@Override
	public ActemiumTicket getActemiumTicket(){
		setAttributes();
		try {
			return new ActemiumTicket.TicketBuilder()
					.ticketPriority(TicketPriority.P1)
					.ticketType(TicketType.SOFTWARE)
					.title("Printer Broken")
					.description("Cannot print labels")
	                .company(customer.getCompany())
					.build();
		} catch (InformationRequiredException e) {
			throw new IllegalArgumentException("Problem with initialize variables before test.");
		}
	}
}
