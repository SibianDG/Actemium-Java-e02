package domain.facades;

import domain.*;
import domain.enums.EmployeeRole;
import domain.enums.TicketType;
import domain.enums.UserStatus;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.ObservableList;
import languages.LanguageResource;

import java.util.Set;

/**
 * The type User facade.
 */
public class UserFacade implements Facade {

	// For now we are still working with a single DomainController
	// In the next sprint this will be replaced by:
	// - Abstract GuiController with e.g. LoginController inheriting from it
	// - Facade Interface in pacakge domain.facades
	// - UserModelFacade implements Facade
	// - SignedInUserManagaer in domain.facades
	// - ...	
	
	private final Actemium actemium;

	/**
	 * Instantiates a new User facade.
	 *
	 * @param actemium the actemium
	 */
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

	/**
	 * Sign in.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public void signIn(String username, String password) {
		actemium.signIn(username, password);	
	}

	// Johan suggestion
//	public UserFacade signIn2(String username, String password) {				
//		actemium.signIn(username, password);	
//		return this;
//	}

	/**
	 * Gets employee role.
	 *
	 * @return the employee role
	 */
	public EmployeeRole getEmployeeRole() {
		return actemium.giveEmployeeRoleAsEnum();
	}

	/**
	 * Give user role string.
	 *
	 * @return the string
	 */
	public String giveUserRole() {
		return actemium.giveUserRole();
	}

	/**
	 * Give user first name string.
	 *
	 * @return the string
	 */
	public String giveUserFirstName() {
		return actemium.giveUserFirstName();
	}

	/**
	 * Give user last name string.
	 *
	 * @return the string
	 */
	public String giveUserLastName() {
		return actemium.giveUserLastName();
	}

	/**
	 * Give user username string.
	 *
	 * @return the string
	 */
	public String giveUserUsername() {
		return actemium.giveUsername();
	}

	/**
	 * Give user employee id string.
	 *
	 * @return the string
	 */
	public String giveUserEmployeeId() {
		return actemium.giveUserEmployeeId();
	}

	/**
	 * Give user status string.
	 *
	 * @return the string
	 */
	public String giveUserStatus( ) {
		return actemium.giveUserStatus();
	}

	/**
	 * Give user email address string.
	 *
	 * @return the string
	 */
	public String giveUserEmailAddress() {
		return actemium.giveUserEmailAddress();
	}

	/**
	 * Give user phone number string.
	 *
	 * @return the string
	 */
	public String giveUserPhoneNumber() {
		return actemium.giveUserPhoneNumber();
	}

	/**
	 * Give user address string.
	 *
	 * @return the string
	 */
	public String giveUserAddress() {
		return actemium.giveUserAddress();
	}

	/**
	 * Give user seniority string.
	 *
	 * @return the string
	 */
	public String giveUserSeniority() {
		return actemium.giveUserSeniority();
	}

	/**
	 * Give user password string.
	 *
	 * @return the string
	 */
	public String giveUserPassword() {
		return actemium.giveUserPassword();
	}

	/**
	 * Register customer.
	 *
	 * @param username       the username
	 * @param password       the password
	 * @param firstName      the first name
	 * @param lastName       the last name
	 * @param companyName    the company name
	 * @param companyCountry the company country
	 * @param companyCity    the company city
	 * @param companyAddress the company address
	 * @param companyPhone   the company phone
	 * @throws InformationRequiredException the information required exception
	 */
	public void registerCustomer(String username, String password, String firstName, String lastName, String companyName,
			String companyCountry, String companyCity, String companyAddress, String companyPhone) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermission(EmployeeRole.ADMINISTRATOR);
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

	/**
	 * Register customer using existing company.
	 *
	 * @param username  the username
	 * @param password  the password
	 * @param firstName the first name
	 * @param lastName  the last name
	 * @param companyId the company id
	 * @throws InformationRequiredException the information required exception
	 */
// TODO
	// companyName vs companyId vs ... ?
	// how will we select an existing company when creating a new contactperson for it
	public void registerCustomerUsingExistingCompany(String username, String password, String firstName, String lastName, Long companyId) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermission(EmployeeRole.ADMINISTRATOR);
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

