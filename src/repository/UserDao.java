package repository;

import javax.persistence.EntityNotFoundException;

import domain.LoginStatus;
import domain.User;

public interface UserDao extends GenericDao<User> {

	public User findByUsername(String username) throws EntityNotFoundException;

	public void registerLoginAttempt(User user, LoginStatus loginStatus);

}
