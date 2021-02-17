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
import domain.UserModel;
import domain.UserStatus;
import exceptions.BlockedUserException;
import exceptions.PasswordException;
import repository.GenericDao;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class DomainTest {

    final String USERNAME = "thoDirven123", PASSWORD = "Passwd123&", WRONGPASSWORD = "foutPas12&",
    		WRONGUSERNAME = "usernameDoesNotExist"; //EntityNotFoundException()
    UserModel aUser = new Administrator(USERNAME, PASSWORD, "Thomas", "Dirven");

    @Mock
    private UserDao userRepoDummy;
    @Mock
    private GenericDao<UserModel> genericRepoDummy;
    @InjectMocks
    private DomainController domain;

    private void trainDummy() {
    	Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
    }
    
    // does this method test too many things at once?
    // or are we allowed to test multiple things as long
    // as they relate to one another
    @Test
    public void loginAttempt_Valid() {
    	trainDummy();
        assertThrows(NullPointerException.class, () -> domain.giveUsername());
        domain.signIn(USERNAME, PASSWORD);
        assertTrue(domain.giveUsername().equals(USERNAME));
        assertEquals(0, aUser.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    } // tried to split this test up in smaller tests
    
    @Test
    public void loginAttempt_Valid_DoesNotThrowException() {
    	trainDummy();
    	assertDoesNotThrow(()->domain.signIn(USERNAME, PASSWORD));
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }

    @Test
    public void loginAttempt_Valid_ZeroFailedLoginAttempts() {
    	trainDummy();
        domain.signIn(USERNAME, PASSWORD);
        assertEquals(0, aUser.getFailedLoginAttempts());
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void giveUsername_After_LoginAttempt_Valid_ReturnsSignedInUserUsername() {
    	trainDummy();
        domain.signIn(USERNAME, PASSWORD);
        assertTrue(domain.giveUsername().equals(USERNAME));
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void giveUserType_After_LoginAttempt_Valid_ReturnsSignedInUserUserType() {
    	trainDummy();
        domain.signIn(USERNAME, PASSWORD);
        assertTrue(domain.giveUserType().equals("Administrator"));
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void giveFirstName_After_LoginAttempt_Valid_ReturnsSignedInUserFirstName() {
    	trainDummy();
        domain.signIn(USERNAME, PASSWORD);
        assertTrue(domain.giveUserFirstName().equals("Thomas"));
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void giveLastName_After_LoginAttempt_Valid_ReturnsSignedInUserLastName() {
    	trainDummy();
        domain.signIn(USERNAME, PASSWORD);
        assertTrue(domain.giveUserLastName().equals("Dirven"));
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }    
    
    @Test
    public void giveUsername_After_LoginAttempt_InValidPassword_ReturnsNullPointerException() {
    	trainDummy();
    	assertThrows(PasswordException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    // this tests results in a Stubbing argument mismatch
    // seems pretty useless to test?
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
        assertThrows(PasswordException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
        assertEquals(1, aUser.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void loginAttempt_5InValid_BlocksUser() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
        for (int i = 0; i < 4; i++) {
        	assertEquals(i, aUser.getFailedLoginAttempts());
	        assertThrows(PasswordException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
        }
    	assertEquals(4, aUser.getFailedLoginAttempts());
        assertThrows(BlockedUserException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
        assertTrue(aUser.getStatus().equals(UserStatus.BLOCKED));
        assertEquals(5, aUser.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(5)).findByUsername(USERNAME);
    }
    
    @Test
    public void loginAttempt_4InValid_1Valid_ResetLoginAttempts() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	for (int i = 0; i < 4; i++) {
    		assertEquals(i, aUser.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
    	}
    	domain.signIn(USERNAME, PASSWORD);
    	assertEquals(0, aUser.getFailedLoginAttempts());

		Mockito.verify(userRepoDummy, Mockito.times(5)).findByUsername(USERNAME);
    }
    
    
    //TODO can we test this? should we be able to test this?
    // now that we have EAGER loading for LoginAttempts we can test this
    // however, eager loading for LoginAttempts wil make our program slower
    // but this wil allow an admin for example to view all the login attempts by a user
    // conveniently from the dashboard
    @Test
    public void loginAttempt_3InValid_1Valid_FittingLoginAttemptStatus() {    	
    	trainDummy();
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	for (int i = 0; i < 3; i++) {
    		assertEquals(i, aUser.getFailedLoginAttempts());
    		assertThrows(PasswordException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
    	}
    	domain.signIn(USERNAME, PASSWORD);
    	assertEquals(0, aUser.getFailedLoginAttempts());
    	
    	List<LoginAttempt> loginAttempts = aUser.getLoginAttempts();
    	
		LoginStatus[] correctLoginStatusArray = { LoginStatus.FAILED, LoginStatus.FAILED, LoginStatus.FAILED,
				LoginStatus.SUCCESS };

		LoginStatus[] loginStatusArray = (LoginStatus[]) loginAttempts.stream()
				.map(LoginAttempt::getLoginStatus)
				.collect(Collectors.toList())
				.toArray(new LoginStatus[4]);

    	assertArrayEquals(correctLoginStatusArray, loginStatusArray);
		
		Mockito.verify(userRepoDummy, Mockito.times(4)).findByUsername(USERNAME);
    }



}