package domain.facades;

import java.util.stream.Collectors;

import domain.Contract;
import domain.ContractType;
import domain.Customer;
import domain.Employee;
import domain.KbItem;
import domain.Ticket;
import domain.User;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.manager.Actemium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The interface Facade.
 */
public abstract class Facade {

    protected Actemium actemium;

    public Facade(Actemium actemium) {
        this.actemium = actemium;
    }

    public void setActemium(Actemium actemium) {
        this.actemium = actemium;
    }

    /**
     * Gets employee role.
     *
     * @return the employee role
     */
    public EmployeeRole getEmployeeRole() {
        return actemium.giveEmployeeRoleAsEnum();
    }

    /**
     * Give user role string.
     *
     * @return the string
     */
    public String giveUserRole() {
        return actemium.giveUserRole();
    }

    /**
     * Give user first name string.
     *
     * @return the string
     */
    public String giveUserFirstName() {
        return actemium.giveUserFirstName();
    }

    /**
     * Give user last name string.
     *
     * @return the string
     */
    public String giveUserLastName() {
        return actemium.giveUserLastName();
    }

    /**
     * Give user username string.
     *
     * @return the string
     */
    public String giveUserUsername() {
        return actemium.giveUsername();
    }

    /**
     * Give user employee id string.
     *
     * @return the string
     */
    public String giveUserEmployeeId() {
        return actemium.giveUserEmployeeId();
    }

    /**
     * Give user status string.
     *
     * @return the string
     */
    public String giveUserStatus( ) {
        return actemium.giveUserStatus();
    }

    /**
     * Give user email address string.
     *
     * @return the string
     */
    public String giveUserEmailAddress() {
        return actemium.giveUserEmailAddress();
    }

    /**
     * Give user phone number string.
     *
     * @return the string
     */
    public String giveUserPhoneNumber() {
        return actemium.giveUserPhoneNumber();
    }

    /**
     * Give user address string.
     *
     * @return the string
     */
    public String giveUserAddress() {
        return actemium.giveUserAddress();
    }

    /**
     * Give user seniority string.
     *
     * @return the string
     */
    public String giveUserSeniority() {
        return actemium.giveUserSeniority();
    }

    /**
     * Give user password string.
     *
     * @return the string
     */
    public String giveUserPassword() {
        return actemium.giveUserPassword();
    }


    /**
     * Gets last added customer.
     *
     * @return the last added customer
     */
    public Customer getLastAddedCustomer() {
        return actemium.getLastAddedCustomer();
    }

    /**
     * Gets last added employee.
     *
     * @return the last added employee
     */
    public Employee getLastAddedEmployee() {
        return actemium.getLastAddedEmployee();
    }

    /**
     * Give actemium customers observable list.
     *
     * @return the observable list
     */
    public ObservableList<Customer> giveActemiumCustomers() {
        return actemium.giveActemiumCustomers();
    }

    /**
     * Give actemium employees observable list.
     *
     * @return the observable list
     */
    public ObservableList<Employee> giveActemiumEmployees() {
        return actemium.giveActemiumEmployees();
    }

    /**
     * Gets name by id.
     *
     * @param id the id
     * @return the name by id
     */
    public String getNameByID(long id) {
        return actemium.getNameByID(id);
    }

    /**
     * Gets signed in user.
     *
     * @return the signed in user
     */
    public User getSignedInUser() {
        return actemium.getSignedInUser();
    }

    /**
     * Gets last added ticket.
     *
     * @return the last added ticket
     */
    public Ticket getLastAddedTicket() {
        return actemium.getLastAddedTicket();
    }

    /**
     * Give actemium tickets observable list.
     *
     * @return the observable list
     */
    public ObservableList<Ticket> giveActemiumTickets() {
        return actemium.giveActemiumTickets();
    }

    /**
     * Give actemium tickets outstanding observable list.
     *
     * @return the observable list
     */
    public ObservableList<Ticket> giveActemiumTicketsOutstanding() {
        return actemium.giveActemiumTicketsOutstanding();
    }

