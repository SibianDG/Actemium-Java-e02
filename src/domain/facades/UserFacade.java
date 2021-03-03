package domain.facades;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import domain.Company;
import domain.Customer;
import domain.Employee;
import domain.enums.EmployeeRole;
import domain.LoginAttempt;
import domain.enums.LoginStatus;
import domain.UserModel;
import domain.enums.UserStatus;
import exceptions.BlockedUserException;
import exceptions.PasswordException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import languages.LanguageResource;
import repository.UserDao;
import repository.UserDaoJpa;

public class UserFacade implements Facade {

	// For now we are still working with a single DomainController
	// In the next sprint this will be replaced by:
	// - Abstract GuiController with e.g. LoginController inheriting from it
	// - Facade Interface in pacakge domain.facades
	// - UserModelFacade implements Facade
	// - SignedInUserManagaer in domain.facades
	// - ...	
	
	private UserModel signedInUser;
	//private UserModel selectedUser;
	private UserDao userRepo;
		
	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;

	private ObservableList<Customer> customerList;
	private ObservableList<Employee> employeeList;
	
	public UserFacade(UserDao userRepo) {
		this.userRepo = userRepo;
		fillInUserLists();

	}

	private void fillInUserLists(){
		List<UserModel> userList = userRepo.findAll();
		List<Customer> customerList = userList.stream()
				.filter(c -> c instanceof Customer)
				.map(c -> (Customer) c)
				.collect(Collectors.toList());
		List<Employee> employeeList = userList.stream()
				.filter(e -> e instanceof Employee)
				.map(e -> (Employee) e)
				.collect(Collectors.toList());

		this.customerList = FXCollections.observableArrayList(customerList);
		this.employeeList = FXCollections.observableArrayList(employeeList);
	}
	
	//TODO constructor vs setter injection?
	public UserFacade() {
		this(new UserDaoJpa());
//		setUserRepo(new UserDaoJpa());
	}

	public void setUserRepo(UserDao mock) {
		userRepo = mock;
	}
		
	private void setSignedInUser(UserModel signedInEmployee) {
		this.signedInUser = signedInEmployee;
	}

	/*public UserModel getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(UserModel selectedUser) {
		this.selectedUser = selectedUser;
	}

	 */

	public UserModel findByUsername(String username){
		return userRepo.findByUsername(username);
	}

	public void signIn(String username, String password) {
		UserModel userModel = findByUsername(username);

		if (password.isBlank()) {
			throw new PasswordException(LanguageResource.getString("password_blank"));
		}

		userRepo.startTransaction();
		
		// user account already blocked, only the failed login attempt needs to registered
		if (userModel.getStatusAsEnum().equals(UserStatus.BLOCKED)) {
			userModel.increaseFailedLoginAttempts();
			LoginAttempt loginAttempt = new LoginAttempt(userModel, LoginStatus.FAILED);	
			userModel.addLoginAttempt(loginAttempt);
//			userRepo.registerLoginAttempt(userModel, LoginStatus.FAILED);
//			userRepo.registerLoginAttempt(loginAttempt);
			userRepo.commitTransaction();
			throw new BlockedUserException(String.format(
					"User account has been blocked because more than%n%d failed login attempts have been registered."
					+ "%nPlease contact your system administrator.",
					USER_LOGIN_MAX_ATTEMPTS));
		}
				
		// check password
		if (!userModel.getPassword().equals(password)) {
			userModel.increaseFailedLoginAttempts();
			LoginAttempt loginAttempt = new LoginAttempt(userModel, LoginStatus.FAILED);	
			userModel.addLoginAttempt(loginAttempt);
//			userRepo.registerLoginAttempt(userModel, LoginStatus.FAILED);
//			userRepo.registerLoginAttempt(loginAttempt);
			// block user after 5 failed login attempts
			if (userModel.getFailedLoginAttempts() >= USER_LOGIN_MAX_ATTEMPTS) {
				userModel.blockUser();
				userRepo.commitTransaction();
				throw new BlockedUserException(
						String.format("%s%nUser has reached more than %d failed login attempts,%n account has been blocked.",
								LanguageResource.getString("wrongUsernamePasswordCombination"),
								USER_LOGIN_MAX_ATTEMPTS));
			}
			userRepo.commitTransaction();
			throw new PasswordException(String.format("%s%nOnly %d attempts remaining", 
					LanguageResource.getString("wrongUsernamePasswordCombination"), 
					USER_LOGIN_MAX_ATTEMPTS - userModel.getFailedLoginAttempts()));
		}

		// correct password
		userModel.resetLoginAttempts();

		LoginAttempt loginAttempt = new LoginAttempt(userModel, LoginStatus.SUCCESS);	
		userModel.addLoginAttempt(loginAttempt);
		
//		userRepo.registerLoginAttempt(userModel, LoginStatus.SUCCESS);
//		userRepo.registerLoginAttempt(loginAttempt);

		userRepo.commitTransaction();

		setSignedInUser(userModel);
		
		System.out.println("Just signed in: " + signedInUser.getUsername());
	}

