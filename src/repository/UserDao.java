package repository;

import javax.persistence.EntityNotFoundException;

import domain.enums.LoginStatus;
import domain.UserModel;

public interface UserDao extends GenericDao<UserModel> {

	public UserModel findByUsername(String username) throws EntityNotFoundException;

	public void registerLoginAttempt(UserModel userModel, LoginStatus loginStatus);

}
