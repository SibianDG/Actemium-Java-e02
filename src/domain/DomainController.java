package domain;

import repository.UserDao;
import repository.UserDaoJpa;

public class DomainController {

	private User signedInUser;
	private UserDao userRepo;

	public DomainController(UserDao userRepo) {
		this.userRepo = userRepo;
	}
	
	
	public DomainController() {
		this(new UserDaoJpa());
//		setUserRepo(new UserDaoJpa());
	}

	public void setUserRepo(UserDao mock){
        userRepo = mock;
    }
	
	public void setSignedInUser(User signedInUser) {
		this.signedInUser = signedInUser;
	}

	public void signIn(String username, String password) {
		User signedInUser = userRepo.attemptLogin(username, password);
		setSignedInUser(signedInUser);
	}

	public String giveUserType() {
		return signedInUser.getClass().getSimpleName();
	}

	public String giveUsername() {
		return signedInUser.getUsername();
	}

	public void registerCustomer(String username, String password, String firstName, String lastName) {
		throw new UnsupportedOperationException();
	}

	public void registerEmployee(String username, String password, String firstName, String lastName, String address,
			int phoneNumber, String emailAddress, String role) {
		throw new UnsupportedOperationException();
	}

	public void modifyCustomer(String username, String password, String firstName, String lastName) {
		throw new UnsupportedOperationException();
	}

	public void modifyEmployee(String username, String password, String firstName, String lastName, String address,
			int phoneNumber, String emailAddress, String role) {
		throw new UnsupportedOperationException();
	}
}
