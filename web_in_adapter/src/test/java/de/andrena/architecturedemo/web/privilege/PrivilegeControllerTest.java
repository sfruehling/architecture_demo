package de.andrena.architecturedemo.web.privilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.domain.privilege.in.EditPrivilegesUsecase;
import de.andrena.architecturedemo.domain.privilege.in.FindPrivilegesUsecase;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PrivilegeController.class)
@Import({SecurityTestConfig.class,
        ControllerTestConfig.class})
class PrivilegeControllerTest {

    private static final String PATH = Properties.PRIVILEGE_PATH;
    private static final String DOMAIN_ID = "ID";
    private static final String NAME = "dummy name";
    private static final String DESCRIPTION = "Das ist die super tolle Beschreibung";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FindPrivilegesUsecase findPrivilegesUsecase;
    @MockBean
    private EditPrivilegesUsecase editPrivilegesUsecase;
    @Autowired
    private ObjectMapper jsonMapper;

    private Privilege privilege;
    private Privilege responePrivilege;

    @BeforeEach
    void setUp() {
        privilege = new Privilege(null, null, null, null);
        privilege.setName(NAME);
        privilege.setDescription(DESCRIPTION);
        responePrivilege = new Privilege(DOMAIN_ID, NAME, DESCRIPTION, 0L);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldReturnAllContactsWhenGETIsPerformedOnPATH() throws Exception {
        when(findPrivilegesUsecase.findAllPrivileges()).thenReturn(Collections.singletonList(privilege));

        mockMvc.perform(getAllPrivileges())//
                .andExpect(status().isOk())//
                .andExpect(jsonPath("$", hasSize(1)))//
                .andExpect(jsonPath("$[0].name", is(NAME)));

        verify(findPrivilegesUsecase, times(1)).findAllPrivileges();
        verifyNoMoreInteractions(findPrivilegesUsecase);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void getAllPrivileges_ResponseHasSomeWeirdHeaders() throws Exception {
        when(findPrivilegesUsecase.findAllPrivileges()).thenReturn(Collections.singletonList(privilege));

        mockMvc.perform(getAllPrivileges())//
                .andExpect(header().string("Http status", "OK"))
                .andExpect(header().string("Location", "/privileges"))
                .andExpect(headerCount(10));

        verify(findPrivilegesUsecase, times(1)).findAllPrivileges();
        verifyNoMoreInteractions(findPrivilegesUsecase);
    }

    private ResultMatcher headerCount(int headerCount) {
        return mvcResult -> assertThat(mvcResult.getResponse().getHeaderNames(), hasSize(headerCount));
    }

    @Test
    void getPrivilegeShouldReturnUnauthorizedIfUnAuthenticated() throws Exception {
        mockMvc.perform(getAllPrivileges())
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder getAllPrivileges() {
        return get(PATH) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_READ})
    void shouldReturnPrivilegeWhenGETIsPerformedOnPATHPlusID() throws Exception {
        when(findPrivilegesUsecase.findPrivilegeByDomainId(Mockito.anyString())).thenReturn(responePrivilege);
        mockMvc.perform(getPrivilegeById(DOMAIN_ID)) //
                .andExpect(status().isOk())//
                .andExpect(jsonPath("$.name", is(NAME)));
        verify(findPrivilegesUsecase, times(1)).findPrivilegeByDomainId(DOMAIN_ID);
        verifyNoMoreInteractions(findPrivilegesUsecase);
    }

    @Test
    void getPrivilegeByIdShouldReturnUnauthorizedIfUnauthenticated() throws Exception {
        mockMvc.perform(getPrivilegeById(DOMAIN_ID)).andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder getPrivilegeById(String domainId) {
        return get(PATH + "/" + domainId) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldCreateAPrivilegeWhenPOSTIsPerformedOnPATH() throws Exception {
        when(editPrivilegesUsecase.saveNewPrivilege(any(Privilege.class))).thenReturn(responePrivilege);
        mockMvc.perform(postPrivilege(PATH, asJson(PrivilegeDto.fromBusinessObject(privilege)))) //
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.name", is(NAME)));
        verify(editPrivilegesUsecase, times(1)).saveNewPrivilege(any(Privilege.class));
        verifyNoMoreInteractions(editPrivilegesUsecase);
    }

    @Test
    void postPrivilegeShouldReturnUnauthorizedIfUnauthenticated() throws Exception {
        mockMvc.perform(postPrivilege(PATH, asJson(PrivilegeDto.fromBusinessObject(privilege)))) //
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder postPrivilege(String path, String content) {
        return post(path) //
                .content(content)//
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldUpdateAPrivilegeWhenPUTIsPerformedOnPathPlusID() throws Exception {
        privilege.setVersion(0L);
        when(editPrivilegesUsecase.updatePrivilege(Mockito.anyString(), any(Privilege.class))).thenReturn(responePrivilege);

        mockMvc.perform(putPrivilege(DOMAIN_ID, asJson(privilege))) //
                .andExpect(jsonPath("$.name", is(NAME)));
        verify(editPrivilegesUsecase, times(1)).updatePrivilege(Mockito.anyString(), any(Privilege.class));
        verifyNoMoreInteractions(editPrivilegesUsecase);
    }

    @Test
    void putPrivilegeShouldReturnUnauthorizedIfUnautenticated() throws Exception {
        mockMvc.perform(putPrivilege(DOMAIN_ID, asJson(privilege))) //
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder putPrivilege(String domainId, String content) {
        return put(PATH + "/" + domainId) //
                .content(content) //
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(authorities = {PrivilegeConstants.PRIVILEGE_WRITE})
    void shouldDeletePrivilegeWhenDELETEIsPerforemd() throws Exception {
        mockMvc.perform(deletePrivilege(DOMAIN_ID, asJson(privilege)))
                .andExpect(status().isOk());

        verify(editPrivilegesUsecase, times(1)).deletePrivilegeByDomainId(Mockito.anyString());
        verifyNoMoreInteractions(editPrivilegesUsecase);
    }

    @Test
    void deletePrivilegeShouldReturnUnauthenticatedIfUnauthorized() throws Exception {
        mockMvc.perform(deletePrivilege(DOMAIN_ID, asJson(privilege)))
                .andExpect(status().isUnauthorized());
    }

    @NotNull
    private MockHttpServletRequestBuilder deletePrivilege(String domainId, String content) {
        return delete(PATH + "/" + domainId) //
                .content(content)//
                .contentType(MediaType.APPLICATION_JSON);
    }

    private String asJson(Object javaObject) throws IOException {
        return jsonMapper.writeValueAsString(javaObject);
    }

}
