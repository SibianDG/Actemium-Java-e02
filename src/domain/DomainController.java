package domain;

import repository.UserDao;
import repository.UserDaoJpa;

public class DomainController {

	private UserModel signedInUserModel;
	private UserDao userRepo;

	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;

	public DomainController2(UserDao userRepo) {
		this.userRepo = userRepo;
	}

	//TODO constructor vs setter injection?
	public DomainController() {
		this(new UserDaoJpa());
//		setUserRepo(new UserDaoJpa());
	}

	public void setUserRepo(UserDao mock) {
		userRepo = mock;
	}

	public void setSignedInUser(UserModel signedInUserModel) {
		this.signedInUserModel = signedInUserModel;
	}

	public void signIn(String username, String password) {
		UserModel userModel = userRepo.findByUsername(username);

		if (password.isBlank()) {
			throw new IllegalArgumentException("No password given");
		}

		UserDaoJpa.startTransaction();
		
		// user account already blocked, only the failed login attempt needs to registered
		if (userModel.getStatus().equals(UserStatus.BLOCKED)) {
			userModel.increaseFailedLoginAttempts();
			userRepo.registerLoginAttempt(userModel, LoginStatus.FAILED);
			UserDaoJpa.commitTransaction();
			throw new IllegalArgumentException(String.format(
					"User account has been blocked because more than %d failed login attempts have been registered."
					+ "\nPlease contact your system administrator.",
					USER_LOGIN_MAX_ATTEMPTS));
		}
				
		// check password
		if (!userModel.getPassword().equals(password)) {
			userModel.increaseFailedLoginAttempts();
			userRepo.registerLoginAttempt(userModel, LoginStatus.FAILED);
			// block user after 5 failed login attempts
			if (userModel.getFailedLoginAttempts() >= USER_LOGIN_MAX_ATTEMPTS) {
				userModel.blockUser();
				UserDaoJpa.commitTransaction();
				throw new IllegalArgumentException(
						String.format("Wrong password\nUser has reached more than %d failed login attempts, account has been blocked.",
								USER_LOGIN_MAX_ATTEMPTS));
			}
			UserDaoJpa.commitTransaction();
			throw new IllegalArgumentException(String.format("Wrong password\nOnly %d attempts remaining", 5 - userModel.getFailedLoginAttempts()));
		}

		// correct password
		userModel.resetLoginAttempts();

		userRepo.registerLoginAttempt(userModel, LoginStatus.SUCCESS);

		UserDaoJpa.commitTransaction();

		setSignedInUser(userModel);
		System.out.println("Just signed in: " + signedInUserModel.getUsername());
	}

	public String giveUserType() {
		return signedInUserModel.getClass()
				.getSimpleName();
	}

	public String giveUsername() {
		return signedInUserModel.getUsername();
	}

	public String giveUserFirstName() {
		return signedInUserModel.getFirstName();
	}

	public String giveUserLastName() {
		return signedInUserModel.getLastName();
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
