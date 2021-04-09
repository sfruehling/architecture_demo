package de.andrena.architecturedemo.e2e;

import de.andrena.architecturedemo.PostgresCleanUpExtension;
import de.andrena.architecturedemo.WithArchitectureDemoPostgresTestContainer;
import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.domain.user.User;
import de.andrena.architecturedemo.domain.user.in.UserService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith({
        SpringExtension.class,
        PostgresCleanUpExtension.class
})
@SpringBootTest
@WithArchitectureDemoPostgresTestContainer
class UserServiceIT {

    @Autowired
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        List<Role> roles = new ArrayList<>();
        List<Privilege> privileges = new ArrayList<>();
        Privilege privilege = new Privilege("PrivilegeDomainId", "testPrivilegeName", "testPrivilegeDescription", 0L);
        privileges.add(privilege);
        Role role1 = new Role(null, "roleToDelete", "testDescription", 0L, privileges);
        Role role2 = new Role(null, "roleToUpdate", "testDescription", 0L, privileges);
        roles.add(role1);
        roles.add(role2);
        user = new User(null, null, "TestName", "TestDisplayName", roles);
        user = userService.saveNewUser(user);
    }

    @Test
    void saveNewUser() {
        user = userService.findUserByDomainId("TestName");
        assertThat(user.getDomainId(), is("TestName"));
        assertThat(user.getRoles().size(), is(2));
    }

    @Test
    void updateUser() {
        List<Privilege> privileges = new ArrayList<>();
        Privilege privilege = new Privilege("PrivilegeDomainId", "testPrivilegeName", "testPrivilegeDescription", 0L);
        privileges.add(privilege);
        user.setDisplayName("newDisplayName");
        user.setLoginId("newName");
        List<Role> roles = new ArrayList<>();
        Role role2 = new Role("roleToUpdate", "updatedRoleName", "updatedDescription", 0L, privileges);
        Role role3 = new Role(null, "testRoleName", "testDescription", 0L, privileges);
        roles.add(role2);
        roles.add(role3);
        user.setRoles(roles);

        User updatedUser = userService.updateUser(user.getDomainId(), user);

        assertThat(updatedUser.getDomainId(), is("TestName"));
        assertThat(updatedUser.getDisplayName(), is("newDisplayName"));
        assertThat(updatedUser.getLoginId(), is("newName"));
        assertThat(updatedUser.getRoles().size(), is(2));
        MatcherAssert.assertThat(updatedUser.getRoles().get(0).getId(), is("roleToUpdate"));
        MatcherAssert.assertThat(updatedUser.getRoles().get(1).getId(), is("testRoleName"));
    }

}
