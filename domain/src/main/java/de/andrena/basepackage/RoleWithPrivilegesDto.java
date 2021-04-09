package de.andrena.basepackage;

import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
public class RoleWithPrivilegesDto extends RoleDto {
	private List<PrivilegeDto> privileges;

	public List<PrivilegeDto> getPrivileges() {
		return privileges;
	}

	public RoleWithPrivilegesDto(String id, String name, String description, Long version,
			List<PrivilegeDto> privileges) {
		super(id, name, description, version);
		this.privileges = privileges;
	}

	public RoleWithPrivilegesDto() {
	}

	public static RoleWithPrivilegesDto fromBusinessObject(Role role) {
		List<PrivilegeDto> privileges = role.getPrivileges().stream()
				.map(privilege -> PrivilegeDto.fromBusinessObject(privilege)).collect(Collectors.toList());
		return new RoleWithPrivilegesDto(role.getId(), role.getName(), role.getDescription(), role.getVersion(),
				privileges);
	}

	@Override
	public Role toBusinessObject() {
		return new Role(getId(), getName(), getDescription(), getVersion(),
				PrivilegeDto.toBusinessObjects(getPrivileges()));
	}
}
