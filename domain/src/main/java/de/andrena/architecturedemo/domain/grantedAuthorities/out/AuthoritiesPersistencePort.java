package de.andrena.architecturedemo.domain.grantedAuthorities.out;

import de.andrena.architecturedemo.domain.privilege.Privilege;

import java.util.List;

public interface AuthoritiesPersistencePort {
    List<Privilege> getPrivilegesForUserName(String username);
}
