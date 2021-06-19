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
import domain.enums.KbItemType;
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

/**
 * The type Actemium.
 */
public class Actemium {
	
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

	/**
	 * Instantiates a new Actemium.
	 *
	 * @param userDaoJpa the user dao jpa
	 */
	
// Contrutor for domainTest
	// will change in the future
	public Actemium(UserDao userDaoJpa) {
		this.userDaoJpa = userDaoJpa; 
		
		// Fill up ObservableLists
		fillInUserLists();
	}

	/**
	 * Instantiates a new Actemium.
	 *
	 * @param userDaoJpa      the user dao jpa
	 * @param companyRepo     the company repo
	 * @param ticketRepo      the ticket repo
	 * @param contactTypeRepo the contact type repo
	 * @param contractRepo    the contract repo
	 * @param kbItemDaoJpa    the kb item dao jpa
	 */
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

	/**
	 * Fill ticket list.
	 */
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

	/**
	 * Fill contract type list.
	 */
	public void fillContractTypeList() {
		List<ActemiumContractType> contractTypeList = contractTypeDaoJpa.findAll();
		this.actemiumContractTypes = FXCollections.observableArrayList((List<ContractType>)(Object)contractTypeList);
	}

	/**
	 * Fill contract list.
	 */
	public void fillContractList() {
		List<ActemiumContract> contractList = contractDaoJpa.findAll();
		this.actemiumContracts = FXCollections.observableArrayList((List<Contract>)(Object)contractList);
	}

	/**
	 * Fill kb item list.
	 */
	public void fillKbItemList() {
		List<ActemiumKbItem> kbItemList = kbItemDaoJpa.findAll();
		this.actemiumKbItems = FXCollections.observableArrayList((List<KbItem>)(Object)kbItemList);
	}

	/**
	 * Instantiates a new Actemium.
	 */
	public Actemium() {
		this(new UserDaoJpa());
	}

	/**
	 * Sets user repo.
	 *
	 * @param mock the mock
	 */
	public void setUserRepo(UserDao mock) {
		userDaoJpa = mock;
	}

	/**
	 * Gets signed in user.
	 *
	 * @return the signed in user
	 */
	public UserModel getSignedInUser() {
		return signedInUser;
	}

	private void setSignedInUser(UserModel signedInEmployee) {
		this.signedInUser = signedInEmployee;
	}

	/**
	 * Find by username user model.
	 *
	 * @param username the username
	 * @return the user model
	 */
	public UserModel findByUsername(String username){
		return userDaoJpa.findByUsername(username);
	}

	/**
	 * Find by email user model.
	 *
	 * @param email the email
	 * @return the user model
	 */
	public UserModel findByEmail(String email) {
		return userDaoJpa.findByEmail(email);
	}

	/**
	 * Findby id user model.
	 *
	 * @param id the id
	 * @return the user model
	 */
	public UserModel findbyID(String id) {
		return userDaoJpa.get(Long.parseLong(id));
	}

	/**
	 * Gets name by id.
	 *
	 * @param id the id
	 * @return the name by id
	 */
	public String getNameByID(int id) {
		return String.format("%s %s", userDaoJpa.get(id).getFirstName(), userDaoJpa.get(id).getLastName());
	}

	/**
	 * Signs in and sets signed in user in this class.
	 *
	 * @param usernameIDorEmail the username ID or email
	 * @param password          the password
	 */
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

	}

	/**
	 * Give user role string.
	 *
	 * @return the string
	 */
// Dashboard signedInUser necessities
	public String giveUserRole() {
		if (signedInUser instanceof Employee) {
			return ((Employee) signedInUser).getRoleAsString().toString();
		}
		return "Customer";
	}

	/**
	 * Give employee role as enum employee role.
	 *
	 * @return the employee role
	 */
