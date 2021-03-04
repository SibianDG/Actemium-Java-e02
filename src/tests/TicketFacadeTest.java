package tests;

import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.facades.TicketFacade;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenericDao;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class TicketFacadeTest implements Attributes {

    @Mock
    private GenericDao<ActemiumTicket> ticketRepoDummy;

    @InjectMocks
    private TicketFacade tf;

    private void trainDummy() {
        ArrayList<ActemiumTicket> tickets = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER; i++) {
            ActemiumTicket ticket = new ActemiumTicket(TicketPriority.P1, "Printer Broken", "Cannot print labels", customer);

        }
        Mockito.lenient().when(ticketRepoDummy.findAll()).thenReturn(tickets);

    }

}
