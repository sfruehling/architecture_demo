package de.andrena.basepackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserDto implements VersionableAndIdentible {
	private String id;
	@NotNull
	@NotEmpty
	private String loginId;
	@NotNull
	@NotEmpty
	private String displayName;
	private Long version;

	public UserDto() {
	}

	public static UserDto fromBusinessObject(User user) {
		return new UserDto(user.getDomainId(), user.getLoginId(), user.getDisplayName(), user.getVersion());
	}

}
