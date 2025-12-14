package team.mephi.adminbot.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E-тесты для MailingController (полный контекст + PostgreSQL Testcontainers).
 *
 * Важно: Mailing имеет обязательные поля createdBy и status, поэтому при POST мы передаем createdBy.id и status.
 */
class MailingControllerE2EIT extends BaseE2EIT {

    @Autowired private MockMvc mockMvc;

    @Autowired private MailingRepository mailingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    private Long creatorId;

    @BeforeEach
    void setUp() {
        // given
        mailingRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = roleRepository.save(Role.builder().name("ADMIN").description("Admin").build());

        User creator = userRepository.save(User.builder()
                .externalId("tg-admin-1")
                .name("Admin")
                .firstName("Admin")
                .lastName("User")
                .status("active")
                .role(role)
                .build());

        creatorId = creator.getId();
    }

    @Test
    void getBroadcasts_shouldReturnBroadcastsViewWithModel() throws Exception {
        // given / when / then
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isOk())
                .andExpect(view().name("broadcasts"))
                .andExpect(model().attributeExists("broadcasts"))
                .andExpect(model().attributeExists("newBroadcast"))
                .andExpect(model().attribute("currentUri", "broadcasts"));
    }

    @Test
    void createBroadcast_thenGetBroadcasts_shouldPersistMailing() throws Exception {
        // given
        long before = mailingRepository.count();

        // when
        mockMvc.perform(post("/broadcasts")
                        .param("name", "E2E рассылка")
                        .param("status", MailingStatus.DRAFT.name())
                        .param("createdBy.id", String.valueOf(creatorId))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/broadcasts"));

        // then
        long after = mailingRepository.count();
        assertEquals(before + 1, after, "После POST должна появиться новая рассылка в БД");

        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isOk())
                .andExpect(view().name("broadcasts"))
                .andExpect(model().attributeExists("broadcasts"))
                .andExpect(model().attributeExists("newBroadcast"))
                .andExpect(model().attribute("currentUri", "broadcasts"));

        assertTrue(mailingRepository.findAllByOrderByCreatedAtDesc()
                        .stream()
                        .anyMatch(m -> "E2E рассылка".equals(m.getName())),
                "В списке рассылок должна присутствовать созданная рассылка");
    }
}
