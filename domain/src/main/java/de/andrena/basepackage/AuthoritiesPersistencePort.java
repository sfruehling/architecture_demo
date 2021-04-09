package de.andrena.basepackage;

import java.util.List;

public interface AuthoritiesPersistencePort {
    List<Privilege> getPrivilegesForUserName(String username);
}
