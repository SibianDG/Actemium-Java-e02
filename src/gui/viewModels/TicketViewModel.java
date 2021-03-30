package gui.viewModels;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Employee;
import domain.Ticket;
import domain.User;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;
import languages.LanguageResource;

public class TicketViewModel extends ViewModel {

    private GUIEnum currentState;
    private Ticket selectedTicket;
    //private ObservableList<Employee> techniciansForTicket;
    private final TicketFacade ticketFacade;
    private final UserFacade userFacade;

    private List<ActemiumEmployee> techniciansAsignedToTicket = new ArrayList<>();

    public TicketViewModel(TicketFacade ticketFacade, UserFacade userFacade) {
        super();
        this.ticketFacade = ticketFacade;
        this.userFacade = userFacade;
        setCurrentState(GUIEnum.TICKET);
    }

    public ObservableList<Ticket> giveTicketsOutstanding() {
        return ticketFacade.giveActemiumTicketsOutstanding();
    }

    public ObservableList<Ticket> giveTicketsResolved() {
        return ticketFacade.giveActemiumTicketsResolved();
    }

    public Ticket getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(Ticket ticket) {
        this.selectedTicket = ticket;
        if (ticket != null) {        	
            setCurrentState(GUIEnum.TICKET);
            setTechniciansAsignedToTicketEmpty();
        }
        fireInvalidationEvent();
    }

    public ArrayList<String> getDetailsNewTicket(){
        return new ArrayList<String>(Arrays.asList(LanguageResource.getString("title"), LanguageResource.getString("creation_date")
                , LanguageResource.getString("priority"), LanguageResource.getString("type"), LanguageResource.getString("technicians")
                , LanguageResource.getString("customer_id"), LanguageResource.getString("description")
                , LanguageResource.getString("comments"), LanguageResource.getString("attachments")));
    }

    public Map<String, Map<Boolean, Object>> getDetails() {
        Ticket ticket = selectedTicket;
        Map<String, Map<Boolean, Object>> details = new LinkedHashMap<>();
        boolean techPermissions = userFacade.getEmployeeRole().equals(EmployeeRole.TECHNICIAN) && TicketStatus.isOutstanding();
        boolean editable = TicketStatus.isOutstanding();
        if(TicketStatus.isOutstanding() && userFacade.getEmployeeRole().equals(EmployeeRole.TECHNICIAN)) {
        	editable = !editable;
        }
        details.put(LanguageResource.getString("title"), Collections.singletonMap(editable, ticket.getTitle()));
        details.put(LanguageResource.getString("creation_date"), Collections.singletonMap(false, ticket.getDateOfCreation().format(DateTimeFormatter.ISO_DATE)));
        details.put(LanguageResource.getString("creation_time"), Collections.singletonMap(false, ticket.getDateAndTimeOfCreation().format(formatDateTime)));
        if (!TicketStatus.isOutstanding()) {
            details.put(LanguageResource.getString("completion_date"), Collections.singletonMap(false, ticket.getDateAndTimeOfCompletion().format(DateTimeFormatter.ISO_DATE)));
            details.put(LanguageResource.getString("completion_time"), Collections.singletonMap(false, ticket.getDateAndTimeOfCompletion().format(formatDateTime)));
        }
        details.put(LanguageResource.getString("priority"), Collections.singletonMap(editable, ticket.getPriority()));
        details.put(LanguageResource.getString("type"), Collections.singletonMap(editable, ticket.getTicketType()));
        details.put(LanguageResource.getString("status"), Collections.singletonMap(editable || techPermissions, ticket.getStatus()));
        details.put(LanguageResource.getString("description"), Collections.singletonMap(editable, ticket.getDescription()));
        details.put(LanguageResource.getString("customer/company"), Collections.singletonMap(false, ticket.giveCompany().getName()));
        details.put(LanguageResource.getString("technicians"), Collections.singletonMap(editable, ticket.giveTechnicians()));
        details.put(LanguageResource.getString("comments"), Collections.singletonMap(false, ticket.giveComments().stream().map(Object::toString).collect(Collectors.joining("\n"))));
        if (TicketStatus.isOutstanding()) {
        	details.put(LanguageResource.getString("new_comment"), Collections.singletonMap(editable || techPermissions, ""));
        }
        details.put(LanguageResource.getString("attachments"), Collections.singletonMap(editable || techPermissions, ticket.getAttachments()));
        if (!TicketStatus.isOutstanding()) {
        	details.put(LanguageResource.getString("solution"), Collections.singletonMap(true, ticket.getSolution()));
            details.put(LanguageResource.getString("quality"), Collections.singletonMap(true, ticket.getQuality()));
            details.put(LanguageResource.getString("support_needed"), Collections.singletonMap(true, ticket.getSupportNeeded()));
        }
        return details;
    }

    public String getIdSelectedTicket() {
        return selectedTicket.getTicketIdString();
    }

    public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
    						String commentText, String attachments, int customerId) throws InformationRequiredException {
        ticketFacade.registerTicket(priority, ticketType, title, description, commentText, attachments, customerId, techniciansAsignedToTicket);
        setSelectedTicket(ticketFacade.getLastAddedTicket());
    }

    public void modifyTicketOutstanding(TicketPriority priority, TicketType ticketType, TicketStatus status, String title, String description,
    								String commentText, String attachments, List<ActemiumEmployee> technicians) throws InformationRequiredException {
        ticketFacade.modifyTicketOutstanding((ActemiumTicket) selectedTicket, priority, ticketType, status, title, description, commentText, attachments, technicians);
    }

    public void modifyTicketResolved(String solution, String quality, String supportNeeded) throws InformationRequiredException {
        ticketFacade.modifyTicketResolved((ActemiumTicket) selectedTicket, solution, quality, supportNeeded);
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }
    

    @Override
    public void delete() throws InformationRequiredException {
        ticketFacade.delete((ActemiumTicket) selectedTicket);
    }

    public ObservableList<Employee> getAllTechnicians() {
        return ticketFacade.getAllTechnicians();
    }

    public void addTechnicianToTicket(Employee tech){
        if(!techniciansAsignedToTicket.contains(tech))
            techniciansAsignedToTicket.add((ActemiumEmployee) tech);
    }

    public void removeTechnician(Employee tech) {
        techniciansAsignedToTicket.remove(tech);
    }

    public List<ActemiumEmployee> getTechniciansAsignedToTicket() {
        return techniciansAsignedToTicket;
    }

    public void setTechniciansAsignedToTicketEmpty() {
        this.techniciansAsignedToTicket = new ArrayList<>();
    }

    public User getSignedInUser() {
        return userFacade.getSignedInUser();
    }
}