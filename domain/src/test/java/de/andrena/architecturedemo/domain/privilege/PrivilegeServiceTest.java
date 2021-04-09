package de.andrena.architecturedemo.domain.privilege;

import de.andrena.architecturedemo.domain.privilege.in.PrivilegeService;
import de.andrena.architecturedemo.domain.privilege.out.PrivilegePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PrivilegeServiceTest {

    private static final long VERSION = 1L;
    private static final String DOMAIN_ID = "ID";

    @MockBean
    private PrivilegePersistencePort mockedPrivilegePersistencePort;

    @SpyBean
    private PrivilegeService service;

    private Privilege privilege;

    @BeforeEach
    void setUp() {
        privilege = new Privilege(null, "name", "test", 10L);
    }

    @Test
    void saveNewPrivilegeShouldSaveANewPrivilege() {
        when(mockedPrivilegePersistencePort.saveNewPrivilege(any(Privilege.class))).thenReturn(new Privilege(null, null, null, null));
        service.saveNewPrivilege(new Privilege(null, "a.b.c", "test", null));

        verify(mockedPrivilegePersistencePort, times(1)).saveNewPrivilege(any(Privilege.class));
        verifyNoMoreInteractions(mockedPrivilegePersistencePort);
    }

    @Test
    void updatePrivilegeShouldReturnUpdatedPrivilege() {
        privilege.setVersion(VERSION);
        when(mockedPrivilegePersistencePort.updateNameAndDescription(any(Privilege.class))).thenReturn(privilege);
        service.updatePrivilege(DOMAIN_ID, privilege);

        verify(mockedPrivilegePersistencePort, times(1)).updateNameAndDescription(privilege);
        verifyNoMoreInteractions(mockedPrivilegePersistencePort);
    }

    @Test
    void deletePrivilegeShouldDeletePrivilege() {

        service.deletePrivilegeByDomainId(DOMAIN_ID);

        verify(mockedPrivilegePersistencePort, times(1)).deleteByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedPrivilegePersistencePort);
    }
}
