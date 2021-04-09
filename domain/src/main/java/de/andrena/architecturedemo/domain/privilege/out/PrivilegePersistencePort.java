package de.andrena.architecturedemo.domain.privilege.out;

import de.andrena.architecturedemo.domain.privilege.Privilege;

import java.util.List;

public interface PrivilegePersistencePort {
	List<Privilege> findAllPrivileges();

	Privilege findPrivilegeByDomainId(String domainId);

	Privilege saveNewPrivilege(Privilege privilege);

	Privilege updateNameAndDescription(Privilege updatedPrivilege);

	void deleteByDomainId(String domainId);
}
