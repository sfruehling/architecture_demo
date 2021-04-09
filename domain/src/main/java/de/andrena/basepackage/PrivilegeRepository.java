package de.andrena.basepackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {

	Optional<PrivilegeEntity> findDistinctByDomainId(String domainId);

	@Transactional
	void deleteByDomainId(String domainId);

	List<PrivilegeEntity> findAllByOrderByNameAsc();
}
