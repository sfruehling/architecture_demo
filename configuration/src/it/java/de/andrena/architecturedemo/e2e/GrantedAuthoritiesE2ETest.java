package de.andrena.architecturedemo.e2e;

import de.andrena.architecturedemo.WithArchitectureDemoPostgresTestContainer;
import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.domain.privilege.out.PrivilegePersistencePort;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.domain.user.User;
import de.andrena.architecturedemo.domain.user.out.UserPersistencePort;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.web.security.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SuppressWarnings("SameParameterValue")
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithArchitectureDemoPostgresTestContainer
public class GrantedAuthoritiesE2ETest {

    private static final String TOKEN = "token";
    private static final String PREFERRED_NAME_IN_TOKEN = "aUserId";

    @LocalServerPort
    int port;

    @Autowired
    private UserPersistencePort userPersistencePort;

    @Autowired
    private PrivilegePersistencePort privilegePersistencePort;

    @MockBean
    private JwtDecoder jwtDecoder;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        createUserWithPreferrednameAndPrivilegeInDB(PREFERRED_NAME_IN_TOKEN, PrivilegeConstants.PRIVILEGE_READ);

        Jwt jwt = createJwtForName(PREFERRED_NAME_IN_TOKEN);
        when(jwtDecoder.decode(TOKEN)).thenReturn(jwt);
    }


    @Test
    void shouldReturnAuthorities() {
        String url = "http://localhost:" + port + Properties.GRANTED_AUTHORITIES_PATH;

        //Set the headers you need send
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + TOKEN);

        //Create a new HttpEntity
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        //Execute the method writing your HttpEntity to the request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String privileges = response.getBody();
        assertThat(privileges, is("[\"DemoPrivilege.Read\"]"));
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

    private Jwt createJwtForName(String name) {
        LocalDate issueddate = LocalDate.now();
        Instant issuedInstant = issueddate.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
        LocalDate expiresdate = issueddate.plusDays(1);
        Instant expiresInstant = expiresdate.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "headerValue");
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.PRINCIPAL_CLAIM_NAME, name);

        return new Jwt(TOKEN, issuedInstant, expiresInstant, headers, claims);
    }
}
