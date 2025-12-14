package team.mephi.adminbot.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E-тесты для AnalyticsController (полный контекст + PostgreSQL Testcontainers).
 */
class AnalyticsControllerE2EIT extends BaseE2EIT {

    @Autowired private MockMvc mockMvc;

    @Autowired private DialogRepository dialogRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private DirectionRepository directionRepository;

    @BeforeEach
    void setUp() {
        // given
        dialogRepository.deleteAll();
        userRepository.deleteAll();
        directionRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAnalytics_whenNoDialogs_shouldReturnEmptyChart() throws Exception {
        // given / when / then
        MvcResult result = mockMvc.perform(get("/analytics"))
                .andExpect(status().isOk())
                .andExpect(view().name("analytics"))
                .andExpect(model().attributeExists("chartLabels"))
                .andExpect(model().attributeExists("chartData"))
                .andExpect(model().attribute("type", "activity"))
                .andReturn();

        // then
        Map<String, Object> model = result.getModelAndView().getModel();
        @SuppressWarnings("unchecked")
        List<String> labels = (List<String>) model.get("chartLabels");
        @SuppressWarnings("unchecked")
        List<Integer> data = (List<Integer>) model.get("chartData");

        assertNotNull(labels);
        assertNotNull(data);
        assertTrue(labels.isEmpty(), "При отсутствии данных labels должен быть пустым");
        assertTrue(data.isEmpty(), "При отсутствии данных data должен быть пустым");
    }

    @Test
    void getAnalytics_whenDialogsExist_shouldBuildCountsAndPassCustomType() throws Exception {
        // given
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

        // 3 диалога: 2 сегодня, 1 вчера (в пределах недели)
        Dialog d1 = new Dialog();
        d1.setUser(user);
        d1.setDirection(dir);
        d1.setStatus("open");
        d1.setLastMessageAt(LocalDateTime.now().minusHours(1));

        Dialog d2 = new Dialog();
        d2.setUser(user);
        d2.setDirection(dir);
        d2.setStatus("open");
        d2.setLastMessageAt(LocalDateTime.now().minusHours(2));

        Dialog d3 = new Dialog();
        d3.setUser(user);
        d3.setDirection(dir);
        d3.setStatus("open");
        d3.setLastMessageAt(LocalDateTime.now().minusDays(1));

        dialogRepository.saveAll(List.of(d1, d2, d3));

        String type = "custom";

        // when / then
        MvcResult result = mockMvc.perform(get("/analytics").param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name("analytics"))
                .andExpect(model().attributeExists("chartLabels"))
                .andExpect(model().attributeExists("chartData"))
                .andExpect(model().attribute("type", type))
                .andReturn();

        // then
        Map<String, Object> model = result.getModelAndView().getModel();

        @SuppressWarnings("unchecked")
        List<String> labels = (List<String>) model.get("chartLabels");
        @SuppressWarnings("unchecked")
        List<Integer> data = (List<Integer>) model.get("chartData");

        assertNotNull(labels);
        assertNotNull(data);
        assertFalse(labels.isEmpty(), "labels не должен быть пустым при наличии диалогов");
        assertFalse(data.isEmpty(), "data не должен быть пустым при наличии диалогов");

        int total = data.stream().mapToInt(Integer::intValue).sum();
        assertEquals(3, total, "Сумма дневных счетчиков должна равняться числу диалогов (3)");
        assertEquals(labels.size(), data.size(), "labels и data должны быть одинаковой длины");
    }
}
