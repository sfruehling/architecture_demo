package de.andrena.architecturedemo.web.role;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.domain.role.in.RoleService;
import de.andrena.architecturedemo.web.ControllerTestConfig;
import de.andrena.architecturedemo.web.security.SecurityTestConfig;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RoleController.class)
@Import({SecurityTestConfig.class,
        ControllerTestConfig.class})
class JwtTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService mockedRoleService;

    @Autowired
    private GrantedAuthoritiesUsecase grantedAuthoritiesUsecase;


    private final String REAL_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnZmFaaGlZRWRidFpMZHVQRHFWVnZGNHREZGQ5TTNlYmotajZIRGlCcTIwIn0.eyJqdGkiOiJiM2NkNmY1OS1jN2JjLTQyNDAtODY2ZC1jYTZhNzFkNWFhNTYiLCJleHAiOjE2MTQ4MDE3MzEsIm5iZiI6MCwiaWF0IjoxNjE0NzY1OTAwLCJpc3MiOiJodHRwczovL2F1dGguYXBwcy5hbmRyZW5hLmRlL2F1dGgvcmVhbG1zL2FuZHJlbmEiLCJhdWQiOiJldmVudGNvY2twaXQiLCJzdWIiOiIxYzZlYTA3YS1hYjJhLTQwYWQtODEyMi1lYWFiZjg1MTE0MTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJldmVudGNvY2twaXQiLCJub25jZSI6Ik93NUtxV1lXT0dOTlJ4WTNzRXZwc0lPWlRlclZPVUtnSmNTaXRGTnBueVNNUiIsImF1dGhfdGltZSI6MTYxNDc2NTczMSwic2Vzc2lvbl9zdGF0ZSI6IjlkMzM0ZTA5LTExNTMtNDBjMy1hNGE4LWQ3YmYzOWQyOGU5MCIsImFjciI6IjAiLCJhbGxvd2VkLW9yaWdpbnMiOltdLCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJzY29wZSI6Im9wZW5pZCBkZWZhdWx0IiwibmFtZSI6IlNlYmFzdGlhbiBGcnVlaGxpbmciLCJoYW5kbGUiOiJTZWJhc3RpYW4uRnJ1ZWhsaW5nIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibTAyNjQiLCJnaXZlbl9uYW1lIjoiU2ViYXN0aWFuIiwiZmFtaWx5X25hbWUiOiJGcnVlaGxpbmciLCJlbWFpbCI6InNlYmFzdGlhbi5mcnVlaGxpbmdAYW5kcmVuYS5kZSJ9.Q_ytDMY-nbmBauYhlvC6ogu8JuKgGv5v5bgsR6fzfgvM1XtS2z8-3KBYTtfXrWmnUEEX1bjCOtyygPRLSYynpujIGIaMK9dK4vlUFB_GKThWZxXFDN6dWM9duusNL_YePgfrVGARPlMBcHrQrNoyv0OMAt2zePEptUEx9opFShHH058hyoffl2esKJSH-dcH_jHm-0lbWKEPAFkl5YVKf5k-PMtTb6t7bx-ipousk-ljgjn4iCaNemWioYPrdNMGa-xobKSreDfqQClPpSbm-aXkGXQ95B90SbEKoDTf_IPYcTUbDLjDl6n_JpWEC1W0BEHvm4S3LZs4GQtglgJ1HQ";

    @Language("RegExp")
    private String REGEX = "^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$";

    @BeforeEach
    void setUp() throws Exception {
        String userMNumberOfTokenClaim = any();
        when(grantedAuthoritiesUsecase.getPrivilegesForUser(userMNumberOfTokenClaim)).thenReturn(
                Collections.singletonList(PrivilegeConstants.PRIVILEGE_READ)
        );
    }

    @Test
    @Disabled("ignored since the token expires. This Test is only for JWT-testing and requires to update REAL_TOKEN.")
    void shouldReadToken() throws Exception {
        Role role = new Role("domainId", "name", "desc", 0L, null);
        when(mockedRoleService.findAllRoles()).thenReturn(Collections.singletonList(role));

        mockMvc.perform(getAllRoles())
                .andExpect(status().isOk());

        verify(mockedRoleService, times(1)).findAllRoles();
        verifyNoMoreInteractions(mockedRoleService);
    }

    private MockHttpServletRequestBuilder getAllRoles() {
        return MockMvcRequestBuilders.get(Properties.ROLE_PATH)
                .header(HttpHeaders.AUTHORIZATION, REAL_TOKEN)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void tokenMatchesReqExpOfDefaultBearerTokenResolver() {
        Pattern authorizationPattern = Pattern.compile(REGEX, 2);

        Matcher matcher = authorizationPattern.matcher(REAL_TOKEN);
        assertThat(matcher.matches(), is(true));
    }
}
