package repository;

import javax.persistence.EntityNotFoundException;

import domain.LoginStatus;
import domain.User;

public interface UserDao extends GenericDao<User> {

	public User findByUsername(String username) throws EntityNotFoundException;

	public void registerLoginAttempt(String username, LoginStatus loginStatus);

	public void attemptLogin(String username, String password);
}
