package de.andrena.basepackage;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "privileges")
@SequenceGenerator(name = "privilege_id_generator", sequenceName = "privilege_sequence", allocationSize = 1)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeEntity {

	@Id
	@GeneratedValue(generator = "privilege_id_generator")
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

	public static PrivilegeEntity fromBusinessObject(Privilege privilege) {
		PrivilegeEntity entity = new PrivilegeEntity();
		entity.setDomainId(privilege.getId());
		entity.setName(privilege.getName());
		entity.setVersion(privilege.getVersion());
		entity.setDescription(privilege.getDescription());

		return entity;
	}

	public Privilege toBusinessObject() {
		return new Privilege(domainId, name, description, version);
	}

	public static List<Privilege> toBusinessObjects(List<PrivilegeEntity> privilegeEntities) {
		return privilegeEntities.stream().map(PrivilegeEntity::toBusinessObject).collect(Collectors.toList());
	}

}
