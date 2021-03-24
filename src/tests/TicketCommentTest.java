package tests;

import domain.*;
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

public class TicketCommentTest {

    private static ActemiumTicket ticket;

    private static ActemiumCompany google;

    private static ActemiumCustomer customer;

    private static void setAttributes(){
        try {

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
                    .company(google)
                    .build();
            ticket = new ActemiumTicket.TicketBuilder()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Title")
                    .description("Cannot print labels")
                    .customer(customer)
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }
    }


    private static Stream<Arguments> validTicketCommentsAttributes() {
        setAttributes();

        return Stream.of(
                Arguments.of(ticket, customer, "UserRole", LocalDateTime.now(), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusMonths(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusYears(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusDays(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusHours(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusYears(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusDays(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", Collections.singletonList("")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", null),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2"))

        );
    }

    private static Stream<Arguments> invalidTicketCommentsAttributes() {
        setAttributes();

        return Stream.of(
                Arguments.of(null, customer, "UserRole", LocalDateTime.now(), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),

                Arguments.of(ticket, null, "Admin", LocalDateTime.now().minusMonths(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, null, LocalDateTime.now().minusYears(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", null, "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusHours(1), null, Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().minusHours(1), null, Arrays.asList("changeContent1", "ChangeContent2")),

                Arguments.of(ticket, customer, "", LocalDateTime.now().plusHours(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "   ", LocalDateTime.now().plusYears(1), "changeDescription", Arrays.asList("changeContent1", "ChangeContent2")),

                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusDays(1), "", Arrays.asList("changeContent1", "ChangeContent2")),
                Arguments.of(ticket, customer, "Admin", LocalDateTime.now().plusHours(1), "    ", Arrays.asList("changeContent1", "ChangeContent2"))
        );
    }

    @ParameterizedTest
    @MethodSource("validTicketCommentsAttributes")
    public void createContractType_ValidAttributes_DoesNotThrowException(ActemiumTicket ticket, UserModel user, String userRole
            , LocalDateTime localDateTime, String changeDescription, List<String> changeContent) {
        Assertions.assertDoesNotThrow(() -> new ActemiumTicketChange.TicketChangeBuilder()
                .ticket(ticket)
                .user(user)
                .userRole(userRole)
                .dateTimeOfChange(localDateTime)
                .changeDescription(changeDescription)
                .changeContent(changeContent)
                .build());
    }

    @ParameterizedTest
    @MethodSource("invalidTicketCommentsAttributes")
    public void createContractType_InValidAttributes_ThrowsIllegalArgumentException(ActemiumTicket ticket, UserModel user, String userRole
            , LocalDateTime localDateTime, String changeDescription, List<String> changeContent) {
        Assertions.assertThrows(InformationRequiredException.class, () -> new ActemiumTicketChange.TicketChangeBuilder()
                .ticket(ticket)
                .user(user)
                .userRole(userRole)
                .dateTimeOfChange(localDateTime)
                .changeDescription(changeDescription)
                .changeContent(changeContent)
                .build());
    }

}
