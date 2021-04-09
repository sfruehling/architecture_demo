package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PrivilegePersistenceAdapter implements PrivilegePersistencePort {

	private final PrivilegeRepository privilegeRepository;

	@Autowired
	public PrivilegePersistenceAdapter(PrivilegeRepository privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}

    @Override
	public List<Privilege> findAllPrivileges() {
		return PrivilegeEntity.toBusinessObjects(privilegeRepository.findAllByOrderByNameAsc());
	}

	@Override
	public Privilege findPrivilegeByDomainId(String domainId) {
		return privilegeRepository.findDistinctByDomainId(domainId) //
				.map(PrivilegeEntity::toBusinessObject) //
				.orElseThrow(() -> new EntityNotFoundException(domainId));
	}

	@Override
	public Privilege saveNewPrivilege(Privilege privilege) {
		Optional<PrivilegeEntity> privilegeWithSameId = privilegeRepository.findDistinctByDomainId(privilege.getId());
		if (privilegeWithSameId.isPresent()) {
			throw new IdAlreadyInUseException("Privilege Id: " + privilege.getId());
		}

		return privilegeRepository.save(PrivilegeEntity.fromBusinessObject(privilege)).toBusinessObject();
	}

	@Override
	public Privilege updateNameAndDescription(Privilege updatedPrivilege) {

		PrivilegeEntity entity = privilegeRepository.findDistinctByDomainId(updatedPrivilege.getId())
				.orElseThrow(() -> new EntityNotFoundException(updatedPrivilege.getId()));

		entity.setName(updatedPrivilege.getName());
		entity.setDescription(updatedPrivilege.getDescription());

		return privilegeRepository.save(entity).toBusinessObject();
	}

	@Override
	public void deleteByDomainId(String domainId) {
		privilegeRepository.deleteByDomainId(domainId);
	}

}
