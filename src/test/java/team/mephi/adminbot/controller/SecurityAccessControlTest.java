package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.QuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for access control (403 Forbidden scenarios).
 * 
 * These tests verify that protected endpoints properly enforce role-based access control
 * and return 403 Forbidden when users lack required roles.
 * 
 * According to SecurityConfig:
 * - /users, /analytics, /dialogs, /broadcasts require ROLE_ADMIN
 * - /questions requires ROLE_LC_EXPERT or ROLE_ADMIN
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DialogRepository dialogRepository;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private MailingRepository mailingRepository;

    // ========== /users endpoint tests ==========

    @Test
    @WithMockUser(roles = {"USER"})
    void usersEndpoint_withUserRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"LC_EXPERT"})
    void usersEndpoint_withLcExpertRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withAdminRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    // ========== /analytics endpoint tests ==========

    @Test
    @WithMockUser(roles = {"USER"})
    void analyticsEndpoint_withUserRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/analytics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"LC_EXPERT"})
    void analyticsEndpoint_withLcExpertRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/analytics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void analyticsEndpoint_withAdminRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/analytics"))
                .andExpect(status().isOk());
    }

    // ========== /dialogs endpoint tests ==========

    @Test
    @WithMockUser(roles = {"USER"})
    void dialogsEndpoint_withUserRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/dialogs"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"LC_EXPERT"})
    void dialogsEndpoint_withLcExpertRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/dialogs"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void dialogsEndpoint_withAdminRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/dialogs"))
                .andExpect(status().isOk());
    }

    // ========== /broadcasts endpoint tests ==========

    @Test
    @WithMockUser(roles = {"USER"})
    void broadcastsEndpoint_withUserRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"LC_EXPERT"})
    void broadcastsEndpoint_withLcExpertRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void broadcastsEndpoint_withAdminRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isOk());
    }

    // ========== /questions endpoint tests ==========

    @Test
    @WithMockUser(roles = {"USER"})
    void questionsEndpoint_withUserRole_shouldReturn403() throws Exception {
        mockMvc.perform(get("/questions"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"LC_EXPERT"})
    void questionsEndpoint_withLcExpertRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void questionsEndpoint_withAdminRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk());
    }

    // ========== Unauthenticated access tests ==========

    @Test
    void usersEndpoint_withoutAuthentication_shouldReturn302Redirect() throws Exception {
        // Unauthenticated users should be redirected to login (OAuth2)
        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void analyticsEndpoint_withoutAuthentication_shouldReturn302Redirect() throws Exception {
        mockMvc.perform(get("/analytics"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void dialogsEndpoint_withoutAuthentication_shouldReturn302Redirect() throws Exception {
        mockMvc.perform(get("/dialogs"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void broadcastsEndpoint_withoutAuthentication_shouldReturn302Redirect() throws Exception {
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void questionsEndpoint_withoutAuthentication_shouldReturn302Redirect() throws Exception {
        mockMvc.perform(get("/questions"))
                .andExpect(status().is3xxRedirection());
    }
}
