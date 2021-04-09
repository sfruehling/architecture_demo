package de.andrena.basepackage;

import java.util.List;

public interface RolePersistencePort {

	Role saveNewRole(Role role);

	List<Role> findAll();

	Role findById(String id);

	void deleteById(String id);

	Role update(Role role);

}
