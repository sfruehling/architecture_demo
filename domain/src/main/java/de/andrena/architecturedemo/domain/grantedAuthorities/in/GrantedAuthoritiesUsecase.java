package de.andrena.architecturedemo.domain.grantedAuthorities.in;

import java.util.List;

public interface GrantedAuthoritiesUsecase {
    List<String> getPrivilegesForUser(String username);
}
