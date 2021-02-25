package repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domain.LoginAttempt;
import domain.LoginStatus;
import domain.UserModel;
import languages.LanguageResource;

public class UserDaoJpa extends GenericDaoJpa<UserModel> implements UserDao {


	public UserDaoJpa() {
		super(UserModel.class);
	}

	@Override
	public void registerLoginAttempt(UserModel userModel, LoginStatus loginStatus) {
		
		LoginAttempt loginAttempt = new LoginAttempt(LocalDateTime.now(), userModel, loginStatus);
		
		//userModel.addLoginAttempt(loginAttempt);
		
		em.persist(loginAttempt);		
		
	}

	@Override
	public UserModel findByUsername(String username) {
		try {
			return em.createNamedQuery("User.findByUsername", UserModel.class)
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException(LanguageResource.getString("wrongUsernamePasswordCombination"));
		}
	}
}
