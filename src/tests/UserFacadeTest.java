package tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.InformationRequiredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.UserModel;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import domain.facades.UserFacade;
import domain.manager.Actemium;
import repository.GenericDao;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    final String ADMINUSERNAME = "janJannsens123", PASSWORD = "PassWd123&", WRONGPASSWORD = "foutPas12&",
    			 TECHUSERNAME = "jooKlein123", CUSTOMERUSERNAME = "customer123",
    			 WRONGUSERNAME = "usernameDoesNotExist", //EntityNotFoundException()
    			 USERNAMEAVAILABLE = "usernameAvailable";
    private UserModel admin;
    private UserModel tech;
    private ActemiumCompany google;

    private UserModel cust;

    private void initializeAttributes(){
		try {
			admin = new ActemiumEmployee.EmployeeBuilder()
					.username("janJannsens123")
					.password("PassWd123&")
					.firstName("Jan")
					.lastName("Jannsens")
					.address("Adress")
					.phoneNumber("0470099874")
					.emailAddress("student@student.hogent.be")
					.role(EmployeeRole.ADMINISTRATOR)
					.build();
			tech = new ActemiumEmployee.EmployeeBuilder()
					.username("jooKlein123")
					.password("PassWd123&")
					.firstName("Joost")
					.lastName("Klein")
					.address("Adress")
					.phoneNumber("0470099874")
					.emailAddress("student@student.hogent.be")
					.role(EmployeeRole.TECHNICIAN)
					.build();
			google = new ActemiumCompany.CompanyBuilder()
					.name("Facebook")
					.country("United States")
					.city("Menlo Park, CA 94025")
					.address("1 Hacker Way")
					.phoneNumber("+1-650-308-7300")
					.build();
			cust = new ActemiumCustomer.CustomerBuilder()
					.username("customer123")
					.password("PassWd123&")
					.firstName("John")
					.lastName("Smith")
					.company(google)
					.build();
		} catch (InformationRequiredException e) {
			throw new IllegalArgumentException("Problem with initialize variables before test.");
		}

	}

    private UserFacade userFacade;
    
    @Mock
    private UserDao userRepoDummy;
    @InjectMocks
    private Actemium actemium;

	private void trainDummy() {
		initializeAttributes();
    	Mockito.lenient().when(userRepoDummy.findByUsername(ADMINUSERNAME)).thenReturn(admin);
    	Mockito.lenient().when(userRepoDummy.findByUsername(TECHUSERNAME)).thenReturn(tech);
    	Mockito.lenient().when(userRepoDummy.findByUsername(CUSTOMERUSERNAME)).thenReturn(cust);
    	Mockito.lenient().when(userRepoDummy.findByUsername(USERNAMEAVAILABLE)).thenReturn(null);
    }
    
    private void initUserFacadeAdmin() {
    // userFacade needs to be created
    this.userFacade = new UserFacade(actemium);
    // Administrator needs to be signed in
    userFacade.signIn(ADMINUSERNAME, PASSWORD);
    }
    
	@Test
	public void createCustomer_UsernameAlreadyExists_ThrowsIllegalArgumentException() {
		trainDummy();
		initUserFacadeAdmin();
		assertThrows(IllegalArgumentException.class,
				() -> userFacade.registerCustomer(ADMINUSERNAME, PASSWORD, "John", "Smith", "Google", "United States",
						"Mountain View, CA 94043", "1600 Amphitheatre Parkway", "+1-650-253-0000"));
		Mockito.verify(userRepoDummy, Mockito.times(2)).findByUsername(ADMINUSERNAME);
	}

    @Test
    public void modifyCustomer_UsernameAlreadyExists_ThrowsIllegalArgumentException() {
    	trainDummy();
    	initUserFacadeAdmin();
		assertThrows(IllegalArgumentException.class,
				() -> userFacade.modifyCustomer((ActemiumCustomer) cust, ADMINUSERNAME, PASSWORD, "John", "Smith",
						UserStatus.ACTIVE, "Google", "United States", "Mountain View, CA 94043",
						"1600 Amphitheatre Parkway", "+1-650-253-0000"));
		Mockito.verify(userRepoDummy, Mockito.times(2)).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void createEmployee_UsernameAlreadyExists_ThrowsIllegalArgumentException() {
    	trainDummy();
    	initUserFacadeAdmin();
		assertThrows(IllegalArgumentException.class, () -> userFacade.registerEmployee(ADMINUSERNAME, PASSWORD, "John",
				"Smith", "Address 78", "0458634795", "john.smith@student.hogent.be", EmployeeRole.ADMINISTRATOR));
		Mockito.verify(userRepoDummy, Mockito.times(2)).findByUsername(ADMINUSERNAME);
    }

    @Test
    public void modifyEmployee_UsernameAlreadyExists_ThrowsIllegalArgumentException() {
    	trainDummy();
    	initUserFacadeAdmin();
		assertThrows(IllegalArgumentException.class,
				() -> userFacade.modifyEmployee((ActemiumEmployee) tech, ADMINUSERNAME, PASSWORD, "John", "Smith",
						"Address 78", "0458634795", "john.smith@student.hogent.be", EmployeeRole.ADMINISTRATOR,
						UserStatus.ACTIVE, null));
		Mockito.verify(userRepoDummy, Mockito.times(2)).findByUsername(ADMINUSERNAME);
     }
    
    // valid creations
    @Test
    public void createCustomer_UsernameAvailable_DoesNotThrowException() {
    	trainDummy();
    	initUserFacadeAdmin();
		assertDoesNotThrow(() -> userFacade.registerCustomer(USERNAMEAVAILABLE, PASSWORD, "John", "Smith", "Google",
				"United States", "Mountain View, CA 94043", "1600 Amphitheatre Parkway", "+1-650-253-0000"));
		Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    	Mockito.verify(userRepoDummy).findByUsername(USERNAMEAVAILABLE);
    }
    
    @Test
    public void modifyCustomer_UsernameAvailable_DoesNotThrowException() {
    	trainDummy();
    	initUserFacadeAdmin();
		assertDoesNotThrow(() -> userFacade.modifyCustomer((ActemiumCustomer) cust, USERNAMEAVAILABLE, PASSWORD, "John",
				"Smith", UserStatus.ACTIVE, "Google", "United States", "Mountain View, CA 94043",
				"1600 Amphitheatre Parkway", "+1-650-253-0000"));
		Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    	Mockito.verify(userRepoDummy).findByUsername(USERNAMEAVAILABLE);
    }
    
    @Test
    public void createEmployee_UsernameAvailable_DoesNotThrowException() {
    	trainDummy();
    	initUserFacadeAdmin();
    	assertDoesNotThrow(() -> userFacade.registerEmployee(USERNAMEAVAILABLE, PASSWORD, "John", "Smith",
    			"Address 78", "0458634795", "john.smith@student.hogent.be", EmployeeRole.ADMINISTRATOR));
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    	Mockito.verify(userRepoDummy).findByUsername(USERNAMEAVAILABLE);
    }
    
    @Test
    public void modifyEmployee_UsernameAvailable_DoesNotThrowException() {
    	trainDummy();
    	initUserFacadeAdmin();
		assertDoesNotThrow(() -> userFacade.modifyEmployee((ActemiumEmployee) tech, USERNAMEAVAILABLE, PASSWORD, "John",
				"Smith", "Address 78", "0032458634795", "john.smith@student.hogent.be", EmployeeRole.ADMINISTRATOR,
				UserStatus.ACTIVE, null));
		Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    	Mockito.verify(userRepoDummy).findByUsername(USERNAMEAVAILABLE);
    }    
    
}