	public String giveUserRole() {
		if(signedInUser instanceof Employee) {
			return ((Employee) signedInUser).getRole().toString();
		}
		return "Customer";
	}

	public String giveUsername() {
		return signedInUser.getUsername();
	}

	public String giveUserFirstName() {
		return signedInUser.getFirstName();
	}

	public String giveUserLastName() {
		return signedInUser.getLastName();
	}

	public void existingUsername(String username) {
		try {
			if(userRepo.findByUsername(username) != null) {
				throw new IllegalArgumentException("Username is already taken.");
			}
		} catch (EntityNotFoundException e){
			//ignore
		}

	}

	public void registerCustomer(String username, String password, String firstName, String lastName, Company company) {
		existingUsername(username);		
		Customer customer = new Customer(username, password, firstName, lastName, company);
		userRepo.startTransaction();
		userRepo.insert(customer);
		userRepo.commitTransaction();
		customerList.add(customer);

	}

	public void registerEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role) {
		existingUsername(username);
		Employee employee = new Employee(username, password, firstName, lastName, address, phoneNumber, emailAddress, role);
		userRepo.startTransaction();
		userRepo.insert(employee);
		userRepo.commitTransaction();
		employeeList.add(employee);

	}

	public void modifyCustomer(Customer customer, String username, String password, String firstName, String lastName, Company company, UserStatus status) {
		// only needs to be checked if you changed the username 
		if (!customer.getUsername().equals(username)) {
			existingUsername(username);
		}
		int index = customerList.indexOf(customer);

		if (!(password.equals("********") || password.isBlank())) {
			customer.setPassword(password);
		}
		
		customer.setUsername(username);
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setCompany(company);
		customer.setStatus(status);
		userRepo.startTransaction();
		userRepo.update(customer);
		userRepo.commitTransaction();

		customerList.add(index, customer);
		customerList.remove(index+1);


	}

	public void modifyEmployee(Employee employee, String username, String password, String firstName, String lastName, String address,
							   String phoneNumber, String emailAddress, EmployeeRole role, UserStatus status) {
		// only needs to be checked if you changed the username 
		if (!employee.getUsername().equals(username)) {
			existingUsername(username);
		}
		int index = employeeList.indexOf(employee);

		if (!(password.equals("********") || password.isBlank())) {
			employee.setPassword(password);
		}
		
		employee.setUsername(username);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setAddress(address);
		employee.setPhoneNumber(phoneNumber);
		employee.setEmailAddress(emailAddress);
		employee.setRole(role);
		employee.setStatus(status);
		userRepo.startTransaction();
		userRepo.update(employee);
		userRepo.commitTransaction();

		employeeList.add(index, employee);
		employeeList.remove(index+1);

	}

	public ObservableList<Customer> giveCustomerList() {
		return FXCollections.unmodifiableObservableList(customerList);
	}

	public ObservableList<Employee> giveEmployeeList() {
		return FXCollections.unmodifiableObservableList(employeeList);
	}
	 
	
}
