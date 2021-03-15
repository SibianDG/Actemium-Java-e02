package tests;

import java.util.ArrayList;

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

    private void trainDummy() {
        ArrayList<ActemiumTicket> tickets = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE, "Printer Broken", "Cannot print labels", customer);

        }
        Mockito.lenient().when(ticketRepoDummy.findAll()).thenReturn(tickets);

    }

}
