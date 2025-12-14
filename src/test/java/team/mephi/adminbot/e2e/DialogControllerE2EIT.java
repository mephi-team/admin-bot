package team.mephi.adminbot.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.SenderType;
import team.mephi.adminbot.repository.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E-тесты для DialogController (проверяем список, детальную страницу, группировку сообщений).
 */
class DialogControllerE2EIT extends BaseE2EIT {

    @Autowired private MockMvc mockMvc;

    @Autowired private DialogRepository dialogRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private DirectionRepository directionRepository;

    private Long dialogId;

    @BeforeEach
    void setUp() {
        // given
        dialogRepository.deleteAll();
        userRepository.deleteAll();
        directionRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = roleRepository.save(Role.builder().name("USER").description("User").build());
        Direction dir = directionRepository.save(Direction.builder().code("IT").name("IT").build());

        User user = userRepository.save(User.builder()
                .externalId("tg-1")
                .name("Ivan")
                .firstName("Ivan")
                .lastName("Petrov")
                .status("active")
                .role(role)
                .build());

        Dialog dialog = new Dialog();
        dialog.setUser(user);
        dialog.setDirection(dir);
        dialog.setStatus("open");
        dialog.setLastMessageAt(LocalDateTime.now());

        Message m1 = new Message();
        m1.setDialog(dialog);
        m1.setSenderType(SenderType.USER);
        m1.setText("Hello");
        m1.setStatus("sent");
        m1.setCreatedAt(LocalDateTime.now().minusDays(1));

        Message m2 = new Message();
        m2.setDialog(dialog);
        m2.setSenderType(SenderType.ADMIN);
        m2.setText("Hi");
        m2.setStatus("sent");
        m2.setCreatedAt(LocalDateTime.now());

        dialog.setMessages(List.of(m1, m2));

        Dialog saved = dialogRepository.save(dialog);
        dialogId = saved.getId();
    }

    @Test
    void getDialogs_shouldReturnListView() throws Exception {
        // when / then
        mockMvc.perform(get("/dialogs"))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/list"))
                .andExpect(model().attributeExists("dialogs"))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));
    }

    @Test
    void viewDialog_shouldReturnDetailViewWithGroups() throws Exception {
        // when / then
        mockMvc.perform(get("/dialogs/{id}", dialogId))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/detail"))
                .andExpect(model().attributeExists("dialogs"))
                .andExpect(model().attributeExists("dialog"))
                .andExpect(model().attributeExists("messageGroups"))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));
    }
}
