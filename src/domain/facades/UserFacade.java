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
	
	public String giveUserUsername() {
		return actemium.giveUsername();
	}
	
	public String giveUserEmployeeId() {
		return actemium.giveUserEmployeeId();
	}
	
	public String giveUserStatus( ) {
		return actemium.giveUserStatus();
	}

	public String giveUserEmailAddress() {
		return actemium.giveUserEmailAddress();
	}
	
	public String giveUserPhoneNumber() {
		return actemium.giveUserPhoneNumber();
	}
	
	public String giveUserAddress() {
		return actemium.giveUserAddress();
	}
	
	public String giveUserSeniority() {
		return actemium.giveUserSeniority();
	}
	
	public String giveUserPassword() {
		return actemium.giveUserPassword();
	}
	
	public void registerCustomer(String username, String password, String firstName, String lastName, String companyName,
			String companyCountry, String companyCity, String companyAddress, String companyPhone) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermision(EmployeeRole.ADMINISTRATOR);
		actemium.existingUsername(username);
		ActemiumCompany company = new ActemiumCompany.CompanyBuilder()
				.name(companyName)
				.country(companyCountry)
				.city(companyCity)
				.address(companyAddress)
				.phoneNumber(companyPhone)
				.build();
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

			ActemiumCompany companyClone = cloneCustomer.getCompany().clone();

			companyClone.setName(companyName);
			companyClone.setCountry(companyCountry);
			companyClone.setCity(companyCity);
			companyClone.setAddress(companyAddress);
			companyClone.setPhoneNumber(companyPhone);

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

			companyClone.checkAttributes();
			cloneCustomer.checkAttributes();

			ActemiumCompany company = customer.getCompany();

			company.setName(companyName);
			company.setCountry(companyCountry);
			company.setCity(companyCity);
			company.setAddress(companyAddress);
			company.setPhoneNumber(companyPhone);

			customer.setUsername(username);
			customer.setPassword(password);
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			customer.setStatus(status);
			// JPA automatically updates the company
//		customer.setCompany(company);

;

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

			employee.setUsername(username);
			employee.setPassword(password);
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setAddress(address);
			employee.setPhoneNumber(phoneNumber);
			employee.setEmailAddress(emailAddress);
			employee.setRole(role);
			employee.setStatus(status);

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
