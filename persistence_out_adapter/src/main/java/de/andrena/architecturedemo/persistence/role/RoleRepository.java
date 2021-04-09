package de.andrena.architecturedemo.persistence.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	Optional<RoleEntity> findDistinctByDomainId(String domainId);

	@Transactional
	void deleteByDomainId(String domainId);

	List<RoleEntity> findAllByOrderByNameAsc();

}
