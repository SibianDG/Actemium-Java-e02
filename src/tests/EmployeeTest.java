package tests;

import java.util.stream.Stream;

import domain.Administrator;
import domain.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.User;

public class EmployeeTest {

    private User user;

    private static Stream<Arguments> validUserAttributes() {
        return Stream.of(
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "0470099874", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "0000000000", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999-99", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9/99999-99", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "stu_dent@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress 94521 bus 10", "9999999999", "stu.dent@student.hogent.be", "role")
        );
    }

    private static Stream<Arguments> invalidUserAttributes() {
        return Stream.of(
                //empty
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "   ", "0470099874", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "   ", "   ", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", "student@student.hogent.be", "   "),

                //null
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", null, "9999999999", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", null, "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", null, "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "9999999999", "student@student.hogent.be", null),

                //phone
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999d", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "99999999|", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "99999999@", "student@student.hogent.be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@student.hogent.be", "role"),

                //email
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@studentbe", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@student be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "studentstudent be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student@studen_be", "role"),
                Arguments.of("Tester123", "Passwd123&", "Jan", "Jannsens", "Hogent Adress", "999999999A", "student&studen_be", "role")


                );
    }

    @ParameterizedTest
    @MethodSource("validUserAttributes")
    public void createEmployee_Correct(String username, String password, String firstName, String lastName, String address,
                                       String phoneNumber, String emailAddress, String role) {
        Assertions.assertDoesNotThrow(() -> new Employee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role));
    }

    @ParameterizedTest
    @MethodSource("invalidUserAttributes")
    public void createEmployee_Failed(String username, String password, String firstName, String lastName, String address,
                                       String phoneNumber, String emailAddress, String role) {
        Assertions.assertThrows(IllegalArgumentException.class,() -> new Employee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role));
    }
}


