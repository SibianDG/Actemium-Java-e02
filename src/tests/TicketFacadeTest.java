package tests;

import java.util.ArrayList;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import exceptions.InformationRequiredException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;
import domain.facades.TicketFacade;
import repository.GenericDao;

@ExtendWith(MockitoExtension.class)
public class TicketFacadeTest implements Attributes {

    private ActemiumCompany google;

    private ActemiumCustomer cust;

    private void initializeAttributes(){
        try {
            google = new ActemiumCompany.CompanyBuilder()
                    .name("Google")
                    .country("United States")
                    .city("Mountain View, CA 94043")
                    .address("1600 Amphitheatre Parkway")
                    .phoneNumber("+1-650-253-0000")
                    .build();
            cust = new ActemiumCustomer.CustomerBuilder()
                    .username("customer123")
                    .password("PassWd123&")
                    .firstName("John")
                    .lastName("Smith")
                    .company(google)
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }
    }

    @Mock
    private GenericDao<ActemiumTicket> ticketRepoDummy;

    private void trainDummy() throws InformationRequiredException {
        initializeAttributes();
        ArrayList<ActemiumTicket> tickets = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket = new ActemiumTicket.TicketBuilder()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Printer Broken")
                    .description("Cannot print labels")
	                .company(cust.getCompany())
                    .build();


        }
        Mockito.lenient().when(ticketRepoDummy.findAll()).thenReturn(tickets);

    }

    @Override
    public ActemiumTicket getActemiumTicket() {
        initializeAttributes();

        try {
            return new ActemiumTicket.TicketBuilder()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Printer Broken")
                    .description("Cannot print labels")
	                .company(cust.getCompany())
                    .build();
        } catch (InformationRequiredException e) {
            throw new IllegalArgumentException("Problem with initialize variables before test.");
        }

    }
}
