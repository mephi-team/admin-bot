package team.mephi.adminbot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для UserController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class UserControllerApiTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getUsers_whenStatusAll_shouldReturnUsersViewWithAllUsers() throws Exception {
        // given
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // when / then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "all"));

        verify(userRepository).findAll();
        verify(userRepository, never()).findByStatus(anyString());
    }

    @Test
    void getUsers_whenStatusActive_shouldReturnUsersViewWithActiveUsers() throws Exception {
        // given
        List<User> activeUsers = List.of(new User());
        when(userRepository.findByStatus("active")).thenReturn(activeUsers);

        // when / then
        mockMvc.perform(get("/users").param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "active"));

        verify(userRepository).findByStatus("active");
        verify(userRepository, never()).findAll();
    }

    @Test
    void getUsers_whenStatusBlocked_shouldReturnUsersViewWithBlockedUsers() throws Exception {
        // given
        List<User> blockedUsers = List.of(new User());
        when(userRepository.findByStatus("blocked")).thenReturn(blockedUsers);

        // when / then
        mockMvc.perform(get("/users").param("status", "blocked"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "blocked"));

        verify(userRepository).findByStatus("blocked");
        verify(userRepository, never()).findAll();
    }

    @Test
    void getUsers_whenStatusUnknown_shouldFallbackToAllUsers() throws Exception {
        // given
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        // when / then
        mockMvc.perform(get("/users").param("status", "unknown"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "unknown"));

        verify(userRepository).findAll();
        verify(userRepository, never()).findByStatus(anyString());
    }
}
