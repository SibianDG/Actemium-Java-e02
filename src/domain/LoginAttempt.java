package domain;

import java.time.LocalDateTime;

public class LoginAttempt {

	private LocalDateTime dateAndTime;
	private String username;
	private LoginStatus loginStatus;

	public LoginAttempt(LocalDateTime dateAndTime, String username, LoginStatus loginStatus) {
		throw new UnsupportedOperationException();
	}
}
