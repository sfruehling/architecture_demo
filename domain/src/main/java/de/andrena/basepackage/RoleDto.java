package de.andrena.basepackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class RoleDto implements VersionableAndIdentible {
	private String id;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@NotEmpty
	private String description;
	private Long version;

	public RoleDto() {
	}

	public static RoleDto fromBusinessObject(Role role) {
		return new RoleDto(role.getId(), role.getName(), role.getDescription(), role.getVersion());
	}

	public Role toBusinessObject() {
		return new Role(getId(), getName(), getDescription(), getVersion(), new ArrayList<>());
	}

}
