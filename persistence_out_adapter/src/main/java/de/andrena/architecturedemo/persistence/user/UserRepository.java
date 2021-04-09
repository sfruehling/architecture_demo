package de.andrena.architecturedemo.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	List<UserEntity> findAllByOrderByDisplayNameAsc();

	Optional<UserEntity> findDistinctById(Long id);

	Optional<UserEntity> findDistinctByDomainId(String domainId);

	@Transactional
	void deleteByDomainId(String domainId);
}
