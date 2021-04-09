package de.andrena.basepackage;

import java.util.List;

public interface UserPersistencePort {

	List<User> findAllByOrderByDisplayNameAsc();

	User findUserByDomainId(String domainId);

	User saveNewUser(User user);

	User updateUser(User updatedUserParameter);

	void deleteByDomainId(String domainId);
}
