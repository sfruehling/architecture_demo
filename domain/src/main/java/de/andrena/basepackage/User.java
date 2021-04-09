package de.andrena.basepackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class User {

	private Long version;

	private String domainId;

	private String loginId;

	private String displayName;

	private List<Role> roles = new ArrayList<>();
}