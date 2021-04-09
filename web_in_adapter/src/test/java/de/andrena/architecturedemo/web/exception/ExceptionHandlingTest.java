package de.andrena.architecturedemo.web.exception;

import de.andrena.architecturedemo.domain.exception.DatabaseException;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.domain.role.in.RoleService;
import de.andrena.architecturedemo.web.ControllerTestConfig;
import de.andrena.architecturedemo.web.MockDomainConfig;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.web.security.SecurityTestConfig;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ValidationException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({MockDomainConfig.class,
        SecurityTestConfig.class,
        ControllerTestConfig.class})
class ExceptionHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleValidationException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new ValidationException("amessage"));

        mockMvc.perform(MockMvcRequestBuilders.get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("amessage")))
                .andExpect(jsonPath("$.logref", is("ValidationException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleNoVersionProvidedException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new NoVersionProvidedException());

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Please provide a version when updating an entity.")))
                .andExpect(jsonPath("$.logref", is("NoVersionProvidedException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleNoIdAllowedException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new NoIdAllowedException());

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("No id allowed when creating a new enitity.")))
                .andExpect(jsonPath("$.logref", is("NoIdAllowedException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleNoVersionAllowedException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new NoVersionAllowedException());

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("No version allowed when creating a new entity.")))
                .andExpect(jsonPath("$.logref", is("NoVersionAllowedException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleDatabaseException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new DatabaseException("test"));

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("test")))
                .andExpect(jsonPath("$.logref", is("DatabaseException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleHttpMessageNotReadableException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new HttpMessageNotReadableException("test", (HttpInputMessage) null));

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("test")))
                .andExpect(jsonPath("$.logref", is("HttpMessageNotReadableException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleConstraintViolationException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new ConstraintViolationException("test", null, "constraint"));

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Entity cannot be deleted because it is still referred to.")))
                .andExpect(jsonPath("$.logref", is("ConstraintViolationException")));
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldHandleIdInconsistentException() throws Exception {
        when(roleService.findAllRoles()).thenThrow(new IdInconsistentException());

        mockMvc.perform(get(Properties.ROLE_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Id in path and Body are not equal.")))
                .andExpect(jsonPath("$.logref", is("IdInconsistentException")));
    }
}
