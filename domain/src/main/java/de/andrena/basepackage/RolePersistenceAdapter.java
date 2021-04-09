package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RolePersistenceAdapter implements RolePersistencePort {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public RolePersistenceAdapter(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public Role saveNewRole(Role role) {
        RoleEntity roleEntity = RoleEntity.fromBusinessObject(role);
        roleEntity.setDomainId(role.getName().replace(" ", ""));

        roleEntity.setPrivileges(getPrivilegesEntities(role.getPrivileges()));

        Optional<RoleEntity> roleWithSameId = roleRepository.findDistinctByDomainId(roleEntity.getDomainId());
        if (roleWithSameId.isPresent()) {
            throw new IdAlreadyInUseException(roleEntity.getDomainId());
        }

        return roleRepository.save(roleEntity).toBusinessObject();
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAllByOrderByNameAsc()//
                .stream().map(RoleEntity::toBusinessObject).collect(Collectors.toList());
    }

    @Override
    public Role findById(String id) {
        return findEntityByDomainId(id).toBusinessObject();
    }

    @Override
    public void deleteById(String id) {
        roleRepository.deleteByDomainId(id);

    }

    @Override
    public Role update(Role role) {
        RoleEntity roleEntity = findEntityByDomainId(role.getId());
        roleEntity.updateWithRole(role);
        roleEntity.setPrivileges(getPrivilegesEntities(role.getPrivileges()));
        return roleRepository.save(roleEntity).toBusinessObject();
    }

    public List<PrivilegeEntity> getPrivilegesEntities(List<Privilege> privileges) {
        return privileges.stream() //
                .map(privilege -> privilegeRepository.findDistinctByDomainId(privilege.getId())) //
                .filter(Optional::isPresent) //
                .map(Optional::get) //
                .collect(Collectors.toList());
    }

    private RoleEntity findEntityByDomainId(String domainId) {
        return roleRepository.findDistinctByDomainId(domainId) //
                .orElseThrow(() -> new EntityNotFoundException(domainId));
    }
}
