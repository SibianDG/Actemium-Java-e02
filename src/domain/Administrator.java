package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;

@Entity
public class Administrator extends User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}

	public Administrator(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);

	}
}
