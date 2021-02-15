package domain;

import javax.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;

@Entity
public class Administrator extends UserModel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}

	public Administrator(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);

	}
}
