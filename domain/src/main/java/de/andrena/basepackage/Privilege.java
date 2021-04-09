package de.andrena.basepackage;

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
