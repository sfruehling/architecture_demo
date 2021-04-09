package de.andrena.basepackage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserWithRolesDto extends UserDto {
	private List<RoleDto> roles;

	public UserWithRolesDto() {
		super();
		roles = new ArrayList<>();
	}

	public UserWithRolesDto(String id, String loginId, String displayName, Long version, List<RoleDto> roles) {
		super(id, loginId, displayName, version);
		this.roles = roles;
	}

	public List<RoleDto> getRoles() {
		return roles;
	}

	public static UserWithRolesDto fromBusinessObject(User user) {
		List<RoleDto> roleDtos = user.getRoles().stream().map(RoleDto::fromBusinessObject).collect(Collectors.toList());
		return new UserWithRolesDto(user.getDomainId(), user.getLoginId(), user.getDisplayName(), user.getVersion(),
				roleDtos);
	}

	public User toBusinessObject() {
		return new User(getVersion(), getId(), getLoginId(), getDisplayName(),
				roles.stream().map(RoleDto::toBusinessObject).collect(Collectors.toList()));
	}
}
