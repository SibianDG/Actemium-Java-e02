package repository;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domain.LoginAttempt;
import domain.LoginStatus;
import domain.UserModel;

public class UserDaoJpa extends GenericDaoJpa<UserModel> implements UserDao {


	public UserDaoJpa() {
		super(UserModel.class);
	}

	@Override
	public void registerLoginAttempt(UserModel userModel, LoginStatus loginStatus) {
		
		LoginAttempt loginAttempt = new LoginAttempt(LocalDateTime.now(), userModel.getUsername(), loginStatus);
		
		//userModel.addLoginAttempt(loginAttempt);
		
		em.persist(loginAttempt);		
		
	}

	public void test(){

	}


	@Override
	public UserModel findByUsername(String username) {
		try {
			return em.createNamedQuery("User.findByUsername", UserModel.class)
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}
