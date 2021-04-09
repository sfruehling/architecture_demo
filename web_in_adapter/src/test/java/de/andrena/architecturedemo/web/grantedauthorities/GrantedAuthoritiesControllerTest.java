package de.andrena.architecturedemo.web.grantedauthorities;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.web.ControllerTestConfig;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.web.security.SecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GrantedAuthoritiesController.class)
@Import({ControllerTestConfig.class,
        SecurityTestConfig.class})
class GrantedAuthoritiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrantedAuthoritiesUsecase grantedAuthoritiesUsecase;

    @Test
    @WithMockUser(username = "a name")
    void returnsGrantedAuthorities() throws Exception {
        when(grantedAuthoritiesUsecase.getPrivilegesForUser("a name"))
                .thenReturn(Collections.singletonList(PrivilegeConstants.PRIVILEGE_READ));

        mockMvc.perform(MockMvcRequestBuilders.get(Properties.GRANTED_AUTHORITIES_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"" + PrivilegeConstants.PRIVILEGE_READ + "\"]"));
    }

    @Test
    void returnsUnauthorizedForUnauthenticatedUSer() throws Exception {
        mockMvc.perform(get(Properties.GRANTED_AUTHORITIES_PATH))
                .andExpect(status().isUnauthorized());
    }
}
