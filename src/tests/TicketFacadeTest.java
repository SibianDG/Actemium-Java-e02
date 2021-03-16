package tests;

import java.util.ArrayList;

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

    @Mock
    private GenericDao<ActemiumTicket> ticketRepoDummy;

    @InjectMocks
    private TicketFacade tf;

    private void trainDummy() throws InformationRequiredException {
        ArrayList<ActemiumTicket> tickets = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket = new ActemiumTicket.TicketBuiler()
                    .ticketPriority(TicketPriority.P1)
                    .ticketType(TicketType.SOFTWARE)
                    .title("Printer Broken")
                    .description("Cannot print labels")
                    .customer(customer)
                    .build();


        }
        Mockito.lenient().when(ticketRepoDummy.findAll()).thenReturn(tickets);

    }

    @Override
    public ActemiumTicket getActemiumTicket() throws InformationRequiredException {
        return new ActemiumTicket.TicketBuiler()
                .ticketPriority(TicketPriority.P1)
                .ticketType(TicketType.SOFTWARE)
                .title("Printer Broken")
                .description("Cannot print labels")
                .customer(customer)
                .build();
    }
}
