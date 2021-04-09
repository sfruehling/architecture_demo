package de.andrena.basepackage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements FindUserUsecase, EditUserUsecase {

	@Autowired
	private UserPersistencePort userPersistencePort;

	@Override
	public User saveNewUser(User user) {
		user.setDomainId(user.getLoginId());
		return userPersistencePort.saveNewUser(user);
	}

	@Override
	public User updateUser(String domainId, User updatedUser) {
		updatedUser.setDomainId(domainId);
		return userPersistencePort.updateUser(updatedUser);
	}

	@Override
	public void deleteUserByDomainId(String domainId) {
		userPersistencePort.deleteByDomainId(domainId);
	}

	@Override
	public List<User> findAllUsers() {
		return userPersistencePort.findAllByOrderByDisplayNameAsc();
	}

	@Override
	public User findUserByDomainId(String id) {
		return userPersistencePort.findUserByDomainId(id);
	}

}
