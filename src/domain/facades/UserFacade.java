package domain.facades;

import domain.*;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import domain.manager.Actemium;
import javafx.collections.ObservableList;

public class UserFacade implements Facade {

	// For now we are still working with a single DomainController
	// In the next sprint this will be replaced by:
	// - Abstract GuiController with e.g. LoginController inheriting from it
	// - Facade Interface in pacakge domain.facades
	// - UserModelFacade implements Facade
	// - SignedInUserManagaer in domain.facades
	// - ...	
	
	private Actemium actemium;
		
	public UserFacade(Actemium actemium) {
		this.actemium = actemium;
	}

	// Johan suggested this is how he does it
//	public UserFacade() {
//		UserDaoJpa userDaoJpa= new UserDaoJpa();
//        GenericDaoJpa<ActemiumTicket> ticketDaoJpa = new GenericDaoJpa<>(ActemiumTicket.class);
//        GenericDaoJpa<ActemiumContractType> contractTypeDaoJpa = new GenericDaoJpa<>(ActemiumContractType.class);
//        GenericDaoJpa<ActemiumContract> contractDaoJpa = new GenericDaoJpa<>(ActemiumContract.class);
//		
//		this.actemium = new Actemium(userDaoJpa, ticketDaoJpa, contractTypeDaoJpa, contractDaoJpa);
//	}
	
	public void signIn(String username, String password) {				
		actemium.signIn(username, password);	
	}

	// Johan suggestion
//	public UserFacade signIn2(String username, String password) {				
//		actemium.signIn(username, password);	
//		return this;
//	}
	
	public String giveUserRole() {
		return actemium.giveUserRole();
	}
	
	public String giveUserFirstName() {
		return actemium.giveUserFirstName();
	}

	public String giveUserLastName() {
		return actemium.giveUserLastName();
	}

	public void registerCustomer(String username, String password, String firstName, String lastName, ActemiumCompany company) {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		actemium.existingUsername(username);	
		ActemiumCustomer newCustomer = new ActemiumCustomer(username, password, firstName, lastName, company);
		actemium.registerCustomer(newCustomer);
	}

	public void registerEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role) {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		actemium.existingUsername(username);
		ActemiumEmployee newEmployee = new ActemiumEmployee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role);
		actemium.registerEmployee(newEmployee);
	}

	public void modifyCustomer(ActemiumCustomer customer, String username, String password, String firstName, String lastName, ActemiumCompany company, UserStatus status) {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		// only needs to be checked if you changed the username 
		if (!customer.getUsername().equals(username)) {
			actemium.existingUsername(username);
			customer.setUsername(username);
		}

		if (!(password.equals("********") || password.isBlank())) {
			customer.setPassword(password);
		}
		
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setCompany(company);
		customer.setStatus(status);
		
		actemium.modifyCustomer(customer);		
	}

	public void modifyEmployee(ActemiumEmployee employee, String username, String password, String firstName, String lastName, String address,
							   String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		// only needs to be checked if you changed the username 
		if (!employee.getUsername().equals(username)) {
			actemium.existingUsername(username);
			employee.setUsername(username);
		}

		if (!(password.equals("********") || password.isBlank())) {
			employee.setPassword(password);
		}
		
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setAddress(address);
		employee.setPhoneNumber(phoneNumber);
		employee.setEmailAddress(emailAddress);
		employee.setRole(role);
		employee.setStatus(status);
		
		actemium.modifyEmployee(employee);
	}

	public void deleteUser(UserModel user) {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		user.setStatus(UserStatus.INACTIVE);
		if (user instanceof Employee)
			actemium.modifyEmployee((ActemiumEmployee) user);
		else
			actemium.modifyCustomer((ActemiumCustomer) user);
	}
	
	public Customer getLastAddedCustomer() {
		return actemium.getLastAddedCustomer();
	}
	
	public Employee getLastAddedEmployee() {
		return actemium.getLastAddedEmployee();
	}

	public ObservableList<Customer> giveActemiumCustomers() {
		return actemium.giveActemiumCustomers();
	}

	public ObservableList<Employee> giveActemiumEmployees() {
		return actemium.giveActemiumEmployees();
    }
	
}
