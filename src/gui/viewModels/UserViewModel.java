package gui.viewModels;

import java.util.*;

import domain.*;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import domain.facades.UserFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;
import languages.LanguageResource;

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
        	// substring(8) to remove ACTEMIUM
            setCurrentState(GUIEnum.valueOf(user.getClass().getSimpleName().substring(8).toUpperCase()));
        }
        fireInvalidationEvent();
    }

    public ArrayList<String> getDetailsNewEmployee(){
        return new ArrayList<String>(Arrays.asList(LanguageResource.getString("username"), LanguageResource.getString("firstname")
                , LanguageResource.getString("lastname"), LanguageResource.getString("address"), LanguageResource.getString("email_address")
                , LanguageResource.getString("phone_nr"), LanguageResource.getString("employee_role")));
    }
    
    public ArrayList<String> getDetailsNewCustomer(){
        return new ArrayList<String>(Arrays.asList(LanguageResource.getString("username"), LanguageResource.getString("firstname")
                , LanguageResource.getString("lastname"), LanguageResource.getString("company_name"), LanguageResource.getString("company_country"),
                LanguageResource.getString("company_city"), LanguageResource.getString("company_address"), LanguageResource.getString("company_phone_number")));
    }

    public Map<String, Map<Boolean, Object>> getDetails() {
        switch (selectedUser.getClass().getSimpleName().substring(8).toLowerCase()) {
            case "employee" -> {
                Employee employee = (Employee) selectedUser;
                Map<String, Map<Boolean, Object>> detailsMap = new LinkedHashMap<>();
                detailsMap.put(LanguageResource.getString("employee_id"), Collections.singletonMap(false, String.valueOf(employee.getUserId())));
                detailsMap.put(LanguageResource.getString("username"), Collections.singletonMap(true, employee.getUsername()));
                detailsMap.put(LanguageResource.getString("password"), Collections.singletonMap(true, employee.getPassword()));
                detailsMap.put(LanguageResource.getString("firstname"), Collections.singletonMap(true, employee.getFirstName()));
                detailsMap.put(LanguageResource.getString("lastname"), Collections.singletonMap(true, employee.getLastName()));
                detailsMap.put(LanguageResource.getString("address"), Collections.singletonMap(true, employee.getAddress()));
                detailsMap.put(LanguageResource.getString("phone_number"), Collections.singletonMap(true, employee.getPhoneNumber()));
                detailsMap.put(LanguageResource.getString("email"), Collections.singletonMap(true, employee.getEmailAddress()));
                detailsMap.put(LanguageResource.getString("company_seniority"), Collections.singletonMap(false, String.valueOf(employee.giveSeniority())));
                detailsMap.put(LanguageResource.getString("role"), Collections.singletonMap(true, employee.getRole()));
                detailsMap.put(LanguageResource.getString("status"), Collections.singletonMap(true, employee.getStatus()));
                return detailsMap;
            }
            case "customer" -> {
                Customer customer = (Customer) selectedUser;
                // Using LinkedHashMap so the order of the map values doesn't change
                Map<String, Map<Boolean, Object>> detailsMap = new LinkedHashMap<>();
                detailsMap.put(LanguageResource.getString("customer_id"), Collections.singletonMap(false, String.valueOf(customer.getUserId())));
                detailsMap.put(LanguageResource.getString("username"), Collections.singletonMap(true, customer.getUsername()));
                detailsMap.put(LanguageResource.getString("password"), Collections.singletonMap(true, customer.getPassword()));

                detailsMap.put(LanguageResource.getString("company"), Collections.singletonMap(false, ""));
                detailsMap.put(LanguageResource.getString("name"), Collections.singletonMap(true, customer.giveCompany().getName()));
                detailsMap.put(LanguageResource.getString("country"), Collections.singletonMap(true, customer.giveCompany().getCountry()));
                detailsMap.put(LanguageResource.getString("city"), Collections.singletonMap(true, customer.giveCompany().getCity()));
                detailsMap.put(LanguageResource.getString("address"), Collections.singletonMap(true, customer.giveCompany().getAddress()));
                detailsMap.put(LanguageResource.getString("phone_nr"), Collections.singletonMap(true, customer.giveCompany().getPhoneNumber()));
                
                detailsMap.put(LanguageResource.getString("contact_person"), Collections.singletonMap(false, ""));
                detailsMap.put(LanguageResource.getString("firstname"), Collections.singletonMap(true, customer.getFirstName()));
                detailsMap.put(LanguageResource.getString("lastname"), Collections.singletonMap(true, customer.getLastName()));
                detailsMap.put(LanguageResource.getString("seniority"), Collections.singletonMap(true, String.valueOf(customer.giveSeniority())));
                //TODO
                // how are we going to show all the contracts in the details pannel?
                // they request it in the use case "Manage Users"
                detailsMap.put(LanguageResource.getString("status"), Collections.singletonMap(true, customer.getStatus()));
                detailsMap.put(LanguageResource.getString("contracts"), Collections.singletonMap(true, customer.giveContracts()));
                return detailsMap;
            } 
            default -> {return null;}
        }
    }

	public String getNameOfSelectedUser() {
		return selectedUser.getFirstName() + " " + selectedUser.getLastName();
	}

	public void registerEmployee(String username, String lastName, String firstName, String address,
			String emailAddress, String phoneNumber, EmployeeRole role) throws InformationRequiredException {
		userFacade.registerEmployee(username, "Passwd123&", firstName, lastName, address, phoneNumber, emailAddress, role);
		setSelectedUser(userFacade.getLastAddedEmployee());
	}

	public void modifyEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) throws InformationRequiredException {
		userFacade.modifyEmployee((ActemiumEmployee) selectedUser, username, password, firstName, lastName, address,
				phoneNumber, emailAddress, role, status);
	}

	public void registerCustomer(String username, String firstName, String lastName, String companyName,
			String companyCountry, String companyCity, String companyAddress, String companyPhone) throws InformationRequiredException {
		userFacade.registerCustomer(username, "Passwd123&", firstName, lastName, companyName, companyCountry, companyCity, companyAddress,	companyPhone);
		setSelectedUser(userFacade.getLastAddedCustomer());
	}

	public void modifyCustomer(String username, String password, String firstName, String lastName, String status,
			String companyName, String companyCountry, String companyCity, String companyAddress, String companyPhone) throws InformationRequiredException {
		userFacade.modifyCustomer((ActemiumCustomer) this.selectedUser, username, password, firstName, lastName,
				UserStatus.valueOf(status), companyName, companyCountry, companyCity, companyAddress, companyPhone);
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
