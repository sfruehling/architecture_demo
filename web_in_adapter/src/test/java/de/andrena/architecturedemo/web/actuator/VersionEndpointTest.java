package de.andrena.architecturedemo.web.actuator;

import de.andrena.architecturedemo.web.MockDomainConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@TestPropertySource(properties={
        "spring.flyway.enabled=false",
        "test.version.number=1.0"})
@Import(MockDomainConfig.class)
class VersionEndpointTest {

    @Autowired
    private MockMvc mockmvc;

    @Test
    void shouldReturnVersion() throws Exception {
        mockmvc.perform(get("/actuator/version"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.0"));
    }
}
