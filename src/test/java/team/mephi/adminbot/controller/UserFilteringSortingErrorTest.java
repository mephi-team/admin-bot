package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for error scenarios in user listing and filtering.
 * 
 * These tests verify that the /users endpoint handles invalid parameters gracefully
 * without returning 500 errors, implementing proper fallback behavior.
 * 
 * Covered scenarios:
 * - Invalid status parameter values
 * - Invalid status with search query
 * - Null/empty query parameters
 * - Edge cases in parameter combinations
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserFilteringSortingErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    // ========== Invalid status parameter tests ==========

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withInvalidStatus_shouldReturn200AndFallbackToAll() throws Exception {
        // Given: invalid status parameter
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // When/Then: should return 200 and fall back to showing all users
        mockMvc.perform(get("/users").param("status", "invalid_status"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "invalid_status"));

        // Verify fallback to findAll() was called
        verify(userRepository).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withNumericStatus_shouldReturn200AndFallbackToAll() throws Exception {
        // Given: numeric value instead of status string
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        // When/Then: should handle gracefully
        mockMvc.perform(get("/users").param("status", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"));

        verify(userRepository).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withSpecialCharactersInStatus_shouldReturn200() throws Exception {
        // Given: special characters in status
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        // When/Then: should handle gracefully
        mockMvc.perform(get("/users").param("status", "@#$%"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));

        verify(userRepository).findAll();
    }

    // ========== Invalid status with search query tests ==========

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withInvalidStatusAndSearchQuery_shouldFallbackToSearchAll() throws Exception {
        // Given: invalid status with search query
        String searchQuery = "john";
        List<User> searchResults = List.of(new User(), new User());
        when(userRepository.searchAll(searchQuery)).thenReturn(searchResults);

        // When/Then: should fall back to searchAll when status is invalid
        mockMvc.perform(get("/users")
                        .param("status", "invalid_status")
                        .param("q", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("search", searchQuery));

        // Verify fallback to searchAll() was called
        verify(userRepository).searchAll(searchQuery);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withMalformedStatusAndSearchQuery_shouldNotThrow500() throws Exception {
        // Given: malformed status parameter with search
        String searchQuery = "test";
        when(userRepository.searchAll(searchQuery)).thenReturn(Collections.emptyList());

        // When/Then: should return 200, not 500
        mockMvc.perform(get("/users")
                        .param("status", "INVALID_UPPERCASE")
                        .param("q", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));

        verify(userRepository).searchAll(searchQuery);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withCaseSensitiveInvalidStatusAndSearch_shouldFallbackGracefully() throws Exception {
        // Given: case-sensitive invalid status (lowercase instead of uppercase enum)
        String searchQuery = "user";
        when(userRepository.searchAll(searchQuery)).thenReturn(List.of(new User()));

        // When/Then: should fall back to searchAll for invalid enum value
        mockMvc.perform(get("/users")
                        .param("status", "pending")  // Not a valid UserStatus
                        .param("q", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));

        verify(userRepository).searchAll(searchQuery);
    }

    // ========== Empty and null query parameter tests ==========

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withEmptyQueryString_shouldNotSearch() throws Exception {
        // Given: empty query string
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        // When/Then: should not trigger search, just filter by status
        mockMvc.perform(get("/users")
                        .param("status", "all")
                        .param("q", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("search", ""));

        verify(userRepository).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withWhitespaceOnlyQuery_shouldNotSearch() throws Exception {
        // Given: whitespace-only query
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        // When/Then: should treat as no search
        mockMvc.perform(get("/users")
                        .param("status", "all")
                        .param("q", "   "))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("search", "   "));

        verify(userRepository).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withNullQuery_shouldDefaultToNoSearch() throws Exception {
        // Given: no query parameter
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        // When/Then: should work without search
        mockMvc.perform(get("/users").param("status", "all"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("search", ""));

        verify(userRepository).findAll();
    }

    // ========== Valid status values for comparison ==========

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withValidActiveStatus_shouldWork() throws Exception {
        // Given: valid status "active"
        List<User> activeUsers = List.of(new User());
        when(userRepository.findByStatus(UserStatus.ACTIVE)).thenReturn(activeUsers);

        // When/Then: should work correctly
        mockMvc.perform(get("/users").param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("currentStatus", "active"));

        verify(userRepository).findByStatus(UserStatus.ACTIVE);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withValidBlockedStatus_shouldWork() throws Exception {
        // Given: valid status "blocked"
        List<User> blockedUsers = List.of(new User());
        when(userRepository.findByStatus(UserStatus.BLOCKED)).thenReturn(blockedUsers);

        // When/Then: should work correctly
        mockMvc.perform(get("/users").param("status", "blocked"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("currentStatus", "blocked"));

        verify(userRepository).findByStatus(UserStatus.BLOCKED);
    }

    // ========== Search with valid status tests ==========

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withValidStatusAndSearchQuery_shouldSearchByStatus() throws Exception {
        // Given: valid status with search query
        String searchQuery = "test";
        List<User> searchResults = List.of(new User());
        when(userRepository.searchByStatus(any(UserStatus.class), anyString())).thenReturn(searchResults);

        // When/Then: should search by status
        mockMvc.perform(get("/users")
                        .param("status", "active")
                        .param("q", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"));

        verify(userRepository).searchByStatus(UserStatus.ACTIVE, searchQuery);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withAllStatusAndSearchQuery_shouldSearchAll() throws Exception {
        // Given: "all" status with search query
        String searchQuery = "user";
        List<User> searchResults = List.of(new User());
        when(userRepository.searchAll(searchQuery)).thenReturn(searchResults);

        // When/Then: should search all users
        mockMvc.perform(get("/users")
                        .param("status", "all")
                        .param("q", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));

        verify(userRepository).searchAll(searchQuery);
    }

    // ========== Edge cases ==========

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withVeryLongInvalidStatus_shouldNotCauseError() throws Exception {
        // Given: very long invalid status string (realistic edge case)
        String longStatus = "a".repeat(200);
        when(userRepository.findAll()).thenReturn(List.of(new User()));

        // When/Then: should handle gracefully
        mockMvc.perform(get("/users").param("status", longStatus))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));

        verify(userRepository).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void usersEndpoint_withSqlInjectionAttemptInStatus_shouldBeSafe() throws Exception {
        // Given: SQL injection attempt in status
        String maliciousStatus = "active'; DROP TABLE users; --";
        when(userRepository.findAll()).thenReturn(List.of(new User()));

        // When/Then: should handle safely (no SQL injection)
        mockMvc.perform(get("/users").param("status", maliciousStatus))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));

        verify(userRepository).findAll();
    }
}
