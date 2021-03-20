package gui.viewModels;

import domain.Employee;
import domain.enums.TicketStatus;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;

import java.util.*;

public class ChartViewModel{

    private final UserFacade userFacade;
    private final TicketFacade ticketFacade;

    public ChartViewModel(TicketFacade facade, UserFacade userFacade) {
        this.userFacade = userFacade;
        this.ticketFacade = facade;
    }

    public Map<String, Integer> barChartMonthlyTickets() {
        Map<String, Integer> mapMonthly = new HashMap<>();
        List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

        months.forEach(m -> mapMonthly.put(m, 0));

        ticketFacade.giveActemiumTickets()
                .forEach(t -> {
                    String month = t.getDateAndTimeOfCreation().getMonth().toString().charAt(0) + t.getDateAndTimeOfCreation().getMonth().toString().substring(1).toLowerCase();
                    mapMonthly.put(month, mapMonthly.get(month) + 1);
                });

        return mapMonthly;
    }

    public Map<String, Integer> barChartDataAmountOfTicketsPerStatus() {
        Map<String, Integer> mapStatus = new HashMap<>();

        for (TicketStatus status : TicketStatus.values()) {
            mapStatus.put(status.toString(), 0);
        }

        ticketFacade.giveActemiumTickets()
                .forEach(t -> mapStatus.put(t.getStatusAsString(), mapStatus.get(t.getStatusAsString()) + 1));

        return mapStatus;
    }

    public Map<Long, Integer> barChartDataAmountOfResolvedTicketsAsignedToTechnician() {
        Map<Long, Integer> mapTechniciansResolved = new HashMap<>();

        //Technician is unique by its UserID
        ticketFacade.getAllTechnicians()
                .forEach(tech -> mapTechniciansResolved.put(tech.getUserId(), 0));

        ticketFacade.giveActemiumTicketsResolved()
                .forEach(ticket -> ticket.giveTechnicians()
                        .forEach(tech -> mapTechniciansResolved.put(tech.getUserId(), mapTechniciansResolved.get(tech.getUserId()) + 1)));

        return mapTechniciansResolved;
    }

    public Map<Long, Integer> barChartDataAmountOfOutstandingTicketsAsignedToTechnician() {
        Map<Long, Integer> mapTechniciansOutstanding = new HashMap<>();

        //Technician is unique by its UserID
        ticketFacade.getAllTechnicians()
                .forEach(tech -> mapTechniciansOutstanding.put(tech.getUserId(), 0));

        ticketFacade.giveActemiumTicketsOutstanding()
                .forEach(ticket -> ticket.giveTechnicians()
                        .forEach(tech -> mapTechniciansOutstanding.put(tech.getUserId(), mapTechniciansOutstanding.get(tech.getUserId()) + 1)));

        return mapTechniciansOutstanding;
    }

    public String getnameOftechnicianByID(long id) {
        return userFacade.getNameByID(id);
    }

}
