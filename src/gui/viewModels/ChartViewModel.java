package gui.viewModels;

import domain.enums.TicketStatus;
import domain.facades.TicketFacade;

import java.util.HashMap;
import java.util.Map;

public class ChartViewModel{

    private TicketFacade facade;

    public ChartViewModel(TicketFacade facade) {
        this.facade = facade;
    }

    public Map<String, Integer> barChartDataAmountOfTicketsPerStatus() {
        Map<String, Integer> mapStatus = new HashMap<>();

        for (TicketStatus status : TicketStatus.values()) {
            mapStatus.put(status.toString(), 0);
        }

        facade.giveActemiumTickets()
                .forEach(t -> mapStatus.put(t.getStatusAsString(), mapStatus.get(t.getStatusAsString()) + 1));

        return mapStatus;
    }

}
