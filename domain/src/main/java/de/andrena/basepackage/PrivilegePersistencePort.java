package de.andrena.basepackage;

import java.util.List;

public interface PrivilegePersistencePort {
	List<Privilege> findAllPrivileges();

	Privilege findPrivilegeByDomainId(String domainId);

	Privilege saveNewPrivilege(Privilege privilege);

	Privilege updateNameAndDescription(Privilege updatedPrivilege);

	void deleteByDomainId(String domainId);
}
