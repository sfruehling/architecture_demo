package de.andrena.architecturedemo.web.privilege;

import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.web.common.VersionableAndIdentible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivilegeDto implements VersionableAndIdentible {
	private String id;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@NotEmpty
	private String description;
	private Long version;

	public static PrivilegeDto fromBusinessObject(Privilege privilege) {
		return new PrivilegeDto(privilege.getId(), privilege.getName(), privilege.getDescription(), privilege.getVersion());
	}

	public Privilege toBusinessObject() {
		return new Privilege(getId(), getName(), getDescription(), getVersion());
	}

	public static List<Privilege> toBusinessObjects(List<PrivilegeDto> privilegeDtos) {
		return privilegeDtos.stream().map(PrivilegeDto::toBusinessObject).collect(Collectors.toList());
	}
}
