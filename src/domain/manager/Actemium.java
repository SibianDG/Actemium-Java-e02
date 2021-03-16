package domain.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import domain.ActemiumCompany;
import domain.ActemiumContract;
import domain.ActemiumContractType;
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
import domain.enums.EmployeeRole;
import domain.enums.LoginStatus;
import domain.enums.TicketStatus;
import domain.enums.UserStatus;
import exceptions.AccessException;
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
	private UserDao userDaoJpa;
	private GenericDao<ActemiumCompany> companyDaoJpa;
	private GenericDao<ActemiumTicket> ticketDaoJpa;
	private GenericDao<ActemiumContractType> contractTypeDaoJpa;
	private GenericDao<ActemiumContract> contractDaoJpa;

	// All the ObservableLists
	private ObservableList<Customer> actemiumCustomers;
	private ObservableList<Employee> actemiumEmployees;
	private ObservableList<Ticket> actemiumTickets;
	private ObservableList<Ticket> actemiumTicketsResolved;
	private ObservableList<Ticket> actemiumTicketsOutstanding;
	private ObservableList<Contract> actemiumContracts;
	private ObservableList<ContractType> actemiumContractTypes;
	
	// User (Employee, Customer) that is signed in
	private UserModel signedInUser;
	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;
	
	// Contrutor for domainTest
	// will change in the future
	public Actemium(UserDao userDaoJpa) {
		this.userDaoJpa = userDaoJpa; 
		
		// Fill up ObservableLists
		fillInUserLists();
	}
	
	public Actemium(UserDao userDaoJpa, GenericDao<ActemiumCompany> companyRepo, GenericDao<ActemiumTicket> ticketRepo, GenericDao<ActemiumContractType> contactTypeRepo, GenericDao<ActemiumContract> contractRepo) {
		
		this.userDaoJpa = userDaoJpa;
		this.companyDaoJpa = companyRepo;
		this.ticketDaoJpa = ticketRepo;
		this.contractTypeDaoJpa = contactTypeRepo;
		this.contractDaoJpa = contractRepo;
		
		// Fill up ObservableLists
		fillInUserLists();
		fillTicketList();
		fillContractTypeList();
		fillContractList();
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
		List<TicketStatus> outstanding = Arrays.asList(TicketStatus.CREATED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_ON_USER_INFORMATION, TicketStatus.USER_INFORMATION_RECEIVED, TicketStatus.IN_DEVELOPMENT);
        this.actemiumTicketsOutstanding = FXCollections.observableArrayList((List<Ticket>)(Object)ticketList
                .stream()
                .filter(t -> outstanding.contains(t.getStatusAsEnum()))
                .collect(Collectors.toList()));
		List<TicketStatus> resolved = Arrays.asList(TicketStatus.COMPLETED, TicketStatus.CANCELLED);
        this.actemiumTicketsResolved = FXCollections.observableArrayList((List<Ticket>)(Object)ticketList
                .stream()
                .filter(t -> resolved.contains(t.getStatusAsEnum()))
                .collect(Collectors.toList()));
	}

	public void fillContractTypeList() {
		List<ActemiumContractType> contractTypeList = contractTypeDaoJpa.findAll();
		this.actemiumContractTypes = FXCollections.observableArrayList((List<ContractType>)(Object)contractTypeList);
	}
	
	public void fillContractList() {
		List<ActemiumContract> contractList = contractDaoJpa.findAll();
		this.actemiumContracts = FXCollections.observableArrayList((List<Contract>)(Object)contractList);
	}
	
	//TODO constructor vs setter injection?
	public Actemium() {
		//TODO What type should we give to GenericDaoJpa?
		this(new UserDaoJpa());
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

	public void signIn(String username, String password) {
		UserModel user = findByUsername(username);
		
		if (user instanceof Customer) {
			throw new AccessException("Customers cannot sign in into the desktop application!");
		}
		
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
	
	// necessary for signedInUser right permissions check
	public EmployeeRole giveUserRoleAsEnum() {
		if (signedInUser instanceof Employee) {
			return ((Employee) signedInUser).getRoleAsEnum();
		}
		return null;
	}
	
	// signedInUser right permissions check
	public void checkPermision(EmployeeRole role) {
		if (!giveUserRoleAsEnum().equals(role)) {
			throw new AccessException(
					String.format("You need to be %s %s to do this!"
							, role.equals(EmployeeRole.ADMINISTRATOR) ? "an" : "a"
							, role.toString().toLowerCase()));
		}
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
	
	public ActemiumCompany findCompanyById(Long companyId){
		return companyDaoJpa.get(companyId);
	}	

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
		userDaoJpa.startTransaction();
		userDaoJpa.update(modifiedCustomer);
		userDaoJpa.commitTransaction();
	}

	public void modifyEmployee(ActemiumEmployee modifiedEmployee) {
		userDaoJpa.startTransaction();
		userDaoJpa.update(modifiedEmployee);
		userDaoJpa.commitTransaction();
	}

	/*
	public void deleteUser(UserModel user) {
		userDaoJpa.startTransaction();
		userDaoJpa.delete(user);
		userDaoJpa.commitTransaction();
		if(user instanceof Employee)
			actemiumEmployees.remove(user);
		else
			actemiumCustomers.remove(user);
	}*/
	
	public Customer getLastAddedCustomer() {
		return actemiumCustomers.get(actemiumCustomers.size()-1);
	}
	
	public Employee getLastAddedEmployee() {
		return actemiumEmployees.get(actemiumEmployees.size()-1);
	}
		
	////////-TICKETFACADE-////////
	
	public UserModel findUserById(long userId){
		return userDaoJpa.get(userId);
	}
	
	public void registerTicket(ActemiumTicket ticket, ActemiumCustomer customer) {
		userDaoJpa.startTransaction();
		customer.addTicket(ticket);
		userDaoJpa.commitTransaction();
		actemiumTickets.add(ticket);
		if (TicketStatus.isOutstanding()) {
			actemiumTicketsOutstanding.add(ticket);
		} else {
			actemiumTicketsResolved.add(ticket);
		}
	}
	
	public void modifyTicket(ActemiumTicket ticket) {
		ObservableList<Employee> technicansAsignedToTicket = ticket.giveTechnicians();
		ticket.setTechnicians(new ArrayList<>());

		ticketDaoJpa.startTransaction();
		ticketDaoJpa.update(ticket);
		ticketDaoJpa.commitTransaction();

		technicansAsignedToTicket.forEach(t -> ticket.addTechnician((ActemiumEmployee) t));

		ticketDaoJpa.startTransaction();
		ticketDaoJpa.update(ticket);
		ticketDaoJpa.commitTransaction();
		// change ticket to completed, it will move to resolved tickets
		if (TicketStatus.isOutstanding() &&
				(ticket.getStatusAsEnum().equals(TicketStatus.COMPLETED)
				|| ticket.getStatusAsEnum().equals(TicketStatus.CANCELLED))) {
			actemiumTicketsResolved.add(ticket);
			actemiumTicketsOutstanding.remove(ticket);
		}
		// change ticket to not-completed, it will move to outstanding tickets
		if (!TicketStatus.isOutstanding() &&
				!(ticket.getStatusAsEnum().equals(TicketStatus.COMPLETED)
						|| ticket.getStatusAsEnum().equals(TicketStatus.CANCELLED))) {
			actemiumTicketsOutstanding.add(ticket);
			actemiumTicketsResolved.remove(ticket);
		}
	}

	/*
	//Todo delete ticket from resolved or outstanding
	public void deleteTicket(ActemiumTicket ticket) {
		ticketDaoJpa.startTransaction();
		ticketDaoJpa.delete(ticket);
		ticketDaoJpa.commitTransaction();
		actemiumTickets.remove(ticket);
	}*/
	
	public Ticket getLastAddedTicket() {
		return actemiumTickets.get(actemiumTickets.size()-1);
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

	public ObservableList<Ticket> giveActemiumTicketsResolved() {
		return FXCollections.unmodifiableObservableList(actemiumTicketsResolved);
	}

	public ObservableList<Ticket> giveActemiumTicketsOutstanding() {
		return FXCollections.unmodifiableObservableList(actemiumTicketsOutstanding);
	}

	////////-CONTRACTTYPEFACADE-////////

	public void registerContractType(ActemiumContractType newContractType) {
		contractTypeDaoJpa.startTransaction();
		contractTypeDaoJpa.insert(newContractType);
		contractTypeDaoJpa.commitTransaction();
		actemiumContractTypes.add(newContractType);
	}

	public void modifyContractType(ActemiumContractType contractType) {
		contractTypeDaoJpa.startTransaction();
		contractTypeDaoJpa.update(contractType);
		contractTypeDaoJpa.commitTransaction();
	}

	/*
	public void deleteContractType(ActemiumContractType contractType) {
		contractTypeDaoJpa.startTransaction();
		contractTypeDaoJpa.delete(contractType);
		contractTypeDaoJpa.commitTransaction();
		actemiumContractTypes.remove(contractType);
	}*/

	public ObservableList<ContractType> giveActemiumContractTypes() {
		return FXCollections.unmodifiableObservableList(actemiumContractTypes);
	}

	public ContractType getLastAddedContractType() {
		return actemiumContractTypes.get(actemiumContractTypes.size()-1);
	}

	////////-CONTRACTFACADE-////////
	
	public ActemiumContractType findContractTypeById(String contractTypeId){
		return contractTypeDaoJpa.get(contractTypeId);
	}	
	
	public void registerContract(ActemiumContract contract) {
		contractDaoJpa.startTransaction();
		contractDaoJpa.insert(contract);
		contractDaoJpa.commitTransaction();
		actemiumContracts.add(contract);
	}
	
	public void modifyContract(ActemiumContract contract) {
		contractDaoJpa.startTransaction();
		contractDaoJpa.update(contract);
		contractDaoJpa.commitTransaction();
	}

	public Contract getLastAddedContract() {
		return actemiumContracts.get(actemiumContracts.size()-1);
	}

	public ObservableList<Contract> giveActemiumContracts() {
		return FXCollections.unmodifiableObservableList(actemiumContracts);
	}
}
