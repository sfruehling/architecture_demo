package de.andrena.architecturedemo.domain.grantedAuthorities.in;

import de.andrena.architecturedemo.domain.grantedAuthorities.out.AuthoritiesPersistencePort;
import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GrantedAuthoritiesUsecaseTest {

    private AuthoritiesPersistencePort authoritiesPersistencePort = mock(AuthoritiesPersistencePort.class);

    private GrantedAuthoritiesUsecase grantedAuthoritiesUsecase =
            new GrantedAuthoritiesUsecaseImpl(authoritiesPersistencePort);

    @Test
    void returnsPrivilegesForUsername() {
        Privilege privilege = new Privilege("id", PrivilegeConstants.PRIVILEGE_READ, "desc", 1L);
        when(authoritiesPersistencePort.getPrivilegesForUserName("username")).thenReturn(Collections.singletonList(privilege));

        assertThat(grantedAuthoritiesUsecase.getPrivilegesForUser("username"), hasItems(PrivilegeConstants.PRIVILEGE_READ));
    }
}
