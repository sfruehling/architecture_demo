package de.andrena.architecturedemo.persistence.privilege;

import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.persistence.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PrivilegePersistenceAdapterTest {
    private static final String DOMAIN_ID = "ID";

    private final PrivilegeRepository mockedRepository = mock(PrivilegeRepository.class);

    private final PrivilegePersistenceAdapter privilegePersistenceAdapterImpl = new PrivilegePersistenceAdapter(mockedRepository);

    private PrivilegeEntity privilegeEntity;
    private List<PrivilegeEntity> privileges;

    @BeforeEach
    void setUp() {
        privilegeEntity = PrivilegeEntity.builder().description("test").name("name").build();
        privileges = Arrays.asList(privilegeEntity, new PrivilegeEntity(), new PrivilegeEntity());
    }

    @Test
    void findAllPrivilegesShouldReturnAListOfAllSavedPrivileges() {
        when(mockedRepository.findAll()).thenReturn(privileges);
        privilegePersistenceAdapterImpl.findAllPrivileges();

        verify(mockedRepository, times(1)).findAllByOrderByNameAsc();
        verifyNoMoreInteractions(mockedRepository);
    }

    @Test
    void saveNewPrivilegeShouldThrowExceptionIfDomainIdIsInUse() {
        when(mockedRepository.findDistinctByDomainId(Mockito.anyString())).thenReturn(Optional.of(privilegeEntity));

        assertThatThrownBy(() -> privilegePersistenceAdapterImpl.saveNewPrivilege(new Privilege("id", "name", "description", 0L))).isInstanceOf(IdAlreadyInUseException.class);
    }

    @Test
    void findPrivilegeShouldReturnPrivilege() {
        when(mockedRepository.findDistinctByDomainId(any(String.class))).thenReturn(Optional.of(privilegeEntity));
        privilegePersistenceAdapterImpl.findPrivilegeByDomainId(DOMAIN_ID);

        verify(mockedRepository, times(1)).findDistinctByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedRepository);
    }

    @Test
    void findPrivilegeShouldThrowExceptionWhenNoPrivilegeIsFound() {
        assertThrows(EntityNotFoundException.class, () -> privilegePersistenceAdapterImpl.findPrivilegeByDomainId(DOMAIN_ID));
    }

    @Test
    void updateNameAndDescriptionShouldUpdateNameAndDescription() {

        when(mockedRepository.findDistinctByDomainId(Mockito.anyString())).thenReturn(Optional.of(privilegeEntity));
        ArgumentCaptor<PrivilegeEntity> argumentPrivilegeCaptor = ArgumentCaptor.forClass(PrivilegeEntity.class);
        when(mockedRepository.save(argumentPrivilegeCaptor.capture())).thenReturn(privilegeEntity);

        Privilege privilegeWithNewNameAndDescription = new Privilege(DOMAIN_ID, "newName", "newDescription", 10L);

        privilegePersistenceAdapterImpl.updateNameAndDescription(privilegeWithNewNameAndDescription);

        PrivilegeEntity updatedPrivilegeEntity = argumentPrivilegeCaptor.getValue();
        assertThat(updatedPrivilegeEntity.getName(), equalTo("newName"));
        assertThat(updatedPrivilegeEntity.getDescription(), equalTo("newDescription"));
    }
}
