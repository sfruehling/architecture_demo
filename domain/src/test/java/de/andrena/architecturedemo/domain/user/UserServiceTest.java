package de.andrena.architecturedemo.domain.user;

import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.domain.user.in.UserService;
import de.andrena.architecturedemo.domain.user.out.UserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    private static final String DOMAIN_ID = "ID";

    @MockBean
    private UserPersistencePort mockedUserPersistencePort;

    @SpyBean
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() throws Exception {
        Role role = new Role(null, null, null, 1L, null);
        user = new User(1L, null, "name", "name", Arrays.asList(role));
    }

    @Test
    void deleteUserShouldDeleteUser() {
        service.deleteUserByDomainId(DOMAIN_ID);

        verify(mockedUserPersistencePort, times(1)).deleteByDomainId(any(String.class));
        verifyNoMoreInteractions(mockedUserPersistencePort);
    }

    @Test
    void findUserById() {
        when(mockedUserPersistencePort.findUserByDomainId(Mockito.anyString())).thenReturn(user);

        User foundUser = service.findUserByDomainId(DOMAIN_ID);
        assertThat(foundUser.getDomainId(), is(user.getDomainId()));
    }

    @Test
    void updateUser() {
        when(mockedUserPersistencePort.updateUser(Mockito.any())).thenAnswer(args -> args.getArgument(0));

        User updatedUser = service.updateUser(DOMAIN_ID, user);

        assertThat(updatedUser.getDomainId(), is(DOMAIN_ID));
    }
}
