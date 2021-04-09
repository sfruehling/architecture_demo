package de.andrena.architecturedemo.domain.role;

import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.role.in.RoleService;
import de.andrena.architecturedemo.domain.role.out.RolePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RoleServiceTest {
    private static final String DOMAIN_ID = "ID";

    @MockBean
    private RolePersistencePort mockedRolePersistencePort;

    @SpyBean
    private RoleService service;

    private Role role;

    @BeforeEach
    void setUp() throws Exception {
        role = new Role(DOMAIN_ID, "name", "desc", 0L, Collections.singletonList(new Privilege("Id", "Name", "desc", 0L)));
    }

    @Test
    void findAllRolesShouldReturnAListOfAllSavedRoles() {
        when(mockedRolePersistencePort.findAll()).thenReturn(Collections.singletonList(role));
        service.findAllRoles();

        verify(mockedRolePersistencePort, times(1)).findAll();
        verifyNoMoreInteractions(mockedRolePersistencePort);

    }

    @Test
    void findRoleShouldReturnRole() {
        when(mockedRolePersistencePort.findById(any(String.class))).thenReturn(role);
        service.findRoleById(DOMAIN_ID);

        verify(mockedRolePersistencePort, times(1)).findById(any(String.class));
        verifyNoMoreInteractions(mockedRolePersistencePort);
    }

}
