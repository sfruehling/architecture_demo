package de.andrena.architecturedemo.domain.role;

import de.andrena.architecturedemo.domain.privilege.Privilege;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Role {
	private String id;
	private String name;
	private String description;
	private Long version;
	private List<Privilege> privileges;
}
