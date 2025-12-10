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
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.repository.DialogRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для AnalyticsController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class AnalyticsControllerApiTest {

    @Mock
    private DialogRepository dialogRepository;

    @InjectMocks
    private AnalyticsController analyticsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(analyticsController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getAnalytics_defaultType_shouldReturnAnalyticsViewWithChartData() throws Exception {
        // given
        Dialog d1 = new Dialog();
        d1.setLastMessageAt(LocalDateTime.now().minusDays(1));

        Dialog d2 = new Dialog();
        d2.setLastMessageAt(LocalDateTime.now());

        when(dialogRepository.findLastWeekDialogs(any(LocalDateTime.class)))
                .thenReturn(List.of(d1, d2));

        // when / then
        mockMvc.perform(get("/analytics"))
                .andExpect(status().isOk())
                .andExpect(view().name("analytics"))
                .andExpect(model().attributeExists("chartLabels"))
                .andExpect(model().attributeExists("chartData"))
                .andExpect(model().attribute("type", "activity"));

        verify(dialogRepository).findLastWeekDialogs(any(LocalDateTime.class));
    }

    @Test
    void getAnalytics_withCustomType_shouldPassTypeToModel() throws Exception {
        // given
        String type = "custom";

        when(dialogRepository.findLastWeekDialogs(any(LocalDateTime.class)))
                .thenReturn(List.of());

        // when / then
        mockMvc.perform(get("/analytics").param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name("analytics"))
                .andExpect(model().attributeExists("chartLabels"))
                .andExpect(model().attributeExists("chartData"))
                .andExpect(model().attribute("type", type));

        verify(dialogRepository).findLastWeekDialogs(any(LocalDateTime.class));
    }
}
