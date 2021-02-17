package tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

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
public class DomeinTest {

    final String USERNAME = "thoDirven123", PASSWORD = "Passwd123&", WRONGPASSWORD = "foutPas12&";
    UserModel aUser = new Administrator(USERNAME, PASSWORD, "Thomas", "Dirven");

    @Mock
    private UserDao userRepoDummy;
    @Mock
    private GenericDao<UserModel> genericRepoDummy;
    @InjectMocks
    private DomainController domain;

    @Test
    public void LoginAttempt_Valid_() {
        Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
        assertThrows(NullPointerException.class, () -> domain.giveUsername());
        domain.signIn(USERNAME, PASSWORD);
        assertTrue(domain.giveUsername().equals(USERNAME));
        assertEquals(0, aUser.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }

    @Test
    public void LoginAttempt_InValid_ReturnsPasswordException() {
        Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
        assertThrows(NullPointerException.class, () -> domain.giveUsername());        
        assertThrows(PasswordException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
        assertEquals(1, aUser.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void LoginAttempt_5InValid_BlocksUser() {    	
        Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
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
    public void LoginAttempt_4InValid_1Valid_ResetLoginAttempts() {    	
    	Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
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
    public void LoginAttempt_3InValid_1Valid_FittingLoginAttemptStatus() {    	
    	Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
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