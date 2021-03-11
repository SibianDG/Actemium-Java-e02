package tests;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumEmployee;
import domain.enums.EmployeeRole;

public class EmployeeTest {

	// The first 4 attributes are tested by CustomerTest which uses the same:
    // super(username, password, firstName, lastName)
    
    private static Stream<Arguments> validUserAttributes() {
        return Stream.of(
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "0470099874", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "0000000000", "student@student.hogent.be", EmployeeRole.TECHNICIAN),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", "student@student.hogent.be", EmployeeRole.SUPPORT_MANAGER),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999-99", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9/99999-99", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "stu_dent@student.hogent.be", EmployeeRole.ADMINISTRATOR),
				Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "stu.dent@student.hogent.be", EmployeeRole.ADMINISTRATOR),
				Arguments.of("Tester123", "Passwd 123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "stu.dent@student.hogent.be", EmployeeRole.ADMINISTRATOR),
				Arguments.of("Tester123", "Passwd123& ", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "stu.dent@student.hogent.be", EmployeeRole.ADMINISTRATOR)
        		);
    }

    private static Stream<Arguments> invalidUserAttributes() {
        return Stream.of(
                //empty
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "   ", "0470099874", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "0470099874", "   ", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "   ", "   ", EmployeeRole.ADMINISTRATOR),

                //null
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", null, "9999999999", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", null, "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", null, EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", "student@student.hogent.be", null),

                //phone
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999d", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "99999999|", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "99999999@", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR),

                //email
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@studentbe", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@student be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "studentstudent be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@studen_be", EmployeeRole.ADMINISTRATOR),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student&studen_be", EmployeeRole.ADMINISTRATOR)
                );
    }

	@ParameterizedTest
	@MethodSource("validUserAttributes")
	public void createEmployee_ValidAttributes_DoesNotThrowException(String username, String password, String firstName,
			String lastName, String address, String phoneNumber, String emailAddress, EmployeeRole role) {
		Assertions.assertDoesNotThrow(
				() -> new ActemiumEmployee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role));
	}

	@ParameterizedTest
	@MethodSource("invalidUserAttributes")
	public void createEmployee_InValidAttributes_ThrowsIllegalArgumentException(String username, String password,
			String firstName, String lastName, String address, String phoneNumber, String emailAddress,
			EmployeeRole role) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new ActemiumEmployee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role));
	}

	@Test
	public void giveEmployeeSeniroity_returns_valid() {
		ActemiumEmployee employee = new ActemiumEmployee("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "0470099874",
				"student@student.hogent.be", EmployeeRole.ADMINISTRATOR);
		employee.setRegistrationDate(LocalDate.now().minusYears(10));
		Assertions.assertEquals(10, employee.giveSeniority());
	}
    
}
