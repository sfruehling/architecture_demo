package de.andrena.basepackage;

import java.util.List;

public interface GrantedAuthoritiesUsecase {
    List<String> getPrivilegesForUser(String username);
}
