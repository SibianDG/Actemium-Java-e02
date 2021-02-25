package gui.viewModels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.Customer;
import domain.Employee;
import domain.UserModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserViewModel implements Observable {

    private UserModel selectedUser;
    private ObservableList<UserModel> users;

    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    public UserViewModel() {
    	super();
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

    public ObservableList<UserModel> getUserList() {
        return FXCollections.unmodifiableObservableList(users);
    }
    
    public void setUserList(ObservableList<UserModel> users) {
        this.users = users;
    }

    public void setSelectedUser(UserModel user) {
        this.selectedUser = user;
        fireInvalidationEvent();
    }

    /*public Object getSelectedUser() {
        return selectedUser;
    }*/

    public Map<String, String> getDetails(){
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
				Map<String, String> detailsMap = new LinkedHashMap<>();
				detailsMap.put("Employee Nr", String.valueOf(employee.getEmployeeNr()));
				detailsMap.put("Lastname", employee.getLastName());
				detailsMap.put("Firstname", employee.getFirstName());
				detailsMap.put("Address", employee.getAddress());
				detailsMap.put("Phone number", employee.getPhoneNumber());
				detailsMap.put("Email", employee.getEmailAddress());
				detailsMap.put("Company Seniority", String.valueOf(employee.giveSeniority()));
				detailsMap.put("Role", employee.getRole().toString());
				detailsMap.put("Username", employee.getUsername());
				detailsMap.put("Status", employee.getStatus().toString());
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
                Map<String, String> detailsMap = new LinkedHashMap<>(); 
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
                detailsMap.put("Status", customer.getStatus().toString());                
                return detailsMap;
            } 
            //TODO: later then Ticket
            default -> {return null;}
        }
    }

    public String getNameOfSelectedUser() {
        return selectedUser.getFirstName() + " " + selectedUser.getLastName();
    }
}
