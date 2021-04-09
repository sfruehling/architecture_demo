package de.andrena.basepackage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
@SequenceGenerator(name = "role_id_generator", sequenceName = "role_sequence", allocationSize = 1)
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_generator")
	private Long id;

	@Version
	private Long version;

	@Column(unique = true)
	private String domainId;

	@NotNull
	@NotEmpty
	@Column(unique = true)
	private String name;

	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privileges_id"))
	private List<PrivilegeEntity> privileges = new ArrayList<>();

	public static RoleEntity fromBusinessObject(Role role) {
		RoleEntity entity = new RoleEntity();
		entity.setDomainId(role.getId());
		entity.setName(role.getName());
		entity.setVersion(role.getVersion());
		entity.setDescription(role.getDescription());

		return entity;
	}

	public Role toBusinessObject() {
		return new Role(domainId, name, description, version, PrivilegeEntity.toBusinessObjects(privileges));
	}

	public void updateWithRole(Role role) {
		setDescription(role.getDescription());
		setName(role.getName());
		setVersion(role.getVersion());
	}
}
