package de.andrena.architecturedemo.persistence.user;

import de.andrena.architecturedemo.persistence.cleanup.PostgresCleanUpExtension;
import de.andrena.architecturedemo.persistence.privilege.PrivilegeEntity;
import de.andrena.architecturedemo.persistence.privilege.PrivilegeRepository;
import de.andrena.architecturedemo.persistence.role.RoleEntity;
import de.andrena.architecturedemo.persistence.role.RoleRepository;
import de.andrena.architecturedemo.persistence.WithArchitectureDemoPostgresTestContainer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({SpringExtension.class, PostgresCleanUpExtension.class})
@SpringBootTest
@WithArchitectureDemoPostgresTestContainer
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    String id1;
    String id2;
    private UserEntity user1 = new UserEntity();
    private UserEntity user2 = new UserEntity();

    private RoleEntity role1 = new RoleEntity();
    private RoleEntity role2 = new RoleEntity();

    private PrivilegeEntity priv1 = new PrivilegeEntity();
    private PrivilegeEntity priv2 = new PrivilegeEntity();

    @BeforeEach
    void setUp() throws Exception {
        user1.setName("Nemo");
        user1.setDisplayName("Nemo");
        user2.setName("Jane Doe");
        user2.setDisplayName("Jane Doe");

        user1.setDomainId("NeMo");
        user2.setDomainId("JaDo");

        role1.setName("Eine Rolle");
        role2.setName("Noch eine Rolle");

        priv1.setName("Schlafen");
        priv2.setName("Saufen");

        role1.setPrivileges(Collections.singletonList(privilegeRepository.save(priv1)));
        role2.setPrivileges(Arrays.asList(privilegeRepository.save(priv1), privilegeRepository.save(priv2)));

        user1.setRoles(Collections.singletonList(roleRepository.save(role1)));
        user2.setRoles(Arrays.asList(roleRepository.save(role1), roleRepository.save(role2)));

        id1 = user1.getDomainId();
        id2 = user2.getDomainId();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void findDistinctById_findsUserWithMatchingId() {
        Optional<UserEntity> possibleUser = userRepository.findDistinctByDomainId(id1);
        if (possibleUser.isPresent()) {
            UserEntity foundUser = possibleUser.get();
            assertThat(foundUser.getName().equals(user1.getName()), Matchers.is(true));
        } else {
            fail();
        }
    }

    @Test
    void findAllOrderByDisplayNameDesc_sortedByName() {
        List<UserEntity> users = userRepository.findAllByOrderByDisplayNameAsc();
        assertThat(users.size(), is(2));
        assertThat(users.get(0).getDisplayName(), is("Jane Doe"));
        assertThat(users.get(1).getDisplayName(), is("Nemo"));
    }

}
