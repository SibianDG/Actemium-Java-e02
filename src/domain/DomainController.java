package domain;

import repository.UserDao;
import repository.UserDaoJpa;

public class DomainController {

	private User signedInUser;
	private UserDao userRepo;

	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;


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
		User user = userRepo.findByUsername(username);
		System.out.println(user.getUsername());

		if(password.isBlank()) {
			throw new IllegalArgumentException("No password given");
		}

		//UserDaoJpa.startTransaction();

		user.increaseFailedLoginAttempts();
		System.out.println(user.getFailedLoginAttempts());

		if(user.getFailedLoginAttempts() > USER_LOGIN_MAX_ATTEMPTS) {
			userRepo.registerLoginAttempt(user, LoginStatus.FAILED);
			user.blockUser();
			throw new IllegalArgumentException(String.format("User has reached more than %d failed login attempts, account has been blocked.", USER_LOGIN_MAX_ATTEMPTS));
		}

		if(!user.getPassword().equals(password)) {
			userRepo.registerLoginAttempt(user, LoginStatus.FAILED);
			throw new IllegalArgumentException("Wrong password");
		}

		user.resetLoginAttempts();

		userRepo.registerLoginAttempt(user, LoginStatus.SUCCESS);
		System.out.println("login registered");

		//UserDaoJpa.commitTransaction();

		setSignedInUser(user);
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
