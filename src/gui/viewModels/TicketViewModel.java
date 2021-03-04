package gui.viewModels;

import domain.ActemiumTicket;
import domain.Customer;
import domain.Employee;
import domain.enums.TicketPriority;
import domain.facades.Facade;
import domain.facades.TicketFacade;
import gui.GUIEnum;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TicketViewModel extends ViewModel {

    private GUIEnum currentState;
    private ActemiumTicket selectedActemiumTicket;
    private final TicketFacade ticketFacade;
    private ObservableList<ActemiumTicket> actemiumTickets;

    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    public TicketViewModel(TicketFacade ticketFacade) {
        super();
        this.ticketFacade = ticketFacade;
        this.actemiumTickets = FXCollections.observableArrayList();
    }

    public Map<String, Object> getDetails() {
        ActemiumTicket ticket = selectedActemiumTicket;
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("Title", ticket.getTitle());
        return details;
    }

    public void setActemiumTickets(ObservableList<ActemiumTicket> tickets) {
        this.actemiumTickets = tickets;
    }

    public ObservableList<ActemiumTicket> getActemiumTickets() {
        return FXCollections.unmodifiableObservableList(actemiumTickets);
    }

    public ActemiumTicket getSelectedActemiumTicket() {
        return selectedActemiumTicket;
    }

    public void setSelectedActemiumTicket(ActemiumTicket selectedActemiumTicket) {
        this.selectedActemiumTicket = selectedActemiumTicket;
        fireInvalidationEvent();
    }

    //Todo
    public void registerTicket(TicketPriority priority, String title, String description,
                               String remarks, String attachments, Customer customer) {
        ticketFacade.registerTicket(priority, title, description, remarks, attachments, customer);
    }

    //Todo
    public void modifyTicket(TicketPriority priority, String title, String description,
                             String remarks, String attachments, Customer customer, List<Employee> technicians) {
        ticketFacade.modifyTicket(selectedActemiumTicket, priority, title, description, remarks, attachments, customer, technicians);

    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }
}
