package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.Customer;
import domain.Employee;
import domain.User;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import domain.facades.UserFacade;
import gui.GUIEnum;
import javafx.collections.ObservableList;

public class UserViewModel extends ViewModel {

    private GUIEnum currentState;
    private final UserFacade userFacade;
    private User selectedUser;
    
    public UserViewModel(UserFacade userFacade) {
    	super();
    	this.userFacade = userFacade;
    }

    public ObservableList<Employee> giveEmployees() {
        return userFacade.giveActemiumEmployees();
    }
    
    public ObservableList<Customer> giveCustomers() {
        return userFacade.giveActemiumCustomers();
    }    
 
    public void setSelectedUser(User user) {
        this.selectedUser = user;
        if (user != null){
        	// How can the type still be ActemiumEmployee when we casted it to Employee?
        	System.out.println(user.getClass().getSimpleName().toUpperCase());
        	// substring(8) to remove ACTEMIUM
            setCurrentState(GUIEnum.valueOf(user.getClass().getSimpleName().substring(8).toUpperCase()));
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
//    	switch (selectedUser.getClass().getSimpleName().toLowerCase()) {
        switch (selectedUser.getClass().getSimpleName().substring(8).toLowerCase()) {
            case "employee" -> {
                Employee employee = (Employee) selectedUser;
				Map<String, Object> detailsMap = new LinkedHashMap<>();
				detailsMap.put("Employee ID", String.valueOf(employee.getUserId()));
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
        	    // Using LinkedHashMap so the order of the map values doesn't change
                Map<String, Object> detailsMap = new LinkedHashMap<>();
                detailsMap.put("Customer ID", String.valueOf(customer.getUserId()));
                detailsMap.put("Username", customer.getUsername());
                detailsMap.put("Password", customer.getPassword());

                detailsMap.put("Company", "");
                detailsMap.put("Name", customer.getCompany().getName());
                detailsMap.put("Address", customer.getCompany().getAddress());
                detailsMap.put("Phone Nr", customer.getCompany().getPhoneNumber());
                // Here it's the name and email of the contactperson
                detailsMap.put("Contact person", "");
                // here it's the name and firstname of the customer
                detailsMap.put("Firstname", customer.getFirstName());
                detailsMap.put("Lastname", customer.getLastName());
                detailsMap.put("Seniority", String.valueOf(customer.giveSeniority()));
                //TODO
                // how are we going to show all the contracts in the details pannel?
                // they request it in the use case "Manage Users"
                detailsMap.put("Status", customer.getStatusAsEnum());
                return detailsMap;
            } 
            default -> {return null;}
        }
    }

	public String getNameOfSelectedUser() {
		return selectedUser.getFirstName() + " " + selectedUser.getLastName();
	}

	public void registerEmployee(String username, String lastName, String firstName, String address,
			String emailAddress, String phoneNumber, EmployeeRole role) {
		userFacade.registerEmployee(username, "Passwd123&", firstName, lastName, address, phoneNumber, emailAddress, role);
		setSelectedUser(userFacade.getLastAddedEmployee());
	}

	public void modifyEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) {
		userFacade.modifyEmployee((ActemiumEmployee) selectedUser, username, password, firstName, lastName, address,
				phoneNumber, emailAddress, role, status);
	}

	public void registerCustomer(String username, String firstName, String lastName, String companyName,
			String companyAddress, String companyPhone) {
		ActemiumCompany company = new ActemiumCompany(companyName, companyAddress, companyPhone);
		userFacade.registerCustomer(username, "Passwd123&", firstName, lastName, company);
		setSelectedUser(userFacade.getLastAddedCustomer());
	}

	public void modifyCustomer(String username, String password, String firstName, String lastName, String status) {
		userFacade.modifyCustomer((ActemiumCustomer) this.selectedUser, username, password, firstName, lastName,
				((ActemiumCustomer) selectedUser).getCompany(), UserStatus.valueOf(status));
	}

	public GUIEnum getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GUIEnum currentState) {
		this.currentState = currentState;
	}
	
}
