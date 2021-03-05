package domain.manager;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Contract;
import domain.ContractType;
import domain.Customer;
import domain.Employee;
import domain.LoginAttempt;
import domain.Ticket;
import domain.UserModel;
import domain.enums.LoginStatus;
import domain.enums.UserStatus;
import exceptions.BlockedUserException;
import exceptions.PasswordException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import languages.LanguageResource;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.UserDao;
import repository.UserDaoJpa;

public class Actemium {
	
	// This class is a manager?
	// The class is statefull, meaning it keeps al the data from the DB in lists

	// All the Dao classes
	// Generic version, no Type given
	private GenericDao genericDaoJpa;
	private GenericDao ticketDaoJpa;
	private UserDao userDaoJpa;

	// All the ObservableLists
	private ObservableList<Customer> actemiumCustomers;
	private ObservableList<Employee> actemiumEmployees;
	private ObservableList<Ticket> actemiumTickets;
	private ObservableList<Contract> actemiumContracts;
	private ObservableList<ContractType> actemiumContractTypes;
	
	// User (Employee, Customer) that is signed in
	private UserModel signedInUser;
	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;
	
	// Contrutor for domainTest
	// will change in the future
	public Actemium(GenericDao genericDaoJpa, UserDao userDaoJpa) {
		//TODO What type should we give to GenericDaoJpa?
		this.genericDaoJpa = genericDaoJpa;
		this.userDaoJpa = userDaoJpa; 
		
		// Fill up ObservableLists
		fillInUserLists();
	}
	
	public Actemium(UserDao userDaoJpa, GenericDao ticketRepo) {
		
		this.userDaoJpa = userDaoJpa; 
		this.ticketDaoJpa = new GenericDaoJpa<>(ActemiumTicket.class);
		
		// Fill up ObservableLists
		fillInUserLists();
		fillTicketList();
	}
	
	// Fill up ObservableLists
	private void fillInUserLists(){
		List<UserModel> userList = userDaoJpa.findAll();
		List<ActemiumCustomer> customerList = userList.stream()
				.filter(c -> c instanceof ActemiumCustomer)
				.map(c -> (ActemiumCustomer) c)
				.collect(Collectors.toList());
		List<ActemiumEmployee> employeeList = userList.stream()
				.filter(e -> e instanceof ActemiumEmployee)
				.map(e -> (ActemiumEmployee) e)
				.collect(Collectors.toList());

		this.actemiumCustomers = FXCollections.observableArrayList((List<Customer>)(Object)customerList);
		this.actemiumEmployees = FXCollections.observableArrayList((List<Employee>)(Object)employeeList);
	}
	
	public void fillTicketList() {
		List<ActemiumTicket> ticketList = ticketDaoJpa.findAll();
		this.actemiumTickets = FXCollections.observableArrayList((List<Ticket>)(Object)ticketList);
	}
	
	//TODO constructor vs setter injection?
	public Actemium() {
		//TODO What type should we give to GenericDaoJpa?
		this(new GenericDaoJpa(UserModel.class), new UserDaoJpa());
//		setUserRepo(new UserDaoJpa());
	}

	public void setUserRepo(UserDao mock) {
		userDaoJpa = mock;
	}
		
	private void setSignedInUser(UserModel signedInEmployee) {
		this.signedInUser = signedInEmployee;
	}

