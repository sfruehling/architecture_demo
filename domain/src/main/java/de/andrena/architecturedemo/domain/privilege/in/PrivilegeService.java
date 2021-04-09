package de.andrena.architecturedemo.domain.privilege.in;

import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.privilege.out.PrivilegePersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService implements FindPrivilegesUsecase, EditPrivilegesUsecase {

	private final PrivilegePersistencePort privilegePersistencePort;

	@Autowired
	public PrivilegeService(PrivilegePersistencePort privilegePersistencePort) {
		this.privilegePersistencePort = privilegePersistencePort;
	}

	public List<Privilege> findAllPrivileges() {
		return privilegePersistencePort.findAllPrivileges();
	}

	@Override
	public Privilege findPrivilegeByDomainId(String domainId) {
		return privilegePersistencePort.findPrivilegeByDomainId(domainId);
	}

	@Override
	public Privilege saveNewPrivilege(Privilege privilege) {
		privilege.setId(privilege.getName().replace('.', '-').replace(' ', '_'));
		return privilegePersistencePort.saveNewPrivilege(privilege);
	}

	@Override
	public Privilege updatePrivilege(String domainId, Privilege updatedPrivilege) {
		return privilegePersistencePort.updateNameAndDescription(updatedPrivilege);
	}

	@Override
	public void deletePrivilegeByDomainId(String domainId) {
		privilegePersistencePort.deleteByDomainId(domainId);
	}

}
