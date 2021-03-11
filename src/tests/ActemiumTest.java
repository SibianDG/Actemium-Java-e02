package tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.LoginAttempt;
import domain.UserModel;
import domain.enums.EmployeeRole;
import domain.enums.LoginStatus;
import domain.enums.UserStatus;
import domain.manager.Actemium;
import exceptions.BlockedUserException;
import exceptions.PasswordException;
import repository.GenericDao;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class ActemiumTest {

    final String ADMINUSERNAME = "janJannsens123", PASSWORD = "PassWd123&", WRONGPASSWORD = "foutPas12&",
    			 TECHUSERNAME = "jooKlein123", CUSTOMERUSERNAME = "customer123",
    			 WRONGUSERNAME = "usernameDoesNotExist"; //EntityNotFoundException()
    private final UserModel admin = new ActemiumEmployee("janJannsens123", "PassWd123&", "Jan", "Jannsens", "Adress", "0470099874", "student@student.hogent.be", EmployeeRole.ADMINISTRATOR);
    private final UserModel tech = new ActemiumEmployee("jooKlein123", "PassWd123&", "Joost", "Klein", "Adress", "0470099874", "student@student.hogent.be", EmployeeRole.TECHNICIAN);
    private final ActemiumCompany theWhiteHouse = new ActemiumCompany("The White House", "America 420", "911");
    private final UserModel cust = new ActemiumCustomer("customer123", "PassWd123&", "John", "Smith", theWhiteHouse);

    @Mock
    private UserDao userRepoDummy;
    @Mock
    private GenericDao<UserModel> genericRepoDummy;
    @InjectMocks
    private Actemium actemium;

    private void trainDummy() {
    	Mockito.lenient().when(userRepoDummy.findByUsername(ADMINUSERNAME)).thenReturn(admin);
    	Mockito.lenient().when(userRepoDummy.findByUsername(TECHUSERNAME)).thenReturn(tech);
    	Mockito.lenient().when(userRepoDummy.findByUsername(CUSTOMERUSERNAME)).thenReturn(cust);
    }
    
    // does this method test too many things at once?
    // or are we allowed to test multiple things as long
    // as they relate to one another
    @Test
    public void loginAttempt_Valid() {
    	trainDummy();
        assertThrows(NullPointerException.class, () -> actemium.giveUsername());
        actemium.signIn(ADMINUSERNAME, PASSWORD);
        assertEquals(ADMINUSERNAME, actemium.giveUsername());
        assertEquals(0, admin.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    } // tried to split this test up in smaller tests
    
    @Test
    public void loginAttempt_Valid_DoesNotThrowException() {
		trainDummy();
		assertDoesNotThrow(() -> actemium.signIn(ADMINUSERNAME, PASSWORD));
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }

    @Test
    public void loginAttempt_Valid_ZeroFailedLoginAttempts() {
    	trainDummy();
        actemium.signIn(ADMINUSERNAME, PASSWORD);
        assertEquals(0, admin.getFailedLoginAttempts());
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void giveUsername_After_LoginAttempt_Valid_ReturnsSignedInUserUsername() {
    	trainDummy();
        actemium.signIn(ADMINUSERNAME, PASSWORD);
        assertEquals(ADMINUSERNAME, actemium.giveUsername());
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void giveUserType_After_LoginAttempt_Valid_ReturnsSignedInUserUserType() {
    	trainDummy();
        actemium.signIn(ADMINUSERNAME, PASSWORD);
        assertEquals("ADMINISTRATOR", actemium.giveUserRole());
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void giveFirstName_After_LoginAttempt_Valid_ReturnsSignedInUserFirstName() {
    	trainDummy();
        actemium.signIn(ADMINUSERNAME, PASSWORD);
        assertEquals("Jan", actemium.giveUserFirstName());
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void giveLastName_After_LoginAttempt_Valid_ReturnsSignedInUserLastName() {
    	trainDummy();
        actemium.signIn(ADMINUSERNAME, PASSWORD);
        assertEquals("Jannsens", actemium.giveUserLastName());
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }    
    
    @Test
    public void giveUsername_After_LoginAttempt_InValidPassword_ReturnsNullPointerException() {
    	trainDummy();
    	assertThrows(PasswordException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
    	assertThrows(NullPointerException.class, () -> actemium.giveUsername());
    	Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
    
    // seems pretty useless to test? or not?
    @Test
    public void loginAttempt_InValidUsername_ReturnsEntityNotFoundException() {
        Mockito.when(userRepoDummy.findByUsername(WRONGUSERNAME)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> actemium.signIn(WRONGUSERNAME, WRONGPASSWORD));
        Mockito.verify(userRepoDummy).findByUsername(WRONGUSERNAME);
    }  
    
    @Test
    public void loginAttempt_InValidPassword_ReturnsPasswordException() {
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> actemium.giveUsername());        
        assertThrows(PasswordException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
        assertEquals(1, admin.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void loginAttempt_5InValid_BlocksUser() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> actemium.giveUsername());
        for (int i = 0; i < 4; i++) {
        	assertEquals(i, admin.getFailedLoginAttempts());
	        assertThrows(PasswordException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
        }
    	assertEquals(4, admin.getFailedLoginAttempts());
        assertThrows(BlockedUserException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
        assertTrue(admin.getStatusAsEnum().equals(UserStatus.BLOCKED));
        assertEquals(5, admin.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(5)).findByUsername(ADMINUSERNAME);
    }
    
    @Test
    public void loginAttempt_4InValid_1Valid_ResetLoginAttempts() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> actemium.giveUsername());
    	for (int i = 0; i < 4; i++) {
    		assertEquals(i, admin.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
    	}
    	actemium.signIn(ADMINUSERNAME, PASSWORD);
    	assertEquals(0, admin.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(5)).findByUsername(ADMINUSERNAME);
    }
    
    @Test
	public void loginAttempt_4InValidAdmin_3InValidTech_1InValidAdmin_AdminUserBlocked_1ValidTech_TechUserLoginSuccess_1ValidAdmin_AdminUserStillBlocked() {
		trainDummy();
    	// There should be no user signed in when we start the test
    	assertThrows(NullPointerException.class, () -> actemium.giveUsername());
    	// 4 InValid login attempts for Administrator admin
    	for (int i = 0; i < 4; i++) {
    		assertEquals(i, admin.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
    	}
    	// 3 InValid login attempts for Technician tech
    	for (int i = 0; i < 3; i++) {
    		assertEquals(i, tech.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> actemium.signIn(TECHUSERNAME, WRONGPASSWORD));
    	}
    	// 1 InValid login attempt for Administrator admin
    	// makes for a total of 5 InValid loginAttempts => admin blocked
    	assertEquals(4, admin.getFailedLoginAttempts());
        assertTrue(admin.getStatusAsEnum().equals(UserStatus.ACTIVE));
		assertThrows(BlockedUserException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
        assertTrue(admin.getStatusAsEnum().equals(UserStatus.BLOCKED));
		// 1 Valid login attempt for Technician tech
    	actemium.signIn(TECHUSERNAME, PASSWORD);
        assertTrue(tech.getStatusAsEnum().equals(UserStatus.ACTIVE));
    	// failedLoginAttempts for tech has been reset after successful login
    	assertEquals(0, tech.getFailedLoginAttempts());
    	// even when using the correct password on a blocked user account 
    	// it will still be an invalid loginAttempt
		assertThrows(BlockedUserException.class, () -> actemium.signIn(ADMINUSERNAME, PASSWORD));
        assertTrue(admin.getStatusAsEnum().equals(UserStatus.BLOCKED));
		// failedLoginAttempts for admin keep counting up even after account has been blocked
    	assertEquals(6, admin.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(6)).findByUsername(ADMINUSERNAME);
		Mockito.verify(userRepoDummy, Mockito.times(4)).findByUsername(TECHUSERNAME);
    }
        
    // Now that our static methods have been removed from GenericDao
    // we are able to create the right Mock and we no longer need 
    // FetchType.EAGER to make this test work
    // Now it runs as it should always have run
    @Test
    public void loginAttempt_3InValid_1Valid_FittingLoginAttemptStatus() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> actemium.giveUsername());
    	for (int i = 0; i < 3; i++) {
    		assertEquals(i, admin.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> actemium.signIn(ADMINUSERNAME, WRONGPASSWORD));
    	}
    	actemium.signIn(ADMINUSERNAME, PASSWORD);
    	assertEquals(0, admin.getFailedLoginAttempts());
    	
    	List<LoginAttempt> loginAttempts = admin.getLoginAttempts();
    	
		LoginStatus[] correctLoginStatusArray = { LoginStatus.FAILED, LoginStatus.FAILED, LoginStatus.FAILED,
				LoginStatus.SUCCESS };

		LoginStatus[] loginStatusArray = (LoginStatus[]) loginAttempts.stream()
				.map(LoginAttempt::getLoginStatus)
				.collect(Collectors.toList())
				.toArray(new LoginStatus[4]);

    	assertArrayEquals(correctLoginStatusArray, loginStatusArray);
		
		Mockito.verify(userRepoDummy, Mockito.times(4)).findByUsername(ADMINUSERNAME);
    }
        
}
