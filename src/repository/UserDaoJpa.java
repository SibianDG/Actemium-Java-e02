package repository;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domain.ActemiumEmployee;
import domain.LoginAttempt;
import domain.UserModel;
import domain.enums.LoginStatus;
import languages.LanguageResource;

public class UserDaoJpa extends GenericDaoJpa<UserModel> implements UserDao {


	public UserDaoJpa() {
		super(UserModel.class);
	}

	@Override
	public void registerLoginAttempt(UserModel userModel, LoginStatus loginStatus) {
		
		LoginAttempt loginAttempt = new LoginAttempt(userModel, loginStatus);
		
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

	public UserModel findByEmail(String email) {
		try {
			return em.createNamedQuery("Employee.findByEmail", ActemiumEmployee.class)
					.setParameter("emailAdress", email)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException(LanguageResource.getString("wrongUsernamePasswordCombination"));
		}
	}
}
