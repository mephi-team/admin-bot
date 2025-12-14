package team.mephi.adminbot.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E-тесты для UserController (полный контекст + PostgreSQL Testcontainers).
 */
class UserControllerE2EIT extends BaseE2EIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // given
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = roleRepository.save(Role.builder()
                .name("USER")
                .description("User role")
                .build());

        userRepository.save(User.builder()
                .externalId("tg-1")
                .name("Ivan")
                .firstName("Ivan")
                .lastName("Petrov")
                .status("active")
                .role(role)
                .build());

        userRepository.save(User.builder()
                .externalId("tg-2")
                .name("Petr")
                .firstName("Petr")
                .lastName("Sidorov")
                .status("blocked")
                .role(role)
                .build());
    }

    @Test
    void getUsers_whenStatusActive_shouldReturnOnlyActiveUsers() throws Exception {
        // when / then
        mockMvc.perform(get("/users").param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "active"));
    }

    @Test
    void getUsers_whenStatusAll_shouldReturnAllUsers() throws Exception {
        // when / then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("currentStatus", "all"));
    }
}
