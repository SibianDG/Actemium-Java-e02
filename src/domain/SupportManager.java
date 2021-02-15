package domain;

import javax.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;

@Entity
public class SupportManager extends UserModel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public SupportManager() {
		super();
	}

	public SupportManager(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}
}
