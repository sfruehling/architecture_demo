package de.andrena.basepackage.e2e;

import de.andrena.basepackage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("SameParameterValue")
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithArchitectureDemoPostgresTestContainer
public class GrantedAuthoritiesE2ETest {

    private static final String USER_ID = "aUserId";

    @LocalServerPort
    int port;

    @Autowired
    private UserPersistencePort userPersistencePort;

    @Autowired
    private PrivilegePersistencePort privilegePersistencePort;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        createUserWithPreferrednameAndPrivilegeInDB(USER_ID, PrivilegeConstants.PRIVILEGE_READ);
    }

    @Test
    void shouldReturnAuthorities() {
        String url = "http://localhost:" + port + Properties.GRANTED_AUTHORITIES_PATH + "?principal="+ USER_ID;

        //Set the headers you need send
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        //Create a new HttpEntity
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        //Execute the method writing your HttpEntity to the request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String privileges = response.getBody();
        assertThat(privileges, is("[\"Privilege.Read\"]"));
    }

    private void createUserWithPreferrednameAndPrivilegeInDB(String preferred_name_in_token, String privilegeString) {
        User user = new User(1L,
                preferred_name_in_token,
                "userlogin",
                "user",
                Collections.emptyList());
        userPersistencePort.saveNewUser(user);

        Privilege privilegeObject = new Privilege("pid", privilegeString, "desc", 1L);
        privilegePersistencePort.saveNewPrivilege(privilegeObject);

        Role role = new Role("rid",
                "role",
                "desc",
                1L,
                Collections.singletonList(privilegeObject));
        user.setRoles(Collections.singletonList(role));
        userPersistencePort.updateUser(user);
    }
}
