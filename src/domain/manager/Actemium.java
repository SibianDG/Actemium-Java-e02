package domain.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import domain.ActemiumCompany;
import domain.ActemiumContract;
import domain.ActemiumContractType;
import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumKbItem;
import domain.ActemiumTicket;
import domain.Contract;
import domain.ContractType;
import domain.Customer;
import domain.Employee;
import domain.KbItem;
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
	private GenericDao<ActemiumKbItem> kbItemDaoJpa;

	// All the ObservableLists
	private ObservableList<Customer> actemiumCustomers;
	private ObservableList<Employee> actemiumEmployees;
	private ObservableList<Ticket> actemiumTickets;
	private ObservableList<Ticket> actemiumTicketsResolved;
	private ObservableList<Ticket> actemiumTicketsOutstanding;
	private ObservableList<Contract> actemiumContracts;
	private ObservableList<ContractType> actemiumContractTypes;
	private ObservableList<KbItem> actemiumKbItems;
	
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
	
	public Actemium(UserDao userDaoJpa, GenericDao<ActemiumCompany> companyRepo, GenericDao<ActemiumTicket> ticketRepo, 
			GenericDao<ActemiumContractType> contactTypeRepo, GenericDao<ActemiumContract> contractRepo,
			GenericDao<ActemiumKbItem> kbItemDaoJpa) {
		
		this.userDaoJpa = userDaoJpa;
		this.companyDaoJpa = companyRepo;
		this.ticketDaoJpa = ticketRepo;
		this.contractTypeDaoJpa = contactTypeRepo;
		this.contractDaoJpa = contractRepo;
		this.kbItemDaoJpa = kbItemDaoJpa;
		
		// Fill up ObservableLists
		fillInUserLists();
		fillTicketList();
		fillContractTypeList();
		fillContractList();
		fillKbItemList();
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
                .filter(t -> outstanding.contains(t.getStatus()))
                .collect(Collectors.toList()));
		List<TicketStatus> resolved = Arrays.asList(TicketStatus.COMPLETED, TicketStatus.CANCELLED);
        this.actemiumTicketsResolved = FXCollections.observableArrayList((List<Ticket>)(Object)ticketList
                .stream()
                .filter(t -> resolved.contains(t.getStatus()))
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
	
	public void fillKbItemList() {
		List<ActemiumKbItem> kbItemList = kbItemDaoJpa.findAll();
		this.actemiumKbItems = FXCollections.observableArrayList((List<KbItem>)(Object)kbItemList);
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
		
	public UserModel getSignedInUser() {
		return signedInUser;
	}

	private void setSignedInUser(UserModel signedInEmployee) {
		this.signedInUser = signedInEmployee;
	}

	public UserModel findByUsername(String username){
		return userDaoJpa.findByUsername(username);
	}

	public UserModel findByEmail(String email) {
		return userDaoJpa.findByEmail(email);
	}

	public UserModel findbyID(String id) {
		return userDaoJpa.get(Long.parseLong(id));
	}

	public String getNameByID(long id) {
		return String.format("%s %s", userDaoJpa.get(id).getFirstName(), userDaoJpa.get(id).getLastName());
	}

	public void signIn(String usernameIDorEmail, String password) {
		UserModel user;
		 if(usernameIDorEmail.contains("@"))
			user = findByEmail(usernameIDorEmail);
		 else if(Pattern.matches("[0-9]+", usernameIDorEmail))
			user = findbyID(usernameIDorEmail);
		 else
		 	user = findByUsername(usernameIDorEmail);


		if (user instanceof Customer) {
			throw new AccessException(LanguageResource.getString("no_customer"));
		}
		
		if (password.isBlank()) {
			throw new PasswordException(LanguageResource.getString("password_blank"));
		}
		
		userDaoJpa.startTransaction();
		
		// user account already blocked, only the failed login attempt needs to registered
		if (user.getStatus().equals(UserStatus.BLOCKED)) {
			user.increaseFailedLoginAttempts();
			LoginAttempt loginAttempt = new LoginAttempt(user, LoginStatus.FAILED);	
			user.addLoginAttempt(loginAttempt);

			userDaoJpa.commitTransaction();
			
			throw new BlockedUserException(String.format(
					"%S%n%d %s."
					+ "%n%s.",
					LanguageResource.getString("blocked_user_message1"),
					USER_LOGIN_MAX_ATTEMPTS,
					LanguageResource.getString("blocked_user_message2"),
					LanguageResource.getString("blocked_user_message3")

			));
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
						String.format("%s%n%s %d %s,%n %s.",
								LanguageResource.getString("wrongUsernamePasswordCombination"),
								LanguageResource.getString("blocked_user_message4"),
								USER_LOGIN_MAX_ATTEMPTS,
								LanguageResource.getString("blocked_user_message5"),
								LanguageResource.getString("blocked_user_message6")
				));
			}
			userDaoJpa.commitTransaction();	
			
			throw new PasswordException(String.format("%s%n%s %d %s",
					LanguageResource.getString("wrongUsernamePasswordCombination"),
					LanguageResource.getString("password_exception_message1"),
					USER_LOGIN_MAX_ATTEMPTS - user.getFailedLoginAttempts(),
					LanguageResource.getString("password_exception_message2")
			));
		}

		// correct password
		user.resetLoginAttempts();

		LoginAttempt loginAttempt = new LoginAttempt(user, LoginStatus.SUCCESS);	
		user.addLoginAttempt(loginAttempt);

		userDaoJpa.commitTransaction();
		
		setSignedInUser(user);

		//TODO remove:
		System.out.println("Just signed in: " + signedInUser.getUsername());
	}

	// Dashboard signedInUser necessities
	public String giveUserRole() {
		if (signedInUser instanceof Employee) {
			return ((Employee) signedInUser).getRoleAsString().toString();
		}
		return "Customer";
	}
	
	// necessary for signedInUser right permissions check
	public EmployeeRole giveUserRoleAsEnum() {
		if (signedInUser instanceof Employee) {
			return ((Employee) signedInUser).getRole();
		}
		return null;
	}

	//TODO Actemium knows role

	// signedInUser right permissions check
	public void checkPermision(EmployeeRole role) {
		if (!giveUserRoleAsEnum().equals(role)) {
			throw new AccessException(
					String.format("%s %s %s %s!"
							, LanguageResource.getString("you_need_to_be")
							, role.equals(EmployeeRole.ADMINISTRATOR) ? LanguageResource.getString("an") : LanguageResource.getString("a")
							, role.toString().toLowerCase()
							, LanguageResource.getString("to_do_this")
					));
		}
	}

	public void checkPermisionForModifyEmployee(EmployeeRole role, String username) {
		if (!signedInUser.getUsername().equals(username)){

			checkPermision(role);
		}
	}
	
	public String giveUserStatus() {
		return signedInUser.getStatusAsString();
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
	
	public String giveUserEmployeeId() {
		return String.valueOf(((Employee)signedInUser).getEmployeeNr());
	}
	
	public String giveUserEmailAddress() {
		return ((Employee) signedInUser).getEmailAddress();
	}
	
	public String giveUserPhoneNumber() {
		return ((Employee) signedInUser).getPhoneNumber();
	}
	
	public String giveUserAddress() {
		return ((Employee) signedInUser).getAddress();
	}
	
	public String giveUserSeniority() {
		return String.valueOf(((Employee) signedInUser).giveSeniority());
	}
	
	public String giveUserPassword() {
		return signedInUser.getPassword();
	}

	// Check needed for register and modify user
	public void existingUsername(String username) {
		try {
			if (userDaoJpa.findByUsername(username) != null) {
				throw new IllegalArgumentException(LanguageResource.getString("username_already_taken"));
			}
		} catch (EntityNotFoundException e) {
			// ignore
		}
	}	

	////////-USERFACADE-////////
	
	public ActemiumCompany findCompanyById(long companyId){
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
				(ticket.getStatus().equals(TicketStatus.COMPLETED)
				|| ticket.getStatus().equals(TicketStatus.CANCELLED))) {
			actemiumTicketsResolved.add(ticket);
			actemiumTicketsOutstanding.remove(ticket);
		}
		// change ticket to not-completed, it will move to outstanding tickets
		if (!TicketStatus.isOutstanding() &&
				!(ticket.getStatus().equals(TicketStatus.COMPLETED)
						|| ticket.getStatus().equals(TicketStatus.CANCELLED))) {
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
		if (signedInUser instanceof Employee) {
			if (((Employee) signedInUser).getRole() == EmployeeRole.TECHNICIAN) {
				return giveActemiumTicketsOutstandingAssignedToTechnician();
			}
		} // else
		return FXCollections.unmodifiableObservableList(actemiumTicketsOutstanding);
	}
	
	public ObservableList<Ticket> giveActemiumTicketsOutstandingAssignedToTechnician() {
		return FXCollections.observableArrayList(actemiumTicketsOutstanding.stream()
				.filter(t -> t.giveTechnicians().stream().collect(Collectors.toList()).contains(getSignedInUser()))
				.collect(Collectors.toList()));
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
	
	public ActemiumContractType findContractTypeById(long contractTypeId){
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
	
	////////-KNOWLEDGEBASEFACADE-////////
	
	public void registerKbItem(ActemiumKbItem kbItem) {
		kbItemDaoJpa.startTransaction();
		kbItemDaoJpa.insert(kbItem);
		kbItemDaoJpa.commitTransaction();
		actemiumKbItems.add(kbItem);
	}
	
	public void modifyKbItem(ActemiumKbItem kbItem) {
		kbItemDaoJpa.startTransaction();
		kbItemDaoJpa.update(kbItem);
		kbItemDaoJpa.commitTransaction();
	}

	public KbItem getLastAddedKbItem() {
		return actemiumKbItems.get(actemiumKbItems.size()-1);
	}

	public ObservableList<KbItem> giveActemiumKbItems() {
		return FXCollections.unmodifiableObservableList(actemiumKbItems);
	}
}
