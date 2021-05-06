package tests;

import domain.*;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.enums.TicketPriority;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TicketChangeTest {

    private static ActemiumTicket ticket;

    private static ActemiumCompany google;

    private static ActemiumCustomer customer;

    private static void setAttributes(){
        try {

            //TicketPriority.P1, TicketType.SOFTWARE, "Title", "Cannot print labels", customer),

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
            ticket = new ActemiumTicket.TicketBuilder()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Title")
                    .description("Cannot print labels")
	                .company(customer.getCompany())
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }
    }


    private static Stream<Arguments> validTicketChangesAttributes() {
        setAttributes();

        return Stream.of(
                Arguments.of(ticket, customer, "UserRole", LocalDateTime.now(), "commentText"),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusMonths(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusYears(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusDays(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusHours(1), "changeDescription"),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusYears(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusDays(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription"),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", Collections.singletonList("")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", null),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription")

                );
    }

    private static Stream<Arguments> invalidTicketChangesAttributes() {
        setAttributes();

        return Stream.of(
                Arguments.of(null, customer, "UserRole", LocalDateTime.now(), "changeDescription"),

                Arguments.of(ticket, null, "Admin", LocalDateTime.now().minusMonths(1), "changeDescription"),
                Arguments.of(ticket, customer, null, LocalDateTime.now().minusYears(1), "changeDescription"),
                Arguments.of(ticket, customer, "Admin", null, "changeDescription"),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusHours(1), null),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusHours(1), null),

                Arguments.of(ticket, customer, "", LocalDateTime.now().plusHours(1), "changeDescription"),
                Arguments.of(ticket, customer, "   ", LocalDateTime.now().plusYears(1), "changeDescription"),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusDays(1), ""),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "    ")
        );
    }

    @ParameterizedTest
    @MethodSource("validTicketChangesAttributes")
    public void createContractType_ValidAttributes_DoesNotThrowException(ActemiumTicket ticket, UserModel user, String userRole
            , LocalDateTime localDateTime, String commentText) {
        Assertions.assertDoesNotThrow(() -> new ActemiumTicketComment.TicketCommentBuilder()
                .ticket(ticket)
                .user(user)
                .userRole(userRole)
                .dateTimeOfComment(localDateTime)
                .commentText(commentText)
                .build());
    }

    @ParameterizedTest
    @MethodSource("invalidTicketChangesAttributes")
    public void createContractType_InValidAttributes_ThrowsIllegalArgumentException(ActemiumTicket ticket, UserModel user, String userRole
            , LocalDateTime localDateTime, String commentText) {
        Assertions.assertThrows(InformationRequiredException.class, () -> new ActemiumTicketComment.TicketCommentBuilder()
                .ticket(ticket)
                .user(user)
                .userRole(userRole)
                .dateTimeOfComment(localDateTime)
                .commentText(commentText)
                .build());
    }
}
