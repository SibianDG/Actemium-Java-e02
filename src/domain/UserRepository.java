package domain;

public class UserRepository {

	private static int USER_LOGIN_MAX_ATTEMPTS;

	public User giveUser(String username, String password) {
		throw new UnsupportedOperationException();
	}

	public User findByUsername(String username) {
		throw new UnsupportedOperationException();
	}

	public void registerLoginAttempt(String username, boolean isSuccessful) {
		throw new UnsupportedOperationException();
	}
}
