package domain;

import domain.enums.UserStatus;
import javafx.beans.property.StringProperty;

public interface User {

	// getters
//	public List<LoginAttempt> getLoginAttempts();

	public long getUserId();

	public String getUsername();

	public String getPassword();

	public String getFirstName();

	public String getLastName();

	public int getFailedLoginAttempts();

	public String getStatus();

	public UserStatus getStatusAsEnum();
	
	public StringProperty usernameProperty();

	public StringProperty statusProperty();

	public StringProperty firstNameProperty();

	public StringProperty lastNameProperty();

}
