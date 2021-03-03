package gui.viewModels;

import domain.ActemiumTicket;
import domain.facades.Facade;
import domain.facades.TicketFacade;
import gui.GUIEnum;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TicketViewModel extends ViewModel {

    private GUIEnum currentState;
    private final TicketFacade ticketFacade;
    private ObservableList<ActemiumTicket> actemiumTickets;

    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    public TicketViewModel(TicketFacade ticketFacade) {
        super();
        this.ticketFacade = ticketFacade;
        this.actemiumTickets = FXCollections.observableArrayList();
    }

    public void setActemiumTickets(ObservableList<ActemiumTicket> tickets) {
        this.actemiumTickets = tickets;
    }

    public ObservableList<ActemiumTicket> getActemiumTickets() {
        return FXCollections.unmodifiableObservableList(actemiumTickets);
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }
}
