package de.andrena.architecturedemo.web;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import de.andrena.architecturedemo.domain.privilege.in.PrivilegeService;
import de.andrena.architecturedemo.domain.role.in.RoleService;
import de.andrena.architecturedemo.domain.user.in.EditUserUsecase;
import de.andrena.architecturedemo.domain.user.in.FindUserUsecase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class MockDomainConfig {
    @MockBean
    GrantedAuthoritiesUsecase grantedAuthoritiesUsecase;

    @MockBean
    PrivilegeService privilegeService;

    @MockBean
    RoleService roleService;

    @MockBean
    FindUserUsecase findUserUsecase;

    @MockBean
    EditUserUsecase editUserUsecase;
}
