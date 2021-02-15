package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.Administrator;
import domain.DomainController;
import domain.User;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class DomeinTest {
//	@Mock
//    private GenericDao<User> userRepo2;
	@Mock
    private UserDao userRepo;
    @InjectMocks
    private DomainController domain;
    
    @Test
    public void findByUsername() {
//    	final String WINKELNAAM = "Station";
       final String USERNAME = "thoDirven123", PASSWORD = "Passwd123";

//     Winkel eenWinkel = new Winkel(WINKELNAAM);   
       User aUser = new Administrator(USERNAME, PASSWORD, "Thomas", "Dirven");

//     Mockito.when(userRepo2.findAll()).thenReturn(Arrays.asList(eenWinkel));
       Mockito.when(userRepo.findByUsername(USERNAME)).thenReturn(aUser);
       
       assertThrows(NullPointerException.class, () -> domain.giveUsername());
//       assertFalse(domain.giveUsername());
       domain.signIn(USERNAME, PASSWORD);
//       assertTrue(eenWinkel.getBierSet().contains(eenBier));
       assertTrue(domain.giveUsername().equals(USERNAME));
//       Mockito.verify(userRepo2).findAll();
       Mockito.verify(userRepo).findByUsername(USERNAME); 
    }

}