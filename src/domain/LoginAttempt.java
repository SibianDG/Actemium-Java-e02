package domain;

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
	
	public LoginAttempt() {
	}

	public LoginAttempt(LocalDateTime dateAndTime, UserModel userModel, LoginStatus loginStatus) {
		super();
		this.dateAndTime = dateAndTime;
		this.username = userModel.getUsername();
		this.loginStatus = loginStatus;
		//TODO test, remove if possible, can stay if necessary
		this.userModel = userModel;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
}
