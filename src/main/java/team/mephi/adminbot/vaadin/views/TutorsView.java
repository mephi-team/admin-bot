package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.repository.TutorRepository;

public class TutorsView extends VerticalLayout {
    public TutorsView(TutorRepository tutorRepository) {
        setHeightFull();

        final TextField searchField = createSearchField();

        var filterableProvider = getProvider(tutorRepository, searchField);
        Grid<TutorWithCounts> grid = new Grid<>(TutorWithCounts.class, false);
        grid.addColumn(a -> a.getLastName() + " " + a.getFirstName()).setHeader("Фамилия Имя").setSortable(true);
        grid.addColumn(TutorWithCounts::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(TutorWithCounts::getTgId).setHeader("Telegram").setSortable(true);
        grid.addColumn(TutorWithCounts::getDirections).setHeader("Направление").setSortable(true);
        grid.addColumn(TutorWithCounts::getStudentCount).setHeader("Кураторство").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(filterableProvider);
        grid.setHeightFull();

        searchField.addValueChangeListener(e -> {
            filterableProvider.setFilter(e.getValue());
        });

        add(searchField, grid);
    }

    private static ConfigurableFilterDataProvider<TutorWithCounts, Void, String> getProvider(TutorRepository questionRepository, TextField searchField) {
        CallbackDataProvider<TutorWithCounts, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return questionRepository.findAllWithDirectionsAndStudents(searchField.getValue())
                            .stream()
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> questionRepository.countByName(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Найти куратора");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        return searchField;
    }
}
