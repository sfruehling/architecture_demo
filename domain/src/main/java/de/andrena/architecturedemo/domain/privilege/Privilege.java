package de.andrena.architecturedemo.domain.privilege;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Privilege {
	private String id;
	private String name;
	private String description;
	private Long version;
}