	/**
	 * Register employee.
	 *
	 * @param username     the username
	 * @param password     the password
	 * @param firstName    the first name
	 * @param lastName     the last name
	 * @param address      the address
	 * @param phoneNumber  the phone number
	 * @param emailAddress the email address
	 * @param role         the role
	 * @throws InformationRequiredException the information required exception
	 */
	public void registerEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role) throws InformationRequiredException {
		// check to see if signed in user is Admin
		actemium.checkPermission(EmployeeRole.ADMINISTRATOR);
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

	/**
	 * Modify customer.
	 *
	 * @param customer       the customer
	 * @param username       the username
	 * @param password       the password
	 * @param firstName      the first name
	 * @param lastName       the last name
	 * @param status         the status
	 * @param companyName    the company name
	 * @param companyCountry the company country
	 * @param companyCity    the company city
	 * @param companyAddress the company address
	 * @param companyPhone   the company phone
	 * @throws InformationRequiredException the information required exception
	 */
	public void modifyCustomer(ActemiumCustomer customer, String username,
							   String password, String firstName, String lastName, UserStatus status,
							   String companyName, String companyCountry, String companyCity,
							   String companyAddress, String companyPhone) throws InformationRequiredException {
		try {
			ActemiumCustomer cloneCustomer = customer.clone();

			// check to see if signed in user is Admin
			actemium.checkPermission(EmployeeRole.ADMINISTRATOR);

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
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
	}

	/**
	 * Modify employee.
	 *
	 * @param employee     the employee
	 * @param username     the username
	 * @param password     the password
	 * @param firstName    the first name
	 * @param lastName     the last name
	 * @param address      the address
	 * @param phoneNumber  the phone number
	 * @param emailAddress the email address
	 * @param role         the role
	 * @param status       the status
	 * @param specialties  the specialties
	 * @throws InformationRequiredException the information required exception
	 */
	public void modifyEmployee(ActemiumEmployee employee, String username, String password, String firstName, String lastName, String address,
							   String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status, Set<TicketType> specialties) throws InformationRequiredException {
		try {

			// check to see if signed in user is Admin
			actemium.checkPermisionForModifyEmployee(EmployeeRole.ADMINISTRATOR, username);
			actemium.checkAdminDontEditOwnRole(employee, role);

			ActemiumEmployee cloneEmployee = employee.clone();

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
			cloneEmployee.setSpecialties(specialties);

			cloneEmployee.checkAttributes();

			employee.setUsername(cloneEmployee.getUsername());
			employee.setPassword(cloneEmployee.getPassword());
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setAddress(address);
			employee.setPhoneNumber(phoneNumber);
			employee.setEmailAddress(emailAddress);
			employee.setRole(role);
			employee.setStatus(status);
			employee.setSpecialties(specialties);


			actemium.modifyEmployee(employee);
		} catch (CloneNotSupportedException e){
			System.out.println(LanguageResource.getString("cannot_clone"));
		}

	}

	/**
	 * Modify profile of employee.
	 *
	 * @param username     the username
	 * @param password     the password
	 * @param firstName    the first name
	 * @param lastName     the last name
	 * @param address      the address
	 * @param phoneNumber  the phone number
	 * @param emailAddress the email address
	 * @throws InformationRequiredException the information required exception
	 */
	public void modifyProfileOfEmployee(String username, String password, String firstName, String lastName, String address, String phoneNumber, String emailAddress) throws InformationRequiredException {
		ActemiumEmployee profile = (ActemiumEmployee) actemium.findByUsername(username);
		modifyEmployee(profile, profile.getUsername(), password, firstName, lastName, address, phoneNumber, emailAddress, profile.getRole(), profile.getStatus(), profile.getSpecialties());
	}

	/**
	 * Delete user.
	 *
	 * @param user the user
	 */
	public void deleteUser(UserModel user) {
		// check to see if signed in user is Admin
		actemium.checkPermission(EmployeeRole.ADMINISTRATOR);
		user.setStatus(UserStatus.INACTIVE);
		if (user instanceof Employee)
			actemium.modifyEmployee((ActemiumEmployee) user);
		else
			actemium.modifyCustomer((ActemiumCustomer) user);
	}

	/**
	 * Gets last added customer.
	 *
	 * @return the last added customer
	 */
	public Customer getLastAddedCustomer() {
		return actemium.getLastAddedCustomer();
	}

	/**
	 * Gets last added employee.
	 *
	 * @return the last added employee
	 */
	public Employee getLastAddedEmployee() {
		return actemium.getLastAddedEmployee();
	}

	/**
	 * Give actemium customers observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Customer> giveActemiumCustomers() {
		return actemium.giveActemiumCustomers();
	}

	/**
	 * Give actemium employees observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Employee> giveActemiumEmployees() {
		return actemium.giveActemiumEmployees();
    }

	/**
	 * Gets name by id.
	 *
	 * @param id the id
	 * @return the name by id
	 */
	public String getNameByID(long id) {
		return actemium.getNameByID(id);
	}

	/**
	 * Gets signed in user.
	 *
	 * @return the signed in user
	 */
	public User getSignedInUser() {
		return actemium.getSignedInUser();
    }
}
