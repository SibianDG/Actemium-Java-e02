package repository;

import domain.LoginStatus;
import domain.User;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {

	public UserDaoJpa() {
		super(User.class);
	}



	@Override
	public void registerLoginAttempt(String username, LoginStatus loginStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attemptLogin(String username, String password) {
		User user = findByUsername(username);

		if(password.isBlank()) {
			throw new IllegalArgumentException("No password given");
		}

		UserDaoJpa.startTransaction();

		user.increaseFailedLoginAttempts();

		UserDaoJpa.commitTransaction();
	}

	@Override
	public User findByUsername(String username) {
		try {
			return em.createNamedQuery("User.findByUsername", User.class)
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}
