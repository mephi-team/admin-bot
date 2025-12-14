package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.repository.DialogRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для AnalyticsController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {

    @Mock
    private DialogRepository dialogRepository;

    @InjectMocks
    private AnalyticsController analyticsController;

    private Dialog createDialog(Instant lastMessageAt) {
        Dialog dialog = new Dialog();
        dialog.setLastMessageAt(lastMessageAt);
        return dialog;
    }

    @Test
    void analyticsPage_shouldBuildChartDataFromDialogs() {
        // given
        LocalDate today = LocalDate.now();
        Instant todayMorning = today.atTime(10, 0).atZone(ZoneId.systemDefault()).toInstant();
        Instant todayEvening = today.atTime(18, 0).atZone(ZoneId.systemDefault()).toInstant();
        Instant yesterdayNoon = today.minusDays(1).atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant();

        // 2 диалога сегодня, 1 диалог вчера
        List<Dialog> dialogs = Arrays.asList(
                createDialog(todayMorning),
                createDialog(todayEvening),
                createDialog(yesterdayNoon)
        );

        when(dialogRepository.findLastWeekDialogs(any(Instant.class)))
                .thenReturn(dialogs);

        Model model = new ExtendedModelMap();
        String type = "activity";

        // when
        String viewName = analyticsController.analyticsPage(type, model);

        // then
        assertEquals("analytics", viewName);

        @SuppressWarnings("unchecked")
        List<String> labels = (List<String>) model.getAttribute("chartLabels");
        @SuppressWarnings("unchecked")
        List<Integer> data = (List<Integer>) model.getAttribute("chartData");

        assertNotNull(labels, "chartLabels должен быть в модели");
        assertNotNull(data, "chartData должен быть в модели");
        assertEquals(type, model.getAttribute("type"), "type должен прокидываться в модель как есть");

        // Ожидаем две уникальные даты: сегодня и вчера
        assertEquals(2, labels.size(), "Должно быть 2 уникальные даты в labels");
        assertEquals(2, data.size(), "Для каждой даты должно быть одно значение счётчика");

        // Суммарное количество диалогов = 3
        int totalCount = data.stream().mapToInt(Integer::intValue).sum();
        assertEquals(3, totalCount, "Суммарное количество сообщений по дням должно быть 3");

        // Проверим, что есть значения 2 и 1 (2 диалога в один день, 1 в другой)
        Set<Integer> uniqueCounts = Set.copyOf(data);
        assertTrue(uniqueCounts.contains(2), "В одном из дней должно быть 2 диалога");
        assertTrue(uniqueCounts.contains(1), "В одном из дней должен быть 1 диалог");
    }

    @Test
    void analyticsPage_withNoDialogs_shouldProduceEmptyChart() {
        // given
        when(dialogRepository.findLastWeekDialogs(any(Instant.class)))
                .thenReturn(List.of());

        Model model = new ExtendedModelMap();

        // when
        String viewName = analyticsController.analyticsPage("activity", model);

        // then
        assertEquals("analytics", viewName);

        @SuppressWarnings("unchecked")
        List<String> labels = (List<String>) model.getAttribute("chartLabels");
        @SuppressWarnings("unchecked")
        List<Integer> data = (List<Integer>) model.getAttribute("chartData");

        assertNotNull(labels, "chartLabels не должен быть null даже при отсутствии диалогов");
        assertNotNull(data, "chartData не должен быть null даже при отсутствии диалогов");
        assertTrue(labels.isEmpty(), "При пустом списке диалогов labels должен быть пустым");
        assertTrue(data.isEmpty(), "При пустом списке диалогов data должен быть пустым");
    }
}
