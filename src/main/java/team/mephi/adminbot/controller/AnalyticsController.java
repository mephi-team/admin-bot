package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.repository.DialogRepository;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static tech.tablesaw.aggregate.AggregateFunctions.count;

@Controller
public class AnalyticsController {

    @Autowired
    private DialogRepository dialogRepository;

    @GetMapping("/analytics")
    public String analyticsPage(Model model) {
        YearMonth currentMonth = YearMonth.now();

        // Получаем диалоги за последнюю неделю
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<Dialog> dialogs = dialogRepository.findLastWeekDialogs(oneWeekAgo);

        // Извлекаем даты
        List<LocalDate> dates = dialogs.stream()
                .map(d -> d.getStartedAt().toLocalDate())
                .collect(Collectors.toList());

        // Создаём таблицу в Tablesaw
        Table table = Table.create(DateColumn.create("date", dates));
        Table dailyCounts = table.summarize("date", count).by("date");
        dailyCounts = dailyCounts.sortOn("date");

        // Преобразуем в JSON-совместимые списки
        // Вариант с отображением всех дней
//        Map<LocalDate, Integer> existingData = new HashMap<>();
//        List<LocalDate> existingDates = dailyCounts.dateColumn("date").asList();
//        List<Double> existingCounts = dailyCounts.doubleColumn("Count [date]").asList();
//        for (int i = 0; i < existingDates.size(); i++) {
//            existingData.put(existingDates.get(i), existingCounts.get(i).intValue());
//        }
//        // --- Добавляем пропущенные дни ---
//        List<String> labels = new ArrayList<>();
//        List<Integer> counts = new ArrayList<>();
//
//        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
//            LocalDate date = currentMonth.atDay(day);
//            labels.add(date.toString());
//            counts.add(existingData.getOrDefault(date, 0));
//        }
        // Вариант с отображением дней в которые найдены данные
        List<String> labels = dailyCounts.dateColumn("date").asList()
                .stream()
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        List<Integer> counts = dailyCounts.doubleColumn("Count [date]").asList().stream()
                .map(Double::intValue)
                .collect(Collectors.toList());

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", counts);
        return "analytics";
    }
}