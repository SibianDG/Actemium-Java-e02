package domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import languages.LanguageResource;

@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
		@NamedQuery(name = "User.findByUsername",
				query = "SELECT u FROM UserModel u WHERE u.username = :username")
})
public abstract class UserModel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	// FetchType.EAGER will probably slow down our program by a lot
	// This will only be usefull if an admin wants to see all login attempts by a
	// user in his dashboard
	@OneToMany(
			mappedBy = "userModel",
			cascade = CascadeType.PERSIST
			// We will not use EAGER loading for now
			// mock test that used to be dependand on this still works
//			fetch = FetchType.EAGER
	)
	private List<LoginAttempt> loginAttempts = new ArrayList<>();

//	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Transient
	private StringProperty username = new SimpleStringProperty();
	private String password;
	@Transient
	private StringProperty firstName = new SimpleStringProperty();
	@Transient
	private StringProperty lastName = new SimpleStringProperty();
	private int failedLoginAttempts;
	@Transient
	private StringProperty status = new SimpleStringProperty();

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Access(AccessType.PROPERTY)
	public String getUsername() {
		return username.get();
	}

	public void setUsername(String username) {
		String usernameRegex = "[A-Za-z0-9]+";
		if(username == null || username.isBlank() || !username.matches(usernameRegex)) {
			throw new IllegalArgumentException(LanguageResource.getString("username_invalid"));
		}
		this.username.set(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()â€“{}:;',.?/*~$^+=<>]).{8,}$";
		if(password == null || password.isBlank() || !password.matches(passwordRegex)){
			throw new IllegalArgumentException(LanguageResource.getString("password_invalid"));
		}
		this.password = password;
	}

	@Access(AccessType.PROPERTY)
	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		String firstNameRegex = "[^0-9]+";
		if(firstName == null || firstName.isBlank() || !firstName.matches(firstNameRegex)){
			throw new IllegalArgumentException(LanguageResource.getString("firstname_invalid"));
		}
		this.firstName.set(firstName);
	}

	@Access(AccessType.PROPERTY)
	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		String lastNameRegex = "[^0-9]+";
		if(lastName == null || lastName.isBlank() || !lastName.matches(lastNameRegex)){
			throw new IllegalArgumentException(LanguageResource.getString("lastname_invalid"));
		}
		this.lastName.set(lastName);
	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	private void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public String getStatus() {
		return status.get();
	}

	public UserStatus getStatusAsEnum() {
		return UserStatus.valueOf(status.get());
	}

	public void setStatus(UserStatus status) {
		this.status.set(String.valueOf(status));
	}

	public void blockUser() {
		setStatus(UserStatus.BLOCKED);
	}

	public void addLoginAttempt(LoginAttempt loginAttempt) {
		loginAttempts.add(loginAttempt);
	}

	public StringProperty usernameProperty() {
		return username;
	}

	public StringProperty statusProperty() {
		return status;
	}

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public StringProperty lastNameProperty() {
		return lastName;
	}

}