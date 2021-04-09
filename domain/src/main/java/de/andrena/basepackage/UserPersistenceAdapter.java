package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserPersistenceAdapter implements UserPersistencePort, AuthoritiesPersistencePort {

	private final UserRepository userRepository;
	private final RolePersistencePort rolePersistencePort;
	private final RoleRepository roleRepository;

	@Autowired
	public UserPersistenceAdapter(UserRepository userRepository,
								  RolePersistencePort rolePersistencePort,
								  RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.rolePersistencePort = rolePersistencePort;
		this.roleRepository = roleRepository;
	}

	@Override
	public List<User> findAllByOrderByDisplayNameAsc() {
		return userRepository.findAllByOrderByDisplayNameAsc().stream() //
				.map(UserEntity::toBusinessObject) //
				.collect(Collectors.toList());
	}

	@Override
	public User findUserByDomainId(String domainId) {
		return userRepository.findDistinctByDomainId(domainId) //
				.map(UserEntity::toBusinessObject) //
				.orElseThrow(() -> new EntityNotFoundException(domainId));
	}

	@Override
	public User saveNewUser(User user) {
		Optional<UserEntity> foundUser = userRepository.findDistinctByDomainId(user.getDomainId());
		if (foundUser.isPresent())
			throw new IdAlreadyInUseException(user.getDomainId());

		UserEntity newUserEntity = UserEntity.fromBusinessObject(user);
		newUserEntity.setRoles(findOrCreateRoles(user));
		return userRepository.save(newUserEntity).toBusinessObject();
	}

	@Override
	public User updateUser(User updatedUser) {
		Optional<UserEntity> findDistinctByDomainId = userRepository.findDistinctByDomainId(updatedUser.getDomainId());

		UserEntity userToSave = findDistinctByDomainId //
				.map(user -> user.update(updatedUser)) //
				.orElseThrow(() -> new EntityNotFoundException(updatedUser.getDomainId()));

		userToSave.setRoles(findOrCreateRoles(updatedUser));
		return userRepository.save(userToSave).toBusinessObject();
	}

	private List<RoleEntity> findOrCreateRoles(User user) {
		return user.getRoles().stream() //
				.map(role -> {
					Optional<RoleEntity> foundRole = roleRepository.findDistinctByDomainId(role.getId());
					return foundRole.orElseGet(() -> saveNewRoleAndFind(role));
				}).collect(Collectors.toList());
	}

	private RoleEntity saveNewRoleAndFind(Role role) {
		Role savedRole = rolePersistencePort.saveNewRole(role);
		return roleRepository.findDistinctByDomainId(savedRole.getId()).get();
	}

	@Override
	public void deleteByDomainId(String domainId) {
		userRepository.deleteByDomainId(domainId);
	}

	@Override
	public List<Privilege> getPrivilegesForUserName(String username) {
		return userRepository.findDistinctByDomainId(username) //
				.map(this::getPrivilegesForUser) //
				.map(privileges -> //
				privileges.stream() //
						.map(PrivilegeEntity::toBusinessObject)//
						.collect(Collectors.toList())) //
				.orElse(new ArrayList<>());
	}

	private List<PrivilegeEntity> getPrivilegesForUser(UserEntity user) {
		return user.getRoles().stream() //
				.flatMap(r -> r.getPrivileges().stream()) //
				.distinct() //
				.collect(Collectors.toList());
	}
}
