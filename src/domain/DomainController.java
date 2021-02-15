package domain;

import repository.UserDaoJpa;

public class DomainController {

	private User signedInUser;
	private UserDaoJpa userRepo;

	public DomainController() {
	}

	public void setSignedInUser(User signedInUser) {
		this.signedInUser = signedInUser;
	}

	public void signIn(String username, String password) {
		User signedInUser = userRepo.findByUsername(username);

	}

	public String giveUserType() {
		throw new UnsupportedOperationException();
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
