package de.andrena.architecturedemo.web.actuator;

import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.web.MockDomainConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "spring.flyway.enabled=false"})
@Import(MockDomainConfig.class)
class HealthEndpointTest {

    @Autowired
    private MockMvc mockmvc;

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ, PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldReturnHealthStatus() throws Exception {
        mockmvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"UP\"}"));
    }
}
