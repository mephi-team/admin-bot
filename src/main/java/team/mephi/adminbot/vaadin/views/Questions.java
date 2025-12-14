package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.dto.UserQuestionDto;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.repository.QuestionRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.vaadin.components.SearchField;

@Route(value = "/questions", layout = DialogsLayout.class)
public class Questions extends VerticalLayout {
    public Questions(UserQuestionRepository questionRepository) {
        setHeightFull();

        add(new H1("Вопросы"));

        Grid<UserQuestionDto> grid = new Grid<>(UserQuestionDto.class, false);
//        grid.addComponentColumn(item -> {
//                    Checkbox checkbox = new Checkbox();
//                    // Опционально: связать состояние чекбокса с полем в объекте Person
//                    // checkbox.setValue(item.isSelected());
//                    // checkbox.addValueChangeListener(event -> item.setSelected(event.getValue()));
//                    return checkbox;
//                }).setHeader("Выбрать")
//                .setFlexGrow(0) // Не растягивать этот столбец
//                .setWidth("75px"); // Задать фиксированную ширину

        grid.addColumn(UserQuestionDto::getQuestion).setHeader("Вопрос").setSortable(true);
        grid.addColumn(UserQuestionDto::getDate).setHeader("Дата").setSortable(true);
        grid.addColumn(UserQuestionDto::getUser).setHeader("Автор").setSortable(true);
        grid.addColumn(UserQuestionDto::getRole).setHeader("Роль").setSortable(true);
        grid.addColumn(UserQuestionDto::getDirection).setHeader("Направление").setSortable(true);
        grid.addColumn(UserQuestionDto::getAnswer).setHeader("Ответ").setSortable(true);
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

    private static ConfigurableFilterDataProvider<UserQuestionDto, Void, String> getProvider(UserQuestionRepository questionRepository, TextField searchField) {
        CallbackDataProvider<UserQuestionDto, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    return questionRepository.findAllByText(searchField.getValue())
                            .stream()
                            .map(u -> UserQuestionDto
                                    .builder()
                                    .id(u.getId())
                                    .question(u.getText())
                                    .date(u.getCreatedAt())
                                    .user(u.getUser().getUserName())
                                    .role(u.getRole())
                                    .direction(u.getDirection() != null ? u.getDirection().getName() : "")
                                    .answer(u.getAnswers().getLast().getAnswerText())
                                    .build()
                            )
                            .skip(query.getOffset())
                            .limit(query.getLimit());
                },
                query -> questionRepository.countByText(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }
}
