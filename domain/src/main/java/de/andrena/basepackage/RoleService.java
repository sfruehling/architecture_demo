package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements FindRoleUsecase, EditRoleUsecase {

	private final RolePersistencePort rolePersistencePort;

	@Autowired
	public RoleService(RolePersistencePort rolePersistencePort){
		this.rolePersistencePort = rolePersistencePort;
	}

	@Override
	public Role saveNewRole(Role role) {
		return rolePersistencePort.saveNewRole(role);
	}

	@Override
	public Role update(Role role) {
		return rolePersistencePort.update(role);
	}

	@Override
	public void deleteRoleById(String id) {
		rolePersistencePort.deleteById(id);
	}

	@Override
	public List<Role> findAllRoles() {
		return rolePersistencePort.findAll();
	}

	@Override
	public Role findRoleById(String id) {
		return rolePersistencePort.findById(id);
	}

}
