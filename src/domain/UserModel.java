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

import domain.enums.UserStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import languages.LanguageResource;

/**
 * The type User model.
 */
@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
		@NamedQuery(name = "User.findByUsername",
				query = "SELECT u FROM UserModel u WHERE u.username = :username")
})
public abstract class UserModel implements User, Serializable {

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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

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

	/**
	 * Instantiates a new User model.
	 */
	public UserModel() {

	}

	/**
	 * Instantiates a new User model.
	 *
	 * @param username  the username
	 * @param password  the password
	 * @param firstName the first name
	 * @param lastName  the last name
	 */
	public UserModel(String username, String password, String firstName, String lastName) {
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setFailedLoginAttempts(0);
		setStatus(UserStatus.ACTIVE);
	}

	/**
	 * Reset login attempts.
	 */
	public void resetLoginAttempts() {
		setFailedLoginAttempts(0);
	}

	/**
	 * Increase failed login attempts.
	 */
	public void increaseFailedLoginAttempts() {
		failedLoginAttempts++;
	}

	/**
	 * Gets login attempts.
	 *
	 * @return the login attempts
	 */
	public List<LoginAttempt> getLoginAttempts() {
		return Collections.unmodifiableList(loginAttempts);
	}

	/**
	 * Gets the user id.
	 *
	 * @return user id int
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets user id.
	 *
	 * @param userId the user id
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Gets the username.
	 *
	 * @return username.
	 */
	@Access(AccessType.PROPERTY)
	public String getUsername() {
		return username.get();
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username.set(username);
	}

	/**
	 * Gets the password.
	 *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the first name.
	 *
	 * @return first name.
	 */
	@Access(AccessType.PROPERTY)
	public String getFirstName() {
		return firstName.get();
	}

	/**
	 * Sets first name.
	 *
	 * @param firstName the first name
	 */
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	/**
	 * Gets the last name.
	 *
	 * @return last name
	 */
	@Access(AccessType.PROPERTY)
	public String getLastName() {
		return lastName.get();
	}

	/**
	 * Sets last name.
	 *
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	/**
	 * Gets number of failed login attemps.
	 *
	 * @return failed login attempts.
	 */
	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	/**
	 * Sets failed login attempts.
	 *
	 * @param failedLoginAttempts
	 */
	private void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	/**
	 * Gets status as string.
	 *
	 * @return status
	 */
	public String getStatusAsString() {
		return status.get();
	}

	/**
	 * Gets the user status.
	 *
	 * @return user status.
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public UserStatus getStatus() {
		return UserStatus.valueOf(status.get());
	}

	/**
	 * Sets status.
	 *
	 * @param status the status
	 */
	public void setStatus(UserStatus status) {
		this.status.set(String.valueOf(status));
	}

	/**
	 * Block user.
	 */
	public void blockUser() {
		setStatus(UserStatus.BLOCKED);
	}

	/**
	 * Add login attempt.
	 *
	 * @param loginAttempt the login attempt
	 */
	public void addLoginAttempt(LoginAttempt loginAttempt) {
		loginAttempts.add(loginAttempt);
	}

	/**
	 *	Gets the property username.
	 *
	 * @return username property
	 */
	public StringProperty usernameProperty() {
		return username;
	}

	/**
	 * Gets the property status.
	 *
	 * @return status property
	 */
	public StringProperty statusProperty() {
		return status;
	}

	/**
	 * Gets the property firstname.
	 *
	 * @return firstname property
	 */
	public StringProperty firstNameProperty() {
		return firstName;
	}

	/**
	 * Gets the property lastname
	 *
	 * @return lastname property
	 */
	public StringProperty lastNameProperty() {
		return lastName;
	}	

}
