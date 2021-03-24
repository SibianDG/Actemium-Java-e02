package domain;

import domain.enums.LoginStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The type Login attempt.
 */
@Entity
public class LoginAttempt implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime dateAndTime;
	private String username;
	@Enumerated(EnumType.STRING)
	private LoginStatus loginStatus;
	@ManyToOne
	private UserModel userModel;

	/**
	 * Instantiates a new Login attempt.
	 */
	public LoginAttempt() {
		super();
	}

	/**
	 * Instantiates a new Login attempt.
	 *
	 * @param userModel   the user model
	 * @param loginStatus the login status
	 */
// LoginAttempts for nonExisting usernames are not logged in the database
	public LoginAttempt(UserModel userModel, LoginStatus loginStatus) {
		super();
		this.dateAndTime = LocalDateTime.now();
		this.userModel = userModel;
		this.username = userModel.getUsername();
		this.loginStatus = loginStatus;
	}

	/**
	 * Gets date and time.
	 *
	 * @return the date and time
	 */
	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	/**
	 * Sets date and time.
	 *
	 * @param dateAndTime the date and time
	 */
	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets login status.
	 *
	 * @return the login status
	 */
	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	/**
	 * Sets login status.
	 *
	 * @param loginStatus the login status
	 */
	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * Gets user model.
	 *
	 * @return the user model
	 */
	public UserModel getUserModel() {
		return userModel;
	}

	/**
	 * Sets user model.
	 *
	 * @param userModel the user model
	 */
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
	
}
