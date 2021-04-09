package de.andrena.architecturedemo.persistence.role;

import de.andrena.architecturedemo.persistence.privilege.IdAlreadyInUseException;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.persistence.exception.EntityNotFoundException;
import de.andrena.architecturedemo.persistence.privilege.PrivilegeEntity;
import de.andrena.architecturedemo.persistence.privilege.PrivilegeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RolePersistenceAdapterTest {

    private static final String DOMAIN_ID = "domaiId";
    private static final String NAME = "dummy name";
    private static final String DESCRIPTION = "Das ist die Beschreibung zu der Rolle";

    RoleRepository mockedRoleRepository = mock(RoleRepository.class);
    PrivilegeRepository mockedPrivilegeRepository = mock(PrivilegeRepository.class);

    RolePersistenceAdapter rolePersistenceAdapterImpl = new RolePersistenceAdapter(mockedRoleRepository, mockedPrivilegeRepository);

    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        PrivilegeEntity privilege = new PrivilegeEntity();
        List<PrivilegeEntity> privileges = Arrays.asList(privilege, new PrivilegeEntity(), new PrivilegeEntity());

        roleEntity = new RoleEntity();
        roleEntity.setName(NAME);
        roleEntity.setDescription(DESCRIPTION);
        roleEntity.setPrivileges(privileges);
    }

    @Test
    void findRoleShouldThrowExceptionWhenNoRoleIsFound() {
        assertThrows(EntityNotFoundException.class, () -> rolePersistenceAdapterImpl.findById(DOMAIN_ID));
    }

    @Test
    void deleteRoleShouldDeleteRole() {
        rolePersistenceAdapterImpl.deleteById(DOMAIN_ID);

        verify(mockedRoleRepository, times(1)).deleteByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedRoleRepository);
    }

    @Test
    void saveNewRoleShouldSaveARole() {
        when(mockedRoleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
        when(mockedRoleRepository.findDistinctByDomainId(Mockito.anyString())).thenReturn(Optional.empty());
        when(mockedPrivilegeRepository.findDistinctByDomainId(any(String.class))).thenReturn(Optional.empty());
        rolePersistenceAdapterImpl.saveNewRole(roleEntity.toBusinessObject());

        verify(mockedRoleRepository, times(1)).save(any(RoleEntity.class));
    }

    @Test
    void saveNewRoleShouldThrowExceptionIfDomainIdIsInUse() {
        when(mockedRoleRepository.findDistinctByDomainId(Mockito.anyString())).thenReturn(Optional.of(roleEntity));

        assertThatThrownBy(() -> rolePersistenceAdapterImpl.saveNewRole(new Role(DOMAIN_ID, NAME, DESCRIPTION, 0L, new ArrayList<>()))).isInstanceOf(IdAlreadyInUseException.class);
    }

    @Test
    void updateRoleShouldReturnUpdatedRole() {
        roleEntity.setVersion(10L);
        roleEntity.setDomainId(DOMAIN_ID);

        when(mockedRoleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
        when(mockedRoleRepository.findDistinctByDomainId(any(String.class))).thenReturn(Optional.of(roleEntity));

        rolePersistenceAdapterImpl.update(roleEntity.toBusinessObject());

        verify(mockedRoleRepository, times(1)).save(any(RoleEntity.class));
        verify(mockedRoleRepository, times(1)).findDistinctByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedRoleRepository);
    }
}
