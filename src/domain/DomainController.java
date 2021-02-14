package domain;

public class DomainController {

	private User signedInUser;

	public DomainController() {
		throw new UnsupportedOperationException();
	}

	public void setSignedInUser(User signedInUser) {
		throw new UnsupportedOperationException();
	}

	public void signIn(String username, String password) {
		throw new UnsupportedOperationException();
	}

	public String giveUserType() {
		throw new UnsupportedOperationException();
	}

	public String giveUsername() {
		throw new UnsupportedOperationException();
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
