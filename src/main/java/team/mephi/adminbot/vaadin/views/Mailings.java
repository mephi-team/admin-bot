package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.QuestionRepository;

import java.util.List;

@Route(value = "/mailings", layout = DialogsLayout.class)
public class Mailings extends VerticalLayout {
    public Mailings(MailingRepository mailingRepository) {
        add(new H1("Рассылки"));

        Grid<MailingList> grid = new Grid<>(MailingList.class, false);
        grid.addColumn(MailingList::getId).setHeader("Id").setSortable(true);
        grid.addColumn(MailingList::getName).setHeader("Text").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        final TextField searchField = createSearchField();

        var filterableProvider = getProvider(mailingRepository, searchField);
        grid.setDataProvider(filterableProvider);

        searchField.addValueChangeListener(e -> {
            // setFilter will refresh the data provider and trigger data
            // provider fetch / count queries. As a side effect, the pagination
            // controls will be updated.
            filterableProvider.setFilter(e.getValue());
        });

        add(searchField, grid);
    }

    private static ConfigurableFilterDataProvider<MailingList, Void, String> getProvider(MailingRepository mailingRepository, TextField searchField) {
        CallbackDataProvider<MailingList, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return mailingRepository.findMailingByName(searchField.getValue())
                            .stream()
                            .map(m -> MailingList.builder()
                                    .id(m.getId())
                                    .name(m.getName())
                                    .build())
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> mailingRepository.countByName(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Найти рассылку");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        return searchField;
    }
}
