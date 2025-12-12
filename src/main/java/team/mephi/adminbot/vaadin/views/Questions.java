package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.QuestionRepository;
import team.mephi.adminbot.vaadin.components.SearchField;

@Route(value = "/questions", layout = DialogsLayout.class)
public class Questions extends VerticalLayout {
    public Questions(QuestionRepository questionRepository) {
        add(new H1("Вопросы"));

        Grid<Question> grid = new Grid<>(Question.class, false);

//        grid.addComponentColumn(item -> {
//                    Checkbox checkbox = new Checkbox();
//                    // Опционально: связать состояние чекбокса с полем в объекте Person
//                    // checkbox.setValue(item.isSelected());
//                    // checkbox.addValueChangeListener(event -> item.setSelected(event.getValue()));
//                    return checkbox;
//                }).setHeader("Выбрать")
//                .setFlexGrow(0) // Не растягивать этот столбец
//                .setWidth("75px"); // Задать фиксированную ширину

        grid.addColumn(Question::getQuestionText).setHeader("Вопрос").setSortable(true);
        grid.addColumn(Question::getCreatedAt).setHeader("Дата").setSortable(true);
        grid.addColumn(Question::getCreatedAt).setHeader("Автор").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        final TextField searchField = new SearchField("Найти вопрос");

        var filterableProvider = getProvider(questionRepository, searchField);

        grid.setDataProvider(filterableProvider);

        searchField.addValueChangeListener(e -> {
            // setFilter will refresh the data provider and trigger data
            // provider fetch / count queries. As a side effect, the pagination
            // controls will be updated.
            filterableProvider.setFilter(e.getValue());
        });

        add(searchField, grid);
    }

    private static ConfigurableFilterDataProvider<Question, Void, String> getProvider(QuestionRepository questionRepository, TextField searchField) {
        CallbackDataProvider<Question, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return questionRepository.findAllLikeQuestionText(searchField.getValue())
                            .stream()
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> questionRepository.countByQuestionText(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }
}
