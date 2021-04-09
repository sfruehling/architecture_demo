package de.andrena.architecturedemo.web.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.domain.user.User;
import de.andrena.architecturedemo.domain.user.in.EditUserUsecase;
import de.andrena.architecturedemo.domain.user.in.FindUserUsecase;
import de.andrena.architecturedemo.web.ControllerTestConfig;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.web.security.SecurityTestConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
@WebMvcTest(value = UserController.class)
@Import({SecurityTestConfig.class,
        ControllerTestConfig.class})
class UserControllerTest {

    private static final String PATH = Properties.USER_PATH;
    private static final String ID = "ID";
    private static final String LOGIN_ID = "admin name";
    private static final String DISPLAY_NAME = "admin display name";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FindUserUsecase findUserUsecase;
    @MockBean
    private EditUserUsecase editUserUsecase;
    @Autowired
    private ObjectMapper jsonMapper;

    private User user;
    private User responseUser;

    @BeforeEach
    void setUp() {
        Role dummyRole = new Role(null, null, null, 1L, null);

        List<Role> roles = Arrays.asList(dummyRole, dummyRole, dummyRole);
        user = new User(1L, null, LOGIN_ID, DISPLAY_NAME, roles);
        responseUser = new User(1L, ID, LOGIN_ID, DISPLAY_NAME, roles);
    }

    @Test
    @WithMockUser(authorities = PrivilegeConstants.PRIVILEGE_READ)
    void shouldReturnAllContactsWhenGETIsPerformedOnPATH() throws Exception {
        when(findUserUsecase.findAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(getAllUser())//
                .andExpect(status().isOk())//
                .andExpect(jsonPath("$", hasSize(1)))//
                .andExpect(jsonPath("$[0].loginId", is(LOGIN_ID)))//
                .andExpect(jsonPath("$[0].displayName", is(DISPLAY_NAME)));

        verify(findUserUsecase, times(1)).findAllUsers();
        verifyNoMoreInteractions(findUserUsecase);
    }

    @Test
    void getUserShouldReturnUnAuthorizedIfUnAuthenticated() throws Exception {
        mockMvc.perform(getAllUser()).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder getAllUser() {
        return get(PATH) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = PrivilegeConstants.PRIVILEGE_READ)
    void shouldReturnUserWhenGETIsPerformedOnPATHPlusID() throws Exception {
        when(findUserUsecase.findUserByDomainId(Mockito.anyString())).thenReturn(responseUser);
        mockMvc.perform(getUser()) //
                .andExpect(status().isOk())//
                .andExpect(jsonPath("$.loginId", is(LOGIN_ID)))//
                .andExpect(jsonPath("$.displayName", is(DISPLAY_NAME)));
        verify(findUserUsecase, times(1)).findUserByDomainId(ID);
        verifyNoMoreInteractions(findUserUsecase);
    }

    @Test
    void getUserShouldReturnUnauthorizedIfUnauthenticated() throws Exception {
        mockMvc.perform(getUser()).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder getUser() {
        return get(PATH + "/" + ID) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = PrivilegeConstants.PRIVILEGE_WRITE)
    void shouldCreateAUserWhenPOSTIsPerformedOnPATH() throws Exception {
        user.setDomainId(null);
        user.setVersion(null);
        when(editUserUsecase.saveNewUser(any(User.class))).thenReturn(responseUser);

        mockMvc.perform(postUser()) //
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.loginId", is(LOGIN_ID))) //
                .andExpect(jsonPath("$.displayName", is(DISPLAY_NAME)));
        verify(editUserUsecase, times(1)).saveNewUser(any(User.class));
        verifyNoMoreInteractions(editUserUsecase);
    }

    @Test
    void postUserShouldReturnUnauthorizedifUnauthenticated() throws Exception {
        mockMvc.perform(postUser()).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder postUser() throws IOException {
        return post(PATH) //
                .content(asJson(user))//
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = PrivilegeConstants.PRIVILEGE_WRITE)
    void shouldUpdateAUserWhenPUTIsPerformedOnPathPlusID() throws Exception {
        when(editUserUsecase.updateUser(Mockito.anyString(), any(User.class))).thenReturn(responseUser);

        mockMvc.perform(putUser()) //
                .andExpect(jsonPath("$.loginId", is(LOGIN_ID))) //
                .andExpect(jsonPath("$.displayName", is(DISPLAY_NAME)));
        verify(editUserUsecase, times(1)).updateUser(Mockito.anyString(), any(User.class));
        verifyNoMoreInteractions(editUserUsecase);
    }

    @Test
    void putUserShouldReturnUnauthorizedIfUnauthenticated() throws Exception {
        mockMvc.perform(putUser()).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder putUser() throws IOException {
        return put(PATH + "/" + ID) //
                .content(asJson(user)) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = PrivilegeConstants.PRIVILEGE_WRITE)
    void shouldDeleteUserWhenDELETEIsPerforemd() throws Exception {

        mockMvc.perform(deleteUser())//
                .andExpect(status().isOk());

        verify(editUserUsecase, times(1)).deleteUserByDomainId(Mockito.anyString());
        verifyNoMoreInteractions(editUserUsecase);
    }

    @Test
    void deleteUserShouldReturnUnauthorizedIfUnauthenticated() throws Exception {
        mockMvc.perform(deleteUser()).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder deleteUser() throws IOException {
        return delete(PATH + "/" + ID) //
                .content(asJson(user))//
                .contentType(MediaType.APPLICATION_JSON);
    }

    private String asJson(Object javaObject) throws IOException {
        return jsonMapper.writeValueAsString(javaObject);
    }
}