    /**
     * Give actemium tickets resolved observable list.
     *
     * @return the observable list
     */
    public ObservableList<Ticket> giveActemiumTicketsResolved() {
        return actemium.giveActemiumTicketsResolved();
    }

    /**
     * Give actemium tickets completed observable list.
     *
     * @return the observable list
     */
    public ObservableList<Ticket> giveActemiumTicketsCompleted() {
        return FXCollections.observableArrayList(
                actemium.giveActemiumTicketsResolved()
                        .stream()
                        .filter(t -> t.getStatus().equals(TicketStatus.COMPLETED))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets all technicians.
     *
     * @return the all technicians
     */
    public ObservableList<Employee> getAllTechnicians() {
        return FXCollections.observableArrayList(actemium.giveActemiumEmployees()
                .stream()
                .filter(t -> t.getRole().equals(EmployeeRole.TECHNICIAN))
                .collect(Collectors.toList()));
    }

    /**
     * Give tickets of same type observable list.
     *
     * @param type the type
     * @return the observable list
     */
    public ObservableList<Ticket> giveTicketsOfSameType(KbItemType type) {
        ObservableList<Ticket> ticketsOfSameType;
        switch(type) {
            case HARDWARE -> {
                ticketsOfSameType = FXCollections.observableArrayList(
                        giveActemiumTicketsCompleted()
                                .stream()
                                .filter(t -> t.getTicketType().equals(TicketType.HARDWARE))
                                .collect(Collectors.toList()));
            }
            case SOFTWARE -> {
                ticketsOfSameType = FXCollections.observableArrayList(
                        giveActemiumTicketsCompleted()
                                .stream()
                                .filter(t -> t.getTicketType().equals(TicketType.SOFTWARE))
                                .collect(Collectors.toList()));
            }
            case INFRASTRUCTURE -> {
                ticketsOfSameType = FXCollections.observableArrayList(
                        giveActemiumTicketsCompleted()
                                .stream()
                                .filter(t -> t.getTicketType().equals(TicketType.INFRASTRUCTURE))
                                .collect(Collectors.toList()));
            }
            case DATABASE -> {
                ticketsOfSameType = FXCollections.observableArrayList(
                        giveActemiumTicketsCompleted()
                                .stream()
                                .filter(t -> t.getTicketType().equals(TicketType.DATABASE))
                                .collect(Collectors.toList()));
            }
            case NETWORK -> {
                ticketsOfSameType = FXCollections.observableArrayList(
                        giveActemiumTicketsCompleted()
                                .stream()
                                .filter(t -> t.getTicketType().equals(TicketType.NETWORK))
                                .collect(Collectors.toList()));
            }
            default -> {
                ticketsOfSameType = FXCollections.observableArrayList(
                        giveActemiumTicketsCompleted()
                                .stream()
                                .filter(t -> t.getTicketType().equals(TicketType.OTHER))
                                .collect(Collectors.toList()));
            }
        }
        return ticketsOfSameType;
    }

    /**
     * Give actemium kb items observable list.
     *
     * @return the observable list
     */
    public ObservableList<KbItem> giveActemiumKbItems() {
        return actemium.giveActemiumKbItems();
    }

    /**
     * Gets last added kb item.
     *
     * @return the last added kb item
     */
    public KbItem getLastAddedKbItem() {
        return actemium.getLastAddedKbItem();
    }

    /**
     * Give actemium contract types observable list.
     *
     * @return the observable list
     */
    public ObservableList<ContractType> giveActemiumContractTypes() {
        return actemium.giveActemiumContractTypes();
    }

    /**
     * Gets last added contract type.
     *
     * @return the last added contract type
     */
    public ContractType getLastAddedContractType() {
        return actemium.getLastAddedContractType();
    }


    /**
     * Give actemium contracts observable list.
     *
     * @return the observable list
     */
    public ObservableList<Contract> giveActemiumContracts() {
        return actemium.giveActemiumContracts();
    }

    /**
     * Gets last added contract.
     *
     * @return the last added contract
     */
    public Contract getLastAddedContract() {
        return actemium.getLastAddedContract();
    }


}
