package domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
	@NamedQuery(name = "User.findByUsername",
			query = "SELECT u FROM UserModel u WHERE u.username = :username")
})
public abstract class UserModel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@OneToMany(
			mappedBy = "userModel",
			cascade = CascadeType.REMOVE
	)
	private List<LoginAttempt> loginAttempts = new ArrayList<>();;

//	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private int failedLoginAttempts;
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	public UserModel() {
	}

	public UserModel(String username, String password, String firstName, String lastName) {
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setFailedLoginAttempts(0);
		setStatus(UserStatus.ACTIVE);
	}

	public void resetLoginAttempts() {
		setFailedLoginAttempts(0);
	}

	public void increaseFailedLoginAttempts() {
		failedLoginAttempts++;
//		if (failedLoginAttempts > USER_LOGIN_MAX_ATTEMPTS) {
//			throw new IllegalArgumentException("User has reached more than 5 failed login attempts, account has been blocked.");
//		}
	}

	public List<LoginAttempt> getLoginAttempts() {
		return Collections.unmodifiableList(loginAttempts);
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		String usernameRegex = "[A-Za-z0-9]+";
		if(username == null || username.isBlank() || !username.matches(usernameRegex)) {
			throw new IllegalArgumentException("Invalid Username");
		}
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',.?/*~$^+=<>]).{8,}$";
		if(password == null || password.isBlank() || !password.matches(passwordRegex)){
			throw new IllegalArgumentException("Invalid Password");
		}
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		String firstNameRegex = "[^0-9]+";
		if(firstName == null || firstName.isBlank() || !firstName.matches(firstNameRegex)){
			throw new IllegalArgumentException("Invalid FirstName");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		String lastNameRegex = "[^0-9]+";
		if(lastName == null || lastName.isBlank() || !lastName.matches(lastNameRegex)){
			throw new IllegalArgumentException("Invalid LastName");
		}
		this.lastName = lastName;
	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	private void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void blockUser() {
		setStatus(UserStatus.BLOCKED);
	}
	
	public void addLoginAttempt(LoginAttempt loginAttempt) {
		loginAttempts.add(loginAttempt);
	}
	
}