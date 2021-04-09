package de.andrena.architecturedemo.web.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.domain.role.in.EditRoleUsecase;
import de.andrena.architecturedemo.domain.role.in.FindRoleUsecase;
import de.andrena.architecturedemo.web.ControllerTestConfig;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.web.common.VersionableAndIdentible;
import de.andrena.architecturedemo.web.security.SecurityTestConfig;
import de.andrena.architecturedemo.web.validation.ValidationProviderImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RoleController.class)
@Import({SecurityTestConfig.class,
        ControllerTestConfig.class})
class RoleControllerTest {

    private static final String PATH = Properties.ROLE_PATH;
    private static final String DOMAIN_ID = "ID";
    private static final Long ID = 1L;
    private static final String NAME = "dummy name";
    private static final String DESCRIPTION = "Das ist die Beschreibung zu der Rolle";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FindRoleUsecase findRoleUsecase;

    @MockBean
    private EditRoleUsecase editRoleUsecase;

    @Autowired
    private ObjectMapper jsonMapper;
    @SpyBean
    private ValidationProviderImpl validationProvider;

    private Role role;
    private List<Privilege> privileges;
    private RoleWithPrivilegesDto roleDto;


    @BeforeEach
    void setUp() {
        Privilege privilege = new Privilege(DOMAIN_ID, NAME, DESCRIPTION, 0L);
        privileges = Arrays.asList(privilege, new Privilege("otherId", "otherName", DESCRIPTION, 0L));

        role = new Role(DOMAIN_ID, NAME, DESCRIPTION, 0L, privileges);
        roleDto = RoleWithPrivilegesDto.fromBusinessObject(role);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldReturnAllContactsWhenGETIsPerformedOnPATH() throws Exception {
        when(findRoleUsecase.findAllRoles()).thenReturn(Collections.singletonList(role));

        mockMvc.perform(getAllRoles())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(NAME)));

        verify(findRoleUsecase, times(1)).findAllRoles();
        verifyNoMoreInteractions(findRoleUsecase);
    }

    @Test
    void getAllRolesShouldReturnUnauthorizedIfUnAuthenticated() throws Exception {
        mockMvc.perform(getAllRoles()).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder getAllRoles() {
        return get(PATH)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldReturnRoleWhenGETIsPerformedOnPATHPlusID() throws Exception {
        when(findRoleUsecase.findRoleById(Mockito.anyString())).thenReturn(role);
        mockMvc.perform(getRoleById()) //
                .andExpect(status().isOk())//
                .andExpect(jsonPath("$.name", is(NAME)));
        verify(findRoleUsecase, times(1)).findRoleById(DOMAIN_ID);
        verifyNoMoreInteractions(findRoleUsecase);
    }

    @Test
    void getRoleByIdShouldReturnUnauthorizedIfUnauthenticated() throws Exception {
        mockMvc.perform(getRoleById())
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder getRoleById() {
        return get(PATH + "/" + DOMAIN_ID) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldCreateARoleWhenPOSTIsPerformedOnPATH() throws Exception {
        Role responseRole = new Role(DOMAIN_ID, NAME, DESCRIPTION, 0L, privileges);
        when(editRoleUsecase.saveNewRole(any(Role.class))).thenReturn(responseRole);
        Role role = new Role(null, NAME, DESCRIPTION, null, privileges);
        mockMvc.perform(postRole(role)) //
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.name", is(NAME)));
        verify(editRoleUsecase, times(1)).saveNewRole(any(Role.class));
        verify(validationProvider).checkForValidationErrors(any(BindingResult.class));
        verify(validationProvider).checkThatNoIdOrVersionAreGiven(any(VersionableAndIdentible.class));
        verifyNoMoreInteractions(editRoleUsecase);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldReturnBadRequestWhenInvalidPOSTIsPerformed() throws Exception {
        Role role = new Role(DOMAIN_ID, "", DESCRIPTION, 0L, privileges);

        mockMvc.perform(postRole(role))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(findRoleUsecase);
    }

    @Test
    void postRoleShouldReturnUnAuthorizedIfUnAuthenticated() throws Exception {
        Role role = new Role(DOMAIN_ID, NAME, DESCRIPTION, 0L, privileges);
        mockMvc.perform(postRole(role))
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder postRole(Role role) throws IOException {
        return post(PATH)
                .content(asJson(role))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldUpdateARoleWhenPUTIsPerformedOnPathPlusID() throws Exception {
        when(editRoleUsecase.update(any(Role.class))).thenReturn(role);

        mockMvc.perform(putRole(roleDto.getId(), asJson(this.roleDto))) //
                .andExpect(jsonPath("$.name", is(NAME)));
        verify(editRoleUsecase, times(1)).update(any(Role.class));
        verify(validationProvider).checkForValidationErrors(any(BindingResult.class));
        verify(validationProvider).checkIdConsistency(Mockito.anyString(), Mockito.anyString());
        verify(validationProvider).checkThatVersionIsProvided(any(VersionableAndIdentible.class));
        verifyNoMoreInteractions(editRoleUsecase);
    }

    @Test
    void putRoleShouldReturnUnauthorizedIfUnAuthenticated() throws Exception {
        mockMvc.perform(putRole(roleDto.getId(), asJson(this.roleDto))) //
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder putRole(String id, String content) {
        return put(PATH + "/" + id) //
                .content(content) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldReturnBadRequestWhenInvalidPUTIsPerformed() throws Exception {
        Role role = new Role(DOMAIN_ID, "", DESCRIPTION, 0L, privileges);

        mockMvc.perform(putRole("" + ID, asJson(role)))//
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(findRoleUsecase);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void deleteRoleShouldDeleteRoleWhenDELETEIsPerforemd() throws Exception {

        mockMvc.perform(deleteRole("" + ID, this.role))//
                .andExpect(status().isOk());

        verify(editRoleUsecase, times(1)).deleteRoleById(Mockito.anyString());
        verifyNoMoreInteractions(editRoleUsecase);
    }

    @Test
    void deleteRoleShouldReturnUnauthorizedifUnauthenticated() throws Exception {
        mockMvc.perform(deleteRole("" + ID, role))//
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder deleteRole(String id, Role role) throws IOException {
        return delete(PATH + "/" + id) //
                .content(asJson(role))//
                .contentType(MediaType.APPLICATION_JSON);
    }

    private String asJson(Object javaObject) throws IOException {
        return jsonMapper.writeValueAsString(javaObject);
    }

}
