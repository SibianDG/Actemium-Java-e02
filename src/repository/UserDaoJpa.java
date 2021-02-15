package repository;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domain.LoginAttempt;
import domain.LoginStatus;
import domain.User;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {


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
