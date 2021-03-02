package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.*;
import domain.facades.Facade;
import domain.facades.UserFacade;
import gui.GUIEnum;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserViewModel implements Observable {

    private GUIEnum currentState;
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
        if (user != null){
            setCurrentState(GUIEnum.valueOf(user.getClass().getSimpleName().toUpperCase()));
        }
        fireInvalidationEvent();
    }

    public ArrayList<String> getDetailsNewEmployee(){
        return new ArrayList<String>(Arrays.asList("Username", "Lastname", "Firstname", "Address", "Email address", "Phone nr", "Employee role"));
    }
    public ArrayList<String> getDetailsNewCustomer(){
        return new ArrayList<String>(Arrays.asList("Username", "Lastname", "Firstname", "Company name", "Company Address", "Company Phone number"));
    }

    public Map<String, Object> getDetails(){
        switch (selectedUser.getClass().getSimpleName().toLowerCase()) {
            case "employee" -> {
                Employee employee = (Employee) selectedUser;
				Map<String, Object> detailsMap = new LinkedHashMap<>();
				detailsMap.put("Employee ID", String.valueOf(employee.getEmployeeNr()));
                detailsMap.put("Username", employee.getUsername());
                detailsMap.put("Password", employee.getPassword());
                detailsMap.put("Firstname", employee.getFirstName());
                detailsMap.put("Lastname", employee.getLastName());
				detailsMap.put("Address", employee.getAddress());
				detailsMap.put("Phone number", employee.getPhoneNumber());
				detailsMap.put("Email", employee.getEmailAddress());
				detailsMap.put("Company Seniority", String.valueOf(employee.giveSeniority()));
				detailsMap.put("Role", employee.getRoleAsEnum());
				detailsMap.put("Status", employee.getStatusAsEnum());
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
                detailsMap.put("Customer ID", String.valueOf(customer.getCustomerNr()));
                detailsMap.put("Username", customer.getUsername());
                detailsMap.put("Password", customer.getPassword());

                // here it's the name and firstname of the customer
                detailsMap.put("Firstname", customer.getFirstName());
                detailsMap.put("Lastname", customer.getLastName());
                detailsMap.put("Company", "");
                detailsMap.put("Name", customer.getCompany().getName());
                detailsMap.put("Address", customer.getCompany().getAddress());
                detailsMap.put("Phone Nr", customer.getCompany().getPhoneNumber());
                // Here it's the name and email of the contactperson
                detailsMap.put("Contact person", "");
                detailsMap.put("Name and Firstname", String.format("%s %s", "none", "none"));
                detailsMap.put("Seniority", String.valueOf(customer.giveSeniority()));
                // how are we going to show all the contracts in the details pannel?
                // they request it in the use case "Manage Users"
//                detailsMap.put("Contracts", "");
//                detailsMap.put("Contract Nr", "");
//                detailsMap.put("Contract Type", "");
//                detailsMap.put("Contract Status", "");
//                detailsMap.put("Contract Start- and End date", "");
                detailsMap.put("Status", customer.getStatusAsEnum());
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
        setSelectedUser(userFacade.findByUsername(username));
    }

    public void modifyEmployee(String username, String password, String firstName, String lastName, String address,
                               String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) {
        userFacade.modifyEmployee( (Employee) selectedUser,  username, password, firstName,  lastName,  address,
                 phoneNumber,  emailAddress,  role, status);
    }

    public void modifyCustomer(String username, String password, String firstName, String lastName, String status) {
        userFacade.modifyCustomer((Customer) this.selectedUser, username, password, firstName, lastName, ((Customer)selectedUser).getCompany(), UserStatus.valueOf(status));
    }

    public void registerCustomer(String username, String firstName, String lastName, String companyName, String companyAddress, String companyPhone) {
        Company company = new Company(companyName, companyAddress, companyPhone);
        userFacade.registerCustomer(username, "Passwd123&", firstName, lastName, company);
        setSelectedUser(userFacade.findByUsername(username));
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }
}
