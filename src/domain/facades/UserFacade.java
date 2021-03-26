package domain.facades;

import java.util.Set;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.Employee;
import domain.UserModel;
import domain.enums.EmployeeRole;
import domain.enums.TicketType;
import domain.enums.UserStatus;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import languages.LanguageResource;

/**
 * The type User facade.
 */
public class UserFacade extends Facade {

	/**
	 * Instantiates a new User facade.
	 *
	 * @param actemium the actemium
	 */
	public UserFacade(Actemium actemium) {
		super(actemium);
	}

	/**
	 * Sign in.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public void signIn(String username, String password) {
		actemium.signIn(username, password);	
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

	// We hadn't time to implement this feature in the GUI
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

}
