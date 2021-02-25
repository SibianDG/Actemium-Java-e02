package domain.facades;

import domain.Ticket;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class TicketFacade implements Facade {
	
	GenericDao<Ticket> ticketRepo;
	
	public TicketFacade() {
		this.ticketRepo = new GenericDaoJpa<Ticket>(Ticket.class);
	}

}
