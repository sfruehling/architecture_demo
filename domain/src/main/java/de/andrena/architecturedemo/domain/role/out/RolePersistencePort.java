package de.andrena.architecturedemo.domain.role.out;

import de.andrena.architecturedemo.domain.role.Role;

import java.util.List;

public interface RolePersistencePort {

	Role saveNewRole(Role role);

	List<Role> findAll();

	Role findById(String id);

	void deleteById(String id);

	Role update(Role role);

}
