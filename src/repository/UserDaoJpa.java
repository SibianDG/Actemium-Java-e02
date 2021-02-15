package repository;

import domain.LoginStatus;
import domain.User;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {

	public UserDaoJpa() {
		super(User.class);
	}

	@Override
	public void registerLoginAttempt(String username, LoginStatus loginStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
