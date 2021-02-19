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

import domain.Administrator;
import domain.DomainController;
import domain.LoginAttempt;
import domain.LoginStatus;
import domain.Technician;
import domain.UserModel;
import domain.UserStatus;
import exceptions.BlockedUserException;
import exceptions.PasswordException;
import repository.GenericDao;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class DomainTest {

    final String ADMIN = "thoDirven123", PASSWORD = "Passwd123&", WRONGPASSWORD = "foutPas12&",
    			 TECH = "jooKlein123",
    			 WRONGUSERNAME = "usernameDoesNotExist"; //EntityNotFoundException()
    UserModel admin = new Administrator(ADMIN, PASSWORD, "Thomas", "Dirven");
    UserModel tech = new Technician(TECH, PASSWORD, "Joost", "Klein");

    @Mock
    private UserDao userRepoDummy;
    @Mock
    private GenericDao<UserModel> genericRepoDummy;
    @InjectMocks
    private DomainController domain;

    private void trainDummy() {
    	Mockito.lenient().when(userRepoDummy.findByUsername(ADMIN)).thenReturn(admin);
    	Mockito.lenient().when(userRepoDummy.findByUsername(TECH)).thenReturn(tech);
    }
    
    // does this method test too many things at once?
    // or are we allowed to test multiple things as long
    // as they relate to one another
    @Test
    public void loginAttempt_Valid() {
    	trainDummy();
        assertThrows(NullPointerException.class, () -> domain.giveUsername());
        domain.signIn(ADMIN, PASSWORD);
        assertEquals(ADMIN, domain.giveUsername());
        assertEquals(0, admin.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    } // tried to split this test up in smaller tests
    
    @Test
    public void loginAttempt_Valid_DoesNotThrowException() {
		trainDummy();
		assertDoesNotThrow(() -> domain.signIn(ADMIN, PASSWORD));
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }

    @Test
    public void loginAttempt_Valid_ZeroFailedLoginAttempts() {
    	trainDummy();
        domain.signIn(ADMIN, PASSWORD);
        assertEquals(0, admin.getFailedLoginAttempts());
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }
    
    @Test
    public void giveUsername_After_LoginAttempt_Valid_ReturnsSignedInUserUsername() {
    	trainDummy();
        domain.signIn(ADMIN, PASSWORD);
        assertEquals(ADMIN, domain.giveUsername());
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }
    
    @Test
    public void giveUserType_After_LoginAttempt_Valid_ReturnsSignedInUserUserType() {
    	trainDummy();
        domain.signIn(ADMIN, PASSWORD);
        assertEquals("Administrator", domain.giveUserType());
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }
    
    @Test
    public void giveFirstName_After_LoginAttempt_Valid_ReturnsSignedInUserFirstName() {
    	trainDummy();
        domain.signIn(ADMIN, PASSWORD);
        assertEquals("Thomas", domain.giveUserFirstName());
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }
    
    @Test
    public void giveLastName_After_LoginAttempt_Valid_ReturnsSignedInUserLastName() {
    	trainDummy();
        domain.signIn(ADMIN, PASSWORD);
        assertEquals("Dirven", domain.giveUserLastName());
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }    
    
    @Test
    public void giveUsername_After_LoginAttempt_InValidPassword_ReturnsNullPointerException() {
    	trainDummy();
    	assertThrows(PasswordException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }
    
    // seems pretty useless to test? or not?
    @Test
    public void loginAttempt_InValidUsername_ReturnsEntityNotFoundException() {
        Mockito.when(userRepoDummy.findByUsername(WRONGUSERNAME)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> domain.signIn(WRONGUSERNAME, WRONGPASSWORD));
        Mockito.verify(userRepoDummy).findByUsername(WRONGUSERNAME);
    }  
    
    @Test
    public void loginAttempt_InValidPassword_ReturnsPasswordException() {
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());        
        assertThrows(PasswordException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
        assertEquals(1, admin.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(ADMIN);
    }
    
    @Test
    public void loginAttempt_5InValid_BlocksUser() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
        for (int i = 0; i < 4; i++) {
        	assertEquals(i, admin.getFailedLoginAttempts());
	        assertThrows(PasswordException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
        }
    	assertEquals(4, admin.getFailedLoginAttempts());
        assertThrows(BlockedUserException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
        assertTrue(admin.getStatus().equals(UserStatus.BLOCKED));
        assertEquals(5, admin.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(5)).findByUsername(ADMIN);
    }
    
    @Test
    public void loginAttempt_4InValid_1Valid_ResetLoginAttempts() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	for (int i = 0; i < 4; i++) {
    		assertEquals(i, admin.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
    	}
    	domain.signIn(ADMIN, PASSWORD);
    	assertEquals(0, admin.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(5)).findByUsername(ADMIN);
    }
    
    @Test
    public void loginAttempt_4InValidAdmin_3InValidTech_1InValidAdmin_AdminUserBlocked_1ValidTech_TechUserLoginSuccess_1ValidAdmin_AdminUserStillBlocked() {    	
    	trainDummy();
    	// There should be no user signed in when we start the test
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	// 4 InValid login attempts for Administrator admin
    	for (int i = 0; i < 4; i++) {
    		assertEquals(i, admin.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
    	}
    	// 3 InValid login attempts for Technician tech
    	for (int i = 0; i < 3; i++) {
    		assertEquals(i, tech.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> domain.signIn(TECH, WRONGPASSWORD));
    	}
    	// 1 InValid login attempt for Administrator admin
    	// makes for a total of 5 InValid loginAttempts => admin blocked
    	assertEquals(4, admin.getFailedLoginAttempts());
        assertTrue(admin.getStatus().equals(UserStatus.ACTIVE));
		assertThrows(BlockedUserException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
        assertTrue(admin.getStatus().equals(UserStatus.BLOCKED));
		// 1 Valid login attempt for Technician tech
    	domain.signIn(TECH, PASSWORD);
        assertTrue(tech.getStatus().equals(UserStatus.ACTIVE));
    	// failedLoginAttempts for tech has been reset after successful login
    	assertEquals(0, tech.getFailedLoginAttempts());
    	// even when using the correct password on a blocked user account 
    	// it will still be an invalid loginAttempt
		assertThrows(BlockedUserException.class, () -> domain.signIn(ADMIN, PASSWORD));
        assertTrue(admin.getStatus().equals(UserStatus.BLOCKED));
		// failedLoginAttempts for admin keep counting up even after account has been blocked
    	assertEquals(6, admin.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(6)).findByUsername(ADMIN);
		Mockito.verify(userRepoDummy, Mockito.times(4)).findByUsername(TECH);
    }
        
    // Now that our static methods have been removed from GenericDao
    // we are able to create the right Mock and we no longer need 
    // FetchType.EAGER to make this test work
    // Now it runs as it should always have run
    @Test
    public void loginAttempt_3InValid_1Valid_FittingLoginAttemptStatus() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	for (int i = 0; i < 3; i++) {
    		assertEquals(i, admin.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> domain.signIn(ADMIN, WRONGPASSWORD));
    	}
    	domain.signIn(ADMIN, PASSWORD);
    	assertEquals(0, admin.getFailedLoginAttempts());
    	
    	List<LoginAttempt> loginAttempts = admin.getLoginAttempts();
    	
		LoginStatus[] correctLoginStatusArray = { LoginStatus.FAILED, LoginStatus.FAILED, LoginStatus.FAILED,
				LoginStatus.SUCCESS };

		LoginStatus[] loginStatusArray = (LoginStatus[]) loginAttempts.stream()
				.map(LoginAttempt::getLoginStatus)
				.collect(Collectors.toList())
				.toArray(new LoginStatus[4]);

    	assertArrayEquals(correctLoginStatusArray, loginStatusArray);
		
		Mockito.verify(userRepoDummy, Mockito.times(4)).findByUsername(ADMIN);
    }
    

}
