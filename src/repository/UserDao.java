package repository;

import javax.persistence.EntityNotFoundException;

import domain.UserModel;
import domain.enums.LoginStatus;

public interface UserDao extends GenericDao<UserModel> {

	public UserModel findByUsername(String username) throws EntityNotFoundException;

	public void registerLoginAttempt(UserModel userModel, LoginStatus loginStatus);

	UserModel findByEmail(String email) throws EntityNotFoundException;
}