// necessary for signedInUser right permissions check
	public EmployeeRole giveEmployeeRoleAsEnum() {
		if (signedInUser instanceof Employee) {
			return ((Employee) signedInUser).getRole();
		}
		return null;
	}

	/**
	 * Check permissions.
	 *
	 * @param role the role
	 */
// signedInUser right permissions check
	public void checkPermission(EmployeeRole role) {
		if (!giveEmployeeRoleAsEnum().equals(role)) {
			throw new AccessException(
					String.format("%s %s %s %s!"
							, LanguageResource.getString("you_need_to_be")
							, role.equals(EmployeeRole.ADMINISTRATOR) ? LanguageResource.getString("an") : LanguageResource.getString("a")
							, role.toString().toLowerCase()
							, LanguageResource.getString("to_do_this")
					));
		}
	}

	

	/**
	 * Check permision for modify employee.
	 *
	 * @param role     the role
	 * @param username the username
	 */
	public void checkPermisionForModifyEmployee(EmployeeRole role, String username) {
		if (!signedInUser.getUsername().equals(username)){
			checkPermission(role);
		}
	}

	/**
	 * Give user status string.
	 *
	 * @return the string
	 */
	public String giveUserStatus() {
		return signedInUser.getStatusAsString();
	}

	/**
	 * Give username string.
	 *
	 * @return the string
	 */
	public String giveUsername() {
		return signedInUser.getUsername();
	}

	/**
	 * Give user first name string.
	 *
	 * @return the string
	 */
	public String giveUserFirstName() {
		return signedInUser.getFirstName();
	}

	/**
	 * Give user last name string.
	 *
	 * @return the string
	 */
	public String giveUserLastName() {
		return signedInUser.getLastName();
	}

	/**
	 * Give user employee id string.
	 *
	 * @return the string
	 */
	public String giveUserEmployeeId() {
		return String.valueOf(((Employee)signedInUser).getEmployeeNr());
	}

	/**
	 * Give user email address string.
	 *
	 * @return the string
	 */
	public String giveUserEmailAddress() {
		return ((Employee) signedInUser).getEmailAddress();
	}

	/**
	 * Give user phone number string.
	 *
	 * @return the string
	 */
	public String giveUserPhoneNumber() {
		return ((Employee) signedInUser).getPhoneNumber();
	}

	/**
	 * Give user address string.
	 *
	 * @return the string
	 */
	public String giveUserAddress() {
		return ((Employee) signedInUser).getAddress();
	}

	/**
	 * Give user seniority string.
	 *
	 * @return the string
	 */
	public String giveUserSeniority() {
		return String.valueOf(((Employee) signedInUser).giveSeniority());
	}

	/**
	 * Give user password string.
	 *
	 * @return the string
	 */
	public String giveUserPassword() {
		return signedInUser.getPassword();
	}

	/**
	 * Existing username.
	 *
	 * @param username the username
	 */
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

	/**
	 * Find company by id actemium company.
	 *
	 * @param companyId the company id
	 * @return the actemium company
	 */
	public ActemiumCompany findCompanyById(int companyId){
		return companyDaoJpa.get(companyId);
	}

	/**
	 * Register customer.
	 *
	 * @param newCustomer the new customer
	 */
	public void registerCustomer(ActemiumCustomer newCustomer) {
		userDaoJpa.startTransaction();
		userDaoJpa.insert(newCustomer);
		userDaoJpa.commitTransaction();
		actemiumCustomers.add(newCustomer);
	}

	/**
	 * Register employee.
	 *
	 * @param newEmployee the new employee
	 */
	public void registerEmployee(ActemiumEmployee newEmployee) {
		userDaoJpa.startTransaction();
		userDaoJpa.insert(newEmployee);
		userDaoJpa.commitTransaction();
		actemiumEmployees.add(newEmployee);
	}

	/**
	 * Modify customer.
	 *
	 * @param modifiedCustomer the modified customer
	 */
	public void modifyCustomer(ActemiumCustomer modifiedCustomer) {
		userDaoJpa.startTransaction();
		userDaoJpa.update(modifiedCustomer);
		userDaoJpa.commitTransaction();
	}

	/**
	 * Modify employee.
	 *
	 * @param modifiedEmployee the modified employee
	 */
	public void modifyEmployee(ActemiumEmployee modifiedEmployee) {
		userDaoJpa.startTransaction();
		userDaoJpa.update(modifiedEmployee);
		userDaoJpa.commitTransaction();
	}


	/**
	 * Gets last added customer.
	 *
	 * @return the last added customer
	 */
	public Customer getLastAddedCustomer() {
		return actemiumCustomers.get(actemiumCustomers.size()-1);
	}

	/**
	 * Gets last added employee.
	 *
	 * @return the last added employee
	 */
	public Employee getLastAddedEmployee() {
		return actemiumEmployees.get(actemiumEmployees.size()-1);
	}
		
	////////-TICKETFACADE-////////

	/**
	 * Find user by id user model.
	 *
	 * @param userId the user id
	 * @return the user model
	 */
	public UserModel findUserById(int userId){
		return userDaoJpa.get(userId);
	}

	/**
	 * Register ticket.
	 *
	 * @param ticket   the ticket
	 * @param customer the customer
	 */
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

	/**
	 * Modify ticket.
	 *
	 * @param ticket the ticket
	 */
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

	/**
	 * Gets last added ticket.
	 *
	 * @return the last added ticket
	 */
	public Ticket getLastAddedTicket() {
		return actemiumTickets.get(actemiumTickets.size()-1);
	}

	/**
	 * Give actemium customers observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Customer> giveActemiumCustomers() {
		return FXCollections.unmodifiableObservableList(actemiumCustomers);
	}

	/**
	 * Give actemium employees observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Employee> giveActemiumEmployees() {
		return FXCollections.unmodifiableObservableList(actemiumEmployees);
	}

	/**
	 * Give actemium tickets observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTickets() {
		return FXCollections.unmodifiableObservableList(actemiumTickets);
	}

	/**
	 * Give actemium tickets resolved observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTicketsResolved() {
		return FXCollections.unmodifiableObservableList(actemiumTicketsResolved);
	}

	/**
	 * Give actemium tickets outstanding observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTicketsOutstanding() {
		if (signedInUser instanceof Employee) {
			if (((Employee) signedInUser).getRole() == EmployeeRole.TECHNICIAN) {
				return FXCollections.unmodifiableObservableList(giveActemiumTicketsOutstandingAssignedToTechnician());
			}
		}
		return FXCollections.unmodifiableObservableList(actemiumTicketsOutstanding);
	}
	
	private ObservableList<Ticket> giveActemiumTicketsOutstandingAssignedToTechnician() {
		return FXCollections.observableArrayList(actemiumTicketsOutstanding.stream()
				.filter(t -> new ArrayList<>(t.giveTechnicians()).contains((Employee) getSignedInUser()))
				.collect(Collectors.toList()));
	}

	////////-CONTRACTTYPEFACADE-////////

	/**
	 * Register contract type.
	 *
	 * @param newContractType the new contract type
	 */
	public void registerContractType(ActemiumContractType newContractType) {
		contractTypeDaoJpa.startTransaction();
		contractTypeDaoJpa.insert(newContractType);
		contractTypeDaoJpa.commitTransaction();
		actemiumContractTypes.add(newContractType);
	}

	/**
	 * Modify contract type.
	 *
	 * @param contractType the contract type
	 */
	public void modifyContractType(ActemiumContractType contractType) {
		contractTypeDaoJpa.startTransaction();
		contractTypeDaoJpa.update(contractType);
		contractTypeDaoJpa.commitTransaction();
	}


	/**
	 * Give actemium contract types observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<ContractType> giveActemiumContractTypes() {
		return FXCollections.unmodifiableObservableList(actemiumContractTypes);
	}

	/**
	 * Gets last added contract type.
	 *
	 * @return the last added contract type
	 */
	public ContractType getLastAddedContractType() {
		return actemiumContractTypes.get(actemiumContractTypes.size()-1);
	}

	////////-CONTRACTFACADE-////////

	/**
	 * Find contract type by id actemium contract type.
	 *
	 * @param contractTypeId the contract type id
	 * @return the actemium contract type
	 */
	public ActemiumContractType findContractTypeById(int contractTypeId){
		return contractTypeDaoJpa.get(contractTypeId);
	}

	/**
	 * Register contract.
	 *
	 * @param contract the contract
	 */
	public void registerContract(ActemiumContract contract) {
		contractDaoJpa.startTransaction();
		contractDaoJpa.insert(contract);
		contractDaoJpa.commitTransaction();
		actemiumContracts.add(contract);
	}

	/**
	 * Modify contract.
	 *
	 * @param contract the contract
	 */
	public void modifyContract(ActemiumContract contract) {
		contractDaoJpa.startTransaction();
		contractDaoJpa.update(contract);
		contractDaoJpa.commitTransaction();
	}

	/**
	 * Gets last added contract.
	 *
	 * @return the last added contract
	 */
	public Contract getLastAddedContract() {
		return actemiumContracts.get(actemiumContracts.size()-1);
	}

	/**
	 * Give actemium contracts observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Contract> giveActemiumContracts() {
		return FXCollections.unmodifiableObservableList(actemiumContracts);
	}
	
	////////-KNOWLEDGEBASEFACADE-////////

	/**
	 * Register kb item.
	 *
	 * @param kbItem the kb item
	 */
	public void registerKbItem(ActemiumKbItem kbItem) {
		kbItemDaoJpa.startTransaction();
		kbItemDaoJpa.insert(kbItem);
		kbItemDaoJpa.commitTransaction();
		actemiumKbItems.add(kbItem);
	}

	/**
	 * Modify kb item.
	 *
	 * @param kbItem the kb item
	 */
	public void modifyKbItem(ActemiumKbItem kbItem) {
		kbItemDaoJpa.startTransaction();
		kbItemDaoJpa.update(kbItem);
		kbItemDaoJpa.commitTransaction();
	}

	/**
	 * Gets last added kb item.
	 *
	 * @return the last added kb item
	 */
	public KbItem getLastAddedKbItem() {
		return actemiumKbItems.get(actemiumKbItems.size()-1);
	}

	/**
	 * Give actemium kb items observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<KbItem> giveActemiumKbItems() {
		if (signedInUser instanceof Employee) {
			if (((Employee) signedInUser).getRole() == EmployeeRole.SUPPORT_MANAGER) {
				return FXCollections.unmodifiableObservableList(actemiumKbItems);
			}
		}
		return FXCollections.unmodifiableObservableList(giveActemiumKbItemsNonArchived());
	}
	
	private ObservableList<KbItem> giveActemiumKbItemsNonArchived() {
		return FXCollections.observableArrayList(actemiumKbItems.stream()
				.filter(k -> !k.getType().equals(KbItemType.ARCHIVED))
				.collect(Collectors.toList()));
	}

	/**
	 * Check admin dont edit own role.
	 *
	 * @param employee the employee
	 * @param role     the role
	 */
	public void checkAdminDontEditOwnRole(ActemiumEmployee employee, EmployeeRole role) {
		if (employee.getUserId() == signedInUser.getUserId()){
			if (!giveEmployeeRoleAsEnum().equals(role)){
				throw new IllegalArgumentException(LanguageResource.getString("admin_cant_edit_own_role"));
			}
		}
	}

	public void refreshUserData() {
		fillInUserLists();		
	}

	public void refreshTicketData() {
		fillTicketList();		
	}

	public void refreshContractTypeData() {
		fillContractTypeList();		
	}

	public void refreshContractData() {
		fillContractList();		
	}

	public void refreshKbItemData() {
		fillKbItemList();		
	}
	
}
