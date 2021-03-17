package domain.facades;

import domain.*;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
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

	public void registerCustomer(String username, String password, String firstName, String lastName, String companyName,
			String companyCountry, String companyCity, String companyAddress, String companyPhone) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		actemium.existingUsername(username);
		ActemiumCompany company = new ActemiumCompany(companyName, companyCountry, companyCity, companyAddress,	companyPhone);
		ActemiumCustomer newCustomer = new ActemiumCustomer.CustomerBuilder()
				.username(username)
				.password(password)
				.firstName(firstName)
				.lastName(lastName)
				.company(company)
				.build();
		actemium.registerCustomer(newCustomer);
	}
	
	// TODO
	// companyName vs companyId vs ... ?
	// how will we select an existing company when creating a new contactperson for it
	public void registerCustomerUsingExistingCompany(String username, String password, String firstName, String lastName, Long companyId) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		actemium.existingUsername(username);
		ActemiumCompany company = actemium.findCompanyById(companyId);
		ActemiumCustomer newCustomer = new ActemiumCustomer.CustomerBuilder()
				.username(username)
				.password(password)
				.firstName(firstName)
				.lastName(lastName)
				.company(company)
				.build();

		actemium.registerCustomer(newCustomer);
	}

	public void registerEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		actemium.existingUsername(username);
		ActemiumEmployee newEmployee = new ActemiumEmployee.EmployeeBuilder()
										.username(username)
										.password(password)
										.firstName(firstName)
										.lastName(lastName)
										.address(address)
										.phoneNumber(phoneNumber)
										.emailAddress(emailAddress)
										.role(role)
										.build();
		//ActemiumEmployee newEmployee = new ActemiumEmployee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role);

		actemium.registerEmployee(newEmployee);
	}

	public void modifyCustomer(ActemiumCustomer customer, String username,
							   String password, String firstName, String lastName, UserStatus status,
							   String companyName, String companyCountry, String companyCity,
							   String companyAddress, String companyPhone) throws InformationRequiredException {
		try {
			ActemiumCustomer cloneCustomer = customer.clone();

			// check to see if signed in user is Admin
			actemium.checkPermision(EmployeeRole.ADMINISTRATOR);

			// Changes to company of the contactPerson (=Customer)
			//TODO clone
			ActemiumCompany company = customer.getCompany();

			//TODO
			//cloneCustomer.setName(companyName);
			//cloneCustomer.setCountry(companyCountry);
			//cloneCustomer.setCity(companyCity);
			//cloneCustomer.setAddress(companyAddress);
			//cloneCustomer.setPhoneNumber(companyPhone);

			// only needs to be checked if you changed the username
			if (!cloneCustomer.getUsername().equals(username)) {
				actemium.existingUsername(username);
				customer.setUsername(username);
			}

			if (!(password.equals("********") || password.isBlank())) {
				cloneCustomer.setPassword(password);
			}

			cloneCustomer.setFirstName(firstName);
			cloneCustomer.setLastName(lastName);
			// JPA automatically updates the company
//		customer.setCompany(company);
			cloneCustomer.setStatus(status);

			cloneCustomer.checkAttributes();

			customer.setUsername(cloneCustomer.getUsername());
			customer.setPassword(cloneCustomer.getPassword());
			customer.setFirstName(cloneCustomer.getFirstName());
			customer.setLastName(cloneCustomer.getLastName());
			customer.setStatus(cloneCustomer.getStatusAsEnum());
			// JPA automatically updates the company
//		customer.setCompany(company);

			actemium.modifyCustomer(customer);
		} catch (CloneNotSupportedException e) {
			System.out.println("Can't clone object");
		}


	}

	public void modifyEmployee(ActemiumEmployee employee, String username, String password, String firstName, String lastName, String address,
							   String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) throws InformationRequiredException {
		try {
			ActemiumEmployee cloneEmployee = employee.clone();

			// check to see if signed in user is Admin
			actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
			// only needs to be checked if you changed the username
			if (!cloneEmployee.getUsername().equals(username)) {
				actemium.existingUsername(username);
				cloneEmployee.setUsername(username);

			}

			if (!(password.equals("********") || password.isBlank())) {
				cloneEmployee.setPassword(password);
			}

			cloneEmployee.setFirstName(firstName);
			cloneEmployee.setLastName(lastName);
			cloneEmployee.setAddress(address);
			cloneEmployee.setPhoneNumber(phoneNumber);
			cloneEmployee.setEmailAddress(emailAddress);
			cloneEmployee.setRole(role);
			cloneEmployee.setStatus(status);

			cloneEmployee.checkAttributes();

			System.out.println("Hiervoorbij?");

			employee.setUsername(cloneEmployee.getUsername());
			employee.setPassword(cloneEmployee.getPassword());
			employee.setFirstName(cloneEmployee.getFirstName());
			employee.setLastName(cloneEmployee.getLastName());
			employee.setAddress(cloneEmployee.getAddress());
			employee.setPhoneNumber(cloneEmployee.getPhoneNumber());
			employee.setEmailAddress(cloneEmployee.getEmailAddress());
			employee.setRole(cloneEmployee.getRoleAsEnum());
			employee.setStatus(cloneEmployee.getStatusAsEnum());

			actemium.modifyEmployee(employee);
		} catch (CloneNotSupportedException e){
			System.out.println("Can't clone object");
		}

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
