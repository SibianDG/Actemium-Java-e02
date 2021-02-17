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
import repository.GenericDao;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class DomeinTest {

    final String USERNAME = "thoDirven123", PASSWORD = "Passwd123&", WRONGPASSWORD = "foutPas12&";
    UserModel aUser = new Administrator(USERNAME, PASSWORD, "Thomas", "Dirven");

    //	@Mock
//    private GenericDao<User> userRepo2;
    @Mock
    private UserDao userRepoDummy;
    @Mock
    private GenericDao<UserModel> genericRepoDummy;
    @InjectMocks
    private DomainController domain;

    @Test
    public void LoginAttempt_Valid() {
//    	final String WINKELNAAM = "Station";

//     Winkel eenWinkel = new Winkel(WINKELNAAM);

//     Mockito.when(userRepo2.findAll()).thenReturn(Arrays.asList(eenWinkel));
        //Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
        //Mockito.mockStatic(GenericDao.class);
        //Mockito.doNothing().when(UserDaoJpa.startTransaction());

        Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
//       domain.setUserRepo(userRepoDummy);
        assertThrows(NullPointerException.class, () -> domain.giveUsername());
//       assertFalse(domain.giveUsername());
        domain.signIn(USERNAME, PASSWORD);
//       assertTrue(eenWinkel.getBierSet().contains(eenBier));
        assertTrue(domain.giveUsername().equals(USERNAME));
//       Mockito.verify(userRepo2).findAll();
        assertEquals(0, aUser.getFailedLoginAttempts());
        Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }

    @Test
    public void LoginAttempt_InValid() {
//        Mockito.when(userRepoDummy.attemptLogin(USERNAME, "WRONGPASSWORD")).thenReturn(aUser);
//        assertThrows(NullPointerException.class, () -> domain.giveUsername());
//        domain.signIn(USERNAME, "WRONGPASSWORD");
//        assertTrue(domain.giveUsername().equals(USERNAME));
//        assertEquals(1, aUser.getFailedLoginAttempts());
//        Mockito.verify(userRepoDummy).attemptLogin(USERNAME, "WRONGPASSWORD");
    }
    
    @Test
    public void LoginAttempt_5InValid_BlocksUser() {    	
        Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
        assertThrows(NullPointerException.class, () -> domain.giveUsername());
        for (int i = 0; i < 5; i++) {
        	assertEquals(i, aUser.getFailedLoginAttempts());
	        assertThrows(IllegalArgumentException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
        }
        assertTrue(aUser.getStatus().equals(UserStatus.BLOCKED));
        assertEquals(5, aUser.getFailedLoginAttempts());
		//TODO verify 5 times
//      Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    @Test
    public void LoginAttempt_4InValid_1Valid_ResetLoginAttempts() {    	
    	Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	for (int i = 0; i < 4; i++) {
    		assertEquals(i, aUser.getFailedLoginAttempts());
    		assertThrows(IllegalArgumentException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
    	}
    	domain.signIn(USERNAME, PASSWORD);
    	assertEquals(0, aUser.getFailedLoginAttempts());
		//TODO verify 5 times
//    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }
    
    
    //TODO can we test this? should we be able to test this?
    @Test
    public void LoginAttempt_3InValid_1Valid_FittingLoginAttemptStatus() {    	
    	Mockito.when(userRepoDummy.findByUsername(USERNAME)).thenReturn(aUser);
    	assertThrows(NullPointerException.class, () -> domain.giveUsername());
    	for (int i = 0; i < 3; i++) {
    		assertEquals(i, aUser.getFailedLoginAttempts());
    		assertThrows(IllegalArgumentException.class, () -> domain.signIn(USERNAME, WRONGPASSWORD));
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
		
		//TODO verify 4 times
//    	Mockito.verify(userRepoDummy).findByUsername(USERNAME);
    }



}