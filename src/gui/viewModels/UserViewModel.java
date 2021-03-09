package gui.viewModels;

import java.util.*;

import domain.*;
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

    public Map<String, Map<Boolean, Object>> getDetails(){
//    	switch (selectedUser.getClass().getSimpleName().toLowerCase()) {
        switch (selectedUser.getClass().getSimpleName().substring(8).toLowerCase()) {
            case "employee" -> {
                Employee employee = (Employee) selectedUser;
                Map<String, Map<Boolean, Object>> detailsMap = new LinkedHashMap<>();
                detailsMap.put("Employee ID", Collections.singletonMap(false, String.valueOf(employee.getUserId())));
                detailsMap.put("Username", Collections.singletonMap(true, employee.getUsername()));
                detailsMap.put("Password", Collections.singletonMap(true, employee.getPassword()));
                detailsMap.put("Firstname", Collections.singletonMap(true, employee.getFirstName()));
                detailsMap.put("Lastname", Collections.singletonMap(true, employee.getLastName()));
                detailsMap.put("Address", Collections.singletonMap(true, employee.getAddress()));
                detailsMap.put("Phone number", Collections.singletonMap(true, employee.getPhoneNumber()));
                detailsMap.put("Email", Collections.singletonMap(true, employee.getEmailAddress()));
                detailsMap.put("Company Seniority", Collections.singletonMap(false, String.valueOf(employee.giveSeniority())));
                detailsMap.put("Role", Collections.singletonMap(true, employee.getRoleAsEnum()));
                detailsMap.put("Status", Collections.singletonMap(true, employee.getStatusAsEnum()));
                return detailsMap;
            }
            case "customer" -> {
                Customer customer = (Customer) selectedUser;
                // Using LinkedHashMap so the order of the map values doesn't change
                Map<String, Map<Boolean, Object>> detailsMap = new LinkedHashMap<>();
                detailsMap.put("Customer ID", Collections.singletonMap(false, String.valueOf(customer.getUserId())));
                detailsMap.put("Username", Collections.singletonMap(true, customer.getUsername()));
                detailsMap.put("Password", Collections.singletonMap(true, customer.getPassword()));

                detailsMap.put("Company", Collections.singletonMap(false, ""));
                detailsMap.put("Name", Collections.singletonMap(true, customer.getCompany().getName()));
                detailsMap.put("Address", Collections.singletonMap(true, customer.getCompany().getAddress()));
                detailsMap.put("Phone Nr", Collections.singletonMap(true, customer.getCompany().getPhoneNumber()));
                // Here it's the name and email of the contactperson
                detailsMap.put("Contact person", Collections.singletonMap(false, ""));
                // here it's the name and firstname of the customer
                detailsMap.put("Firstname", Collections.singletonMap(true, customer.getFirstName()));
                detailsMap.put("Lastname", Collections.singletonMap(true, customer.getLastName()));
                detailsMap.put("Seniority", Collections.singletonMap(true, String.valueOf(customer.giveSeniority())));
                //TODO
                // how are we going to show all the contracts in the details pannel?
                // they request it in the use case "Manage Users"
                detailsMap.put("Status", Collections.singletonMap(true, customer.getStatusAsEnum()));
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

	public void modifyCustomer(String username, String password, String firstName, String lastName, String status, String companyName, String companyPhone, String companyAddress) {
        ActemiumCompany company = ((ActemiumCustomer) selectedUser).getCompany();
        company.setName(companyName);
        company.setPhoneNumber(companyPhone);
        company.setAddress(companyAddress);
		userFacade.modifyCustomer((ActemiumCustomer) this.selectedUser, username, password, firstName, lastName,
				company, UserStatus.valueOf(status));
	}

	@Override
    public void delete() {
        userFacade.deleteUser((UserModel) selectedUser);
    }

    public GUIEnum getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GUIEnum currentState) {
		this.currentState = currentState;
	}
}
