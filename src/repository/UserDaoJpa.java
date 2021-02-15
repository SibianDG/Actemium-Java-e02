package repository;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domain.LoginAttempt;
import domain.LoginStatus;
import domain.User;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {

	private static final int USER_LOGIN_MAX_ATTEMPTS = 5;
	
	public UserDaoJpa() {
		super(User.class);
	}

	@Override
	public void registerLoginAttempt(User user, LoginStatus loginStatus) {		
		
		LoginAttempt loginAttempt = new LoginAttempt(LocalDateTime.now(), user.getUsername(), loginStatus);
		
		user.addLoginAttempt(loginAttempt);
		
		em.persist(loginAttempt);		
		
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
			registerLoginAttempt(user, LoginStatus.FAILED);
			user.blockUser();
			throw new IllegalArgumentException(String.format("User has reached more than %d failed login attempts, account has been blocked.", USER_LOGIN_MAX_ATTEMPTS));
		}
		
		if(!user.getPassword().equals(password)) {
			registerLoginAttempt(user, LoginStatus.FAILED);
			throw new IllegalArgumentException("Wrong password");
		}
		
		user.resetLoginAttempts();		
		
		registerLoginAttempt(user, LoginStatus.SUCCESS);
		
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
