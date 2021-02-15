package tests;

import domain.Administrator;
import domain.User;
import static org.junit.jupiter.api.Assertions.*;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

public class UserTest {

    private User user;

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
                //Spaces
                Arguments.of("    ", "Passwd123&", "Jan", "Jannsens"),
                Arguments.of("Janerman", "   ", "Jan", "Jannsens"),
                Arguments.of("Janerman", "Passwd123&", "   ", "Jannsens"),
                Arguments.of("Janerman", "Passwd123&", "Jan", "   "),

                //no special chars in username
                Arguments.of("----", "AaBbCc&1", "A", "A"),
                Arguments.of("user_name", "AaBbCc&1", "A", "A"),
                Arguments.of("user.name", "AaBbCc&1", "A", "A"),

                //passwd
                Arguments.of("PieterJan", "Pass2&9", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "pass2&49", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "Pass2049", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "Pass&&&&", "Pieter-Jan", "Manneken-jow"),
                Arguments.of("PieterJan", "PPPP456|", "Pieter-Jan", "Manneken-jow"),

                //firstname & lastname
                Arguments.of("123tester", "Passwd123&", "Naam1", "Jow"),
                Arguments.of("123tester", "Passwd123&", "Naam1", "Jow")

        );
    }

    //invalid
    /*
    Username: not null or empty, not duplicate, no special chars but digits is ok
    Password: min 8 chars, Upper- and lowercase, +1 digit, +1 special char, not null or empty
    FirstName: not null or empty, no digits
    Lastname: not null or empty, no digits
     */
    @ParameterizedTest
    @MethodSource("validUserAttributes")
    public void createUser_Correct(String username, String password, String firstName, String lastName) {
        Assertions.assertDoesNotThrow(() -> new Administrator(username, password, firstName, lastName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidUserAttributes")
    public void createUser_Failed(String username, String password, String firstName, String lastName) {
        Assertions.assertThrows(IllegalArgumentException.class,() -> new Administrator(username, password, firstName, lastName));
    }




}
