package domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class LoginAttempt implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime dateAndTime;
	private String username;
	private LoginStatus loginStatus;

	public LoginAttempt(LocalDateTime dateAndTime, String username, LoginStatus loginStatus) {
		throw new UnsupportedOperationException();
	}

	public LoginAttempt() {

	}
}
