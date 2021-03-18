package tests;

import java.time.LocalDate;
import java.util.stream.Stream;

import exceptions.InformationRequiredException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;

public class CustomerTest {

	private ActemiumCompany google;

	private void initializeAttibutes(){
		try {
			google = new ActemiumCompany.CompanyBuilder()
					.name("Facebook")
					.country("United States")
					.city("Menlo Park, CA 94025")
					.address("1 Hacker Way")
					.phoneNumber("+1-650-308-7300")
					.build();
		} catch (InformationRequiredException e) {
			throw new IllegalArgumentException("Problem with initialize variables before test.");
		}
	}

	public CustomerTest() {
	}

	private static Stream<Arguments> validUserAttributes() {
        return Stream.of(
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens"),
                Arguments.of("A", "AaBbCc&1", "A", "A"),
                Arguments.of("PieterJan", "Passwd.2", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("123tester", "Passwd123&", "MrEcHtZoEeNsUpErLaNgEnAaMmAn", "MrEcHtZoEeNsUpErLaNgEnAaMmAn"),
                Arguments.of("Ahnjaaaa", "Passwd123&", "Ik", "De Gussem")
        		);
    }

    private static Stream<Arguments> invalidUserAttributes() {
        return Stream.of(
        		// Username: not null or empty, not duplicate, no special chars but digits is ok
                Arguments.of("    ", "Passwd123&", "Jan", "Jannsens"),
                Arguments.of("----", "AaBbCc&1", "A", "A"),
                Arguments.of("user_name", "AaBbCc&1", "A", "A"),
                Arguments.of("user.name", "AaBbCc&1", "A", "A"),
                Arguments.of(null, "Passwd123&", "Naam1", "Jow"),

                // Password: min 8 chars, Upper- and lowercase, +1 digit, +1 special char, not null or empty
                Arguments.of("Janerman", "   ", "Jan", "Jannsens"),
                Arguments.of("PieterJan", "Pass2&9", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "pass2&49", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "Pass2049", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "Pass&&&&", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "PPPP456|", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("123tester", null, "Naam1", "Jow"),

                // FirstName: not null or empty, no digits
                Arguments.of("Janerman", "Passwd123&", "   ", "Jannsens"),
                Arguments.of("123tester", "Passwd123&", "Bart1", "Peeters"),
                
                // Lastname: not null or empty, no digits
                Arguments.of("123tester", "Passwd123&", "Koen", "Crucke1"),
                Arguments.of("Janerman", "Passwd123&", "Jan", "   "),
                Arguments.of("123tester", "Passwd123&", null, "Jow"),
                Arguments.of("123tester", "Passwd123&", "Naam1", null)
        		);
    }

	@ParameterizedTest
	@MethodSource("validUserAttributes")
	public void createCustomer_ValidAttributes_DoesNotThrowException(String username, String password, String firstName, String lastName) {
		initializeAttibutes();
		Assertions.assertDoesNotThrow(() -> new ActemiumCustomer.CustomerBuilder()
				.username(username)
				.password(password)
				.firstName(firstName)
				.lastName(lastName)
				.company(google)
				.build());
	}

	@ParameterizedTest
	@MethodSource("invalidUserAttributes")
	public void createCustomer_InValidAttributes_ThrowsIllegalArgumentException(String username, String password,
			String firstName, String lastName) {
		initializeAttibutes();
		Assertions.assertThrows(InformationRequiredException.class,
				() -> new ActemiumCustomer.CustomerBuilder()
						.username(username)
						.password(password)
						.firstName(firstName)
						.lastName(lastName)
						.company(google)
						.build());
	}
    
    @Test
    public void giveEmployeeSeniroity_returns_valid() {
		initializeAttibutes();
		ActemiumCustomer customer;
		try {
			customer = new ActemiumCustomer.CustomerBuilder()
					.username("Tester123")
					.password("Passwd123&")
					.firstName("Jan")
					.lastName("Jannsens")
					.company(google)
					.build();
		} catch (InformationRequiredException e) {
			throw new IllegalArgumentException("Problem with initialize variables before test.");
		}
    	customer.setRegistrationDate(LocalDate.now().minusYears(10));
        Assertions.assertEquals(10, customer.giveSeniority());
    }
    
}
