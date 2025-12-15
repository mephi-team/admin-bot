package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.dto.UserQuestionDto;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.vaadin.components.GridSettingsButton;
import team.mephi.adminbot.vaadin.components.GridSettingsPopover;
import team.mephi.adminbot.vaadin.components.SearchField;
import team.mephi.adminbot.vaadin.components.SearchFragment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Route(value = "/questions", layout = DialogsLayout.class)
@PermitAll
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

        grid.addColumn(UserQuestionDto::getQuestion).setHeader("Вопрос").setSortable(true).setKey("question");
        grid.addColumn(UserQuestionDto::getDate).setHeader("Дата").setSortable(true).setKey("date");
        grid.addColumn(UserQuestionDto::getUser).setHeader("Автор").setSortable(true).setKey("author");
        grid.addColumn(UserQuestionDto::getRole).setHeader("Роль").setSortable(true).setKey("role");
        grid.addColumn(UserQuestionDto::getDirection).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(UserQuestionDto::getAnswer).setHeader("Ответ").setSortable(true).setKey("answer");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button responseButton = new Button("Ответить");
            responseButton.addClickListener(e -> {
                System.out.println(item);
            });
            Button editButton = new Button(new Icon(VaadinIcon.CHAT));
            editButton.addClickListener(e -> {
                System.out.println(item);
            });
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            editButton.addClickListener(e -> {
                System.out.println(item);
            });
            group.add(responseButton, editButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("220px").setFlexGrow(0).setKey("action");

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        final TextField searchField = new SearchField("Найти вопрос");

        var filterableProvider = getProvider(questionRepository, searchField);

        grid.setDataProvider(filterableProvider);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> {
            // System.out.printf("Number of selected people: %s%n",
            // selection.getAllSelectedItems().size());
        });

        searchField.addValueChangeListener(e -> {
            // setFilter will refresh the data provider and trigger data
            // provider fetch / count queries. As a side effect, the pagination
            // controls will be updated.
            filterableProvider.setFilter(e.getValue());
        });

        GridSettingsButton settings = new GridSettingsButton();
        GridSettingsPopover popover = new GridSettingsPopover(grid, Set.of());
        popover.setTarget(settings);
        SearchFragment headerLayout = new SearchFragment(searchField, settings);

        add(headerLayout, grid);
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
                                    .date(LocalDateTime.ofInstant(u.getCreatedAt(), ZoneId.of("UTC")))
                                    .user(u.getUser().getUserName())
                                    .role(u.getRole())
//                                    .direction(u.getDirection() != null ? u.getDirection().getName() : "")
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
