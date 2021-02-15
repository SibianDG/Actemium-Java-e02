package repository;

import domain.LoginStatus;
import domain.User;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {

	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;
	
	public UserDaoJpa() {
		super(User.class);
	}



	@Override
	public void registerLoginAttempt(String username, LoginStatus loginStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User attemptLogin(String username, String password) {
		User user = findByUsername(username);

		if(password.isBlank()) {
			throw new IllegalArgumentException("No password given");
		}

		UserDaoJpa.startTransaction();

		user.increaseFailedLoginAttempts();		
		
		if(user.getFailedLoginAttempts() > USER_LOGIN_MAX_ATTEMPTS) {
			registerLoginAttempt(username, LoginStatus.FAILED);
			user.blockUser();
			throw new IllegalArgumentException(String.format("User has reached more than %d failed login attempts, account has been blocked.", USER_LOGIN_MAX_ATTEMPTS));
		}
		
		if(!user.getPassword().equals(password)) {
			registerLoginAttempt(username, LoginStatus.FAILED);
			throw new IllegalArgumentException("Wrong password");
		}
		
		user.resetLoginAttempts();		
		
		registerLoginAttempt(username, LoginStatus.SUCCESS);
		
		UserDaoJpa.commitTransaction();
		
		return user;
		
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
