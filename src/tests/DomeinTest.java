package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.Administrator;
import domain.DomainController;
import domain.User;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.UserDao;
import repository.UserDaoJpa;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DomeinTest {

    final String USERNAME = "thoDirven123", PASSWORD = "Passwd123&";
    User aUser = new Administrator(USERNAME, PASSWORD, "Thomas", "Dirven");

    //	@Mock
//    private GenericDao<User> userRepo2;
    @Mock
    private UserDao userRepoDummy;
    @Mock
    private GenericDao<User> genericRepoDummy;
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



}