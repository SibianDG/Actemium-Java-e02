package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.*;
import domain.facades.Facade;
import domain.facades.UserFacade;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserViewModel implements Observable {

    private final UserFacade userFacade;
    private UserModel selectedUser;
    private ObservableList<Employee> employees;
    private ObservableList<Customer> customers;

    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    public UserViewModel(Facade userFacade) {
    	super();
    	this.userFacade = (UserFacade) userFacade;
    	this.employees = FXCollections.observableArrayList();
    	this.customers = FXCollections.observableArrayList();
    }

    protected void fireInvalidationEvent() {
        for (InvalidationListener listener : listeners) {
            listener.invalidated(this);
        }
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

    public ObservableList<Employee> getEmployees() {
        return FXCollections.unmodifiableObservableList(employees);
    }
    
    public ObservableList<Customer> getCustomers() {
        return FXCollections.unmodifiableObservableList(customers);
    }
    
    public void setEmployees(ObservableList<Employee> employees) {
        this.employees = employees;
    }
    
    public void setCustomers(ObservableList<Customer> customers) {
        this.customers = customers;
    }

    public void setSelectedUser(UserModel user) {
        this.selectedUser = user;
        fireInvalidationEvent();
    }

    public ArrayList<String> getDetailsNewEmployee(){
        return new ArrayList<String>(Arrays.asList("Username", "Lastname", "Firstname", "Address", "Email address", "Phone nr", "Role"));
    }
    public ArrayList<String> getDetailsNewCustomer(){
        return new ArrayList<String>(Arrays.asList("Lastname", "Firstname", "Address", "Email address", "Phone nr", "Role"));
    }

    public Map<String, Object> getDetails(){
        switch (selectedUser.getClass().getSimpleName().toLowerCase()) {
            case "employee" -> {
                Employee employee = (Employee) selectedUser;
//                return Map.of(
//                        "Username", employee.getUsername()
//                        , "Status", employee.getStatus().toString()
//                        , "Lastname", employee.getLastName()
//                        , "Firstname", employee.getFirstName()
//                        , "Address", employee.getAddress()
//                        , "Email", employee.getEmailAddress()
//                        , "Phone number", employee.getPhoneNumber()
//                        , "Seniority", String.valueOf(employee.giveSeniority())
//                        , "Role", employee.getRole().toString()
//                );
        	    // Using LinkedHashMap so the order of the map values doesn't change
				Map<String, Object> detailsMap = new LinkedHashMap<>();
				detailsMap.put("Employee Nr", String.valueOf(employee.getEmployeeNr()));
				detailsMap.put("Lastname", employee.getLastName());
				detailsMap.put("Firstname", employee.getFirstName());
				detailsMap.put("Address", employee.getAddress());
				detailsMap.put("Phone number", employee.getPhoneNumber());
				detailsMap.put("Email", employee.getEmailAddress());
				detailsMap.put("Company Seniority", String.valueOf(employee.giveSeniority()));
				detailsMap.put("Role", employee.getRole());
				detailsMap.put("Username", employee.getUsername());
				detailsMap.put("Status", employee.getStatus());
				return detailsMap;
            }
            case "customer" -> {
                Customer customer = (Customer) selectedUser;
                // Map.of can only contain up to 10 key value pairs
                // ofEntries can contain unlimited key value pairs
                // Entries are in the right order
//                return Map.ofEntries(
//                		new AbstractMap.SimpleEntry<>("Customer Nr", String.valueOf(customer.getCustomerNr()))
//                        , new AbstractMap.SimpleEntry<>("Company", "")
//                        , new AbstractMap.SimpleEntry<>("Name", customer.getCompany().getName())
//                        , new AbstractMap.SimpleEntry<>("Address", customer.getCompanyAddress())
//                        , new AbstractMap.SimpleEntry<>("Phone number", customer.getCompanyPhone())
//                        , new AbstractMap.SimpleEntry<>("Contact person", "")
//                		  , new AbstractMap.SimpleEntry<>("Name and Firstname", String.format("%s %s", customer.getLastName(), customer.getFirstName()))
//                        , new AbstractMap.SimpleEntry<>("Email address", "")
//                        , new AbstractMap.SimpleEntry<>("Seniority", String.valueOf(customer.giveSeniority()))
//                        , new AbstractMap.SimpleEntry<>("Contracts", "")
//                        , new AbstractMap.SimpleEntry<>("Contract Nr", "")
//                        , new AbstractMap.SimpleEntry<>("Contract Type", "")
//                        , new AbstractMap.SimpleEntry<>("Contract Status", "")
//                        , new AbstractMap.SimpleEntry<>("Contract Start- and End date", "")
//                        , new AbstractMap.SimpleEntry<>("Username", customer.getUsername())
//                        , new AbstractMap.SimpleEntry<>("Status", customer.getStatus().toString())
//                );
        	    // Using LinkedHashMap so the order of the map values doesn't change
                Map<String, Object> detailsMap = new LinkedHashMap<>();
                detailsMap.put("Customer Nr", String.valueOf(customer.getCustomerNr()));
                // here it's the name and firstname of the customer
                detailsMap.put("Customer Name", String.format("%s %s", customer.getLastName(), customer.getFirstName()));
                detailsMap.put("Company", "");
                detailsMap.put("Name", customer.getCompany().getName());
                detailsMap.put("Address", customer.getCompany().getAddress());
                detailsMap.put("Phone Nr", customer.getCompany().getPhoneNumber());
                // Here it's the name and email of the contactperson
                detailsMap.put("Contact person", "");
                detailsMap.put("Name and Firstname", String.format("%s %s", customer.getLastName(), customer.getFirstName()));
                detailsMap.put("Email address", "");
                detailsMap.put("Seniority", String.valueOf(customer.giveSeniority()));
                // how are we going to show all the contracts in the details pannel?
                // they request it in the use case "Manage Users"
//                detailsMap.put("Contracts", "");
//                detailsMap.put("Contract Nr", "");
//                detailsMap.put("Contract Type", "");
//                detailsMap.put("Contract Status", "");
//                detailsMap.put("Contract Start- and End date", "");
                detailsMap.put("Username", customer.getUsername());
                detailsMap.put("Status", customer.getStatus());
                return detailsMap;
            } 
            //TODO: later then Ticket
            default -> {return null;}
        }
    }

    public String getNameOfSelectedUser() {
        return selectedUser.getFirstName() + " " + selectedUser.getLastName();
    }

    public void registerEmployee(String username, String lastName, String firstName, String address,
                                 String emailAddress, String phoneNumber, EmployeeRole role) {
        userFacade.registerEmployee(username, "Passwd123&", firstName, lastName, address, phoneNumber, emailAddress, role);
        selectedUser = userFacade.findByUsername(username);
    }

    public void modifyEmployee(String username, String firstName, String lastName, String address,
                               String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) {
        userFacade.modifyEmployee( (Employee) selectedUser,  username,  firstName,  lastName,  address,
                 phoneNumber,  emailAddress,  role, status);
    }
}
