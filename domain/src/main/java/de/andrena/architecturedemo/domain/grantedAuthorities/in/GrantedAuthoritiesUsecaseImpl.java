package de.andrena.architecturedemo.domain.grantedAuthorities.in;

import de.andrena.architecturedemo.domain.grantedAuthorities.out.AuthoritiesPersistencePort;
import de.andrena.architecturedemo.domain.privilege.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrantedAuthoritiesUsecaseImpl implements GrantedAuthoritiesUsecase {
    private AuthoritiesPersistencePort authoritiesPersistencePort;

    @Autowired
    public GrantedAuthoritiesUsecaseImpl(AuthoritiesPersistencePort authoritiesPersistencePort) {
        this.authoritiesPersistencePort = authoritiesPersistencePort;
    }

    @Override
    public List<String> getPrivilegesForUser(String username) {
        return authoritiesPersistencePort.getPrivilegesForUserName(username).stream()
                .map(Privilege::getName)
                .collect(Collectors.toList());
    }
}
