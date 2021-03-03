package domain.facades;

import domain.ActemiumTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

import java.util.List;

public class TicketFacade implements Facade {
	
	private GenericDao<ActemiumTicket> ticketRepo;

	private ObservableList<ActemiumTicket> tickets;
	
	public TicketFacade() {
		this.ticketRepo = new GenericDaoJpa<>(ActemiumTicket.class);
		fillTickets();
	}

	public void fillTickets() {
		List<ActemiumTicket> ticketList = ticketRepo.findAll();
		this.tickets = FXCollections.observableArrayList(ticketList);
	}

	public ObservableList<ActemiumTicket> getTickets() {
		return FXCollections.unmodifiableObservableList(tickets);
	}
}
