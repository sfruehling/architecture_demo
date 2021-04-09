package de.andrena.architecturedemo.persistence.user;

import de.andrena.architecturedemo.domain.role.out.RolePersistencePort;
import de.andrena.architecturedemo.persistence.exception.EntityNotFoundException;
import de.andrena.architecturedemo.persistence.role.RoleEntity;
import de.andrena.architecturedemo.persistence.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserPersistenceAdapterPortImplTest {
    private static final String DOMAIN_ID = "ID";
    private static final long VERSION = 1L;

    UserRepository mockedUserRepository = mock(UserRepository.class);
    RoleRepository mockedRoleRepository = mock(RoleRepository.class);
    RolePersistencePort mockedRolePersistencePort = mock(RolePersistencePort.class);

    UserPersistenceAdapter userPersistenceAdapterImpl = new UserPersistenceAdapter(mockedUserRepository,
            mockedRolePersistencePort,
            mockedRoleRepository);

    private List<UserEntity> users;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        users = Collections.singletonList(new UserEntity());
        user = new UserEntity();
    }

    @Test
    void findAllUsersShouldReturnAListOfAllSavedUsers() {
        when(mockedUserRepository.findAllByOrderByDisplayNameAsc()).thenReturn(users);
        userPersistenceAdapterImpl.findAllByOrderByDisplayNameAsc();

        verify(mockedUserRepository, times(1)).findAllByOrderByDisplayNameAsc();
        verifyNoMoreInteractions(mockedUserRepository);
    }

    @Test
    void findUserShouldReturnUser() {
        when(mockedUserRepository.findDistinctByDomainId(any(String.class))).thenReturn(Optional.of(user));
        userPersistenceAdapterImpl.findUserByDomainId(DOMAIN_ID);

        verify(mockedUserRepository, times(1)).findDistinctByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedUserRepository);
    }

    @Test
    void findUserShouldThrowExceptionWhenNoUserIsFound() {
        assertThrows(EntityNotFoundException.class, () -> userPersistenceAdapterImpl.findUserByDomainId(DOMAIN_ID));
    }

    @Test
    void saveNewUserShouldSaveAUser() {
        when(mockedUserRepository.save(any(UserEntity.class))).thenReturn(user);
        when(mockedUserRepository.findDistinctByDomainId(Mockito.any())).thenReturn(Optional.empty());
        userPersistenceAdapterImpl.saveNewUser(user.toBusinessObject());

        verify(mockedUserRepository, times(1)).save(any(UserEntity.class));
        verify(mockedUserRepository, times(1)).findDistinctByDomainId(Mockito.any());
        verifyNoMoreInteractions(mockedUserRepository);
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        user.setVersion(VERSION);
        user.setDomainId(DOMAIN_ID);
        when(mockedUserRepository.save(any(UserEntity.class))).thenReturn(user);
        when(mockedUserRepository.findDistinctByDomainId(any(String.class))).thenReturn(Optional.of(user));
        when(mockedRoleRepository.findDistinctByDomainId(any(String.class))).thenReturn(Optional.of(new RoleEntity()));
        userPersistenceAdapterImpl.updateUser(user.toBusinessObject());

        verify(mockedUserRepository, times(1)).save(any(UserEntity.class));
        verify(mockedUserRepository, times(1)).findDistinctByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedUserRepository);
    }

    @Test
    void deleteUserShouldDeleteUser() {
        userPersistenceAdapterImpl.deleteByDomainId(DOMAIN_ID);

        verify(mockedUserRepository, times(1)).deleteByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedUserRepository);
    }
}
