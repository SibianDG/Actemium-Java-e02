package gui.viewModels;

import domain.Customer;
import domain.Employee;
import domain.UserModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Map;

public class UserViewModel implements Observable {

    private UserModel selectedUser;
    private ObservableList<UserModel> users;

    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    private Map<String, String> DetailsOfCustomer;



    public UserViewModel() {
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
                return Map.of(
                        "Username", employee.getUsername()
                        , "Status", employee.getStatus().toString()
                        , "Lastname", employee.getLastName()
                        , "Firstname", employee.getFirstName()
                        , "Address", employee.getAddress()
                        , "Email", employee.getEmailAddress()
                        , "Phone number", employee.getPhoneNumber()
                        , "Seniority", String.valueOf(employee.giveSeniority())
                        , "Role", employee.getRole().toString()
                );
            }
            case "customer" -> {
                Customer customer = (Customer) selectedUser;
                return Map.of(
                        "Username", customer.getUsername()
                        , "Status", customer.getStatus().toString()
                        , "Company", ""
                        , "Name", customer.getCompanyName()
                        , "Address", customer.getCompanyAddress()
                        , "Phone number", customer.getCompanyPhone()
                        , "Contact person", ""
                        , "Lastname", customer.getLastName()
                        , "Firstname", customer.getFirstName()
                        , "Seniority", String.valueOf(customer.giveSeniority())
                );
            }
            //TODO: later then Ticket
            default -> {return null;}
        }
    }

    public String getNameOfSelectedUser() {
        return selectedUser.getFirstName() + " " + selectedUser.getLastName();
    }
}