	public UserModel findByUsername(String username){
		return userDaoJpa.findByUsername(username);
	}

//	public void signIn(UserModel user) {
	public void signIn(String username, String password) {
		UserModel user = findByUsername(username);
		
		if (password.isBlank()) {
			throw new PasswordException(LanguageResource.getString("password_blank"));
		}
		
		userDaoJpa.startTransaction();
		
		// user account already blocked, only the failed login attempt needs to registered
		if (user.getStatusAsEnum().equals(UserStatus.BLOCKED)) {
			user.increaseFailedLoginAttempts();
			LoginAttempt loginAttempt = new LoginAttempt(user, LoginStatus.FAILED);	
			user.addLoginAttempt(loginAttempt);

			userDaoJpa.commitTransaction();
			
			throw new BlockedUserException(String.format(
					"User account has been blocked because more than%n%d failed login attempts have been registered."
					+ "%nPlease contact your system administrator.",
					USER_LOGIN_MAX_ATTEMPTS));
		}
				
		// check password
		if (!user.getPassword().equals(password)) {
			user.increaseFailedLoginAttempts();
			LoginAttempt loginAttempt = new LoginAttempt(user, LoginStatus.FAILED);	
			user.addLoginAttempt(loginAttempt);
			// block user after 5 failed login attempts
			if (user.getFailedLoginAttempts() >= USER_LOGIN_MAX_ATTEMPTS) {
				user.blockUser();

				userDaoJpa.commitTransaction();				
				
				throw new BlockedUserException(
						String.format("%s%nUser has reached more than %d failed login attempts,%n account has been blocked.",
								LanguageResource.getString("wrongUsernamePasswordCombination"),
								USER_LOGIN_MAX_ATTEMPTS));
			}			
			userDaoJpa.commitTransaction();	
			
			throw new PasswordException(String.format("%s%nOnly %d attempts remaining", 
					LanguageResource.getString("wrongUsernamePasswordCombination"), 
					USER_LOGIN_MAX_ATTEMPTS - user.getFailedLoginAttempts()));
		}

		// correct password
		user.resetLoginAttempts();

		LoginAttempt loginAttempt = new LoginAttempt(user, LoginStatus.SUCCESS);	
		user.addLoginAttempt(loginAttempt);

		userDaoJpa.commitTransaction();
		
		setSignedInUser(user);
		
		System.out.println("Just signed in: " + signedInUser.getUsername());
	}

	// Dashboard signedInUser necessities
	public String giveUserRole() {
		if (signedInUser instanceof Employee) {
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

	// Check needed for register and modify user
	public void existingUsername(String username) {
		try {
			if (userDaoJpa.findByUsername(username) != null) {
				throw new IllegalArgumentException("Username is already taken.");
			}
		} catch (EntityNotFoundException e) {
			// ignore
		}
	}	

	////////-USERFACADE-////////

	public void registerCustomer(ActemiumCustomer newCustomer) {
		userDaoJpa.startTransaction();
		userDaoJpa.insert(newCustomer);
		userDaoJpa.commitTransaction();
		actemiumCustomers.add(newCustomer);
	}

	public void registerEmployee(ActemiumEmployee newEmployee) {
		userDaoJpa.startTransaction();
		userDaoJpa.insert(newEmployee);
		userDaoJpa.commitTransaction();
		actemiumEmployees.add(newEmployee);
	}

	public void modifyCustomer(ActemiumCustomer modifiedCustomer) {
		int index = actemiumCustomers.indexOf(modifiedCustomer);

		userDaoJpa.startTransaction();
		userDaoJpa.update(modifiedCustomer);
		userDaoJpa.commitTransaction();

		actemiumCustomers.add(index, modifiedCustomer);
		actemiumCustomers.remove(index + 1);
	}

	public void modifyEmployee(ActemiumEmployee modifiedEmployee) {
		int index = actemiumEmployees.indexOf(modifiedEmployee);

		userDaoJpa.startTransaction();
		userDaoJpa.update(modifiedEmployee);
		userDaoJpa.commitTransaction();

		actemiumEmployees.add(index, modifiedEmployee);
		actemiumEmployees.remove(index + 1);
	}
	
	////////-TICKETFACADE-////////
	
	public void registerTicket(ActemiumTicket ticket, ActemiumCustomer customer) {
		userDaoJpa.startTransaction();
		customer.addTicket(ticket);
		userDaoJpa.commitTransaction();
		actemiumTickets.add(ticket);
	}
	
	public void modifyTicket(ActemiumTicket ticket) {
		int index = actemiumTickets.indexOf(ticket);
		
		ticketDaoJpa.startTransaction();
		ticketDaoJpa.update(ticket);
		ticketDaoJpa.commitTransaction();

		actemiumTickets.add(index, ticket);
		actemiumTickets.remove(index + 1);
	}

	public ObservableList<Customer> giveActemiumCustomers() {
		return FXCollections.unmodifiableObservableList(actemiumCustomers);
	}

	public ObservableList<Employee> giveActemiumEmployees() {
		return FXCollections.unmodifiableObservableList(actemiumEmployees);
	}

	public ObservableList<Ticket> giveActemiumTickets() {
		return FXCollections.unmodifiableObservableList(actemiumTickets);
	}

	public ObservableList<Contract> giveActemiumContracts() {
		return FXCollections.unmodifiableObservableList(actemiumContracts);
	}

	public ObservableList<ContractType> giveActemiumContractTypes() {
		return FXCollections.unmodifiableObservableList(actemiumContractTypes);
	}

}
