package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.vaadin.components.SearchField;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Route(value = "/mailings", layout = DialogsLayout.class)
public class Mailings extends VerticalLayout {
    public Mailings(MailingRepository mailingRepository) {
        setHeightFull();

        add(new H1("Рассылки"));

        Grid<MailingList> grid = new Grid<>(MailingList.class, false);
        grid.addColumn(MailingList::getDate).setHeader("Дата").setSortable(true);
        grid.addColumn(MailingList::getUsers).setHeader("Пользователи").setSortable(true);
        grid.addColumn(MailingList::getCohort).setHeader("Набор").setSortable(true);
        grid.addColumn(MailingList::getDirection).setHeader("Направление").setSortable(true);
        grid.addColumn(MailingList::getCurator).setHeader("Куратор").setSortable(true);
        grid.addColumn(MailingList::getCity).setHeader("Город").setSortable(true);
        grid.addColumn(MailingList::getText).setHeader("Текст сообщения").setSortable(true);
        grid.addColumn(MailingList::getStatus).setHeader("Статус").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        final TextField searchField = new SearchField("Найти рассылку");

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
                                    .date(LocalDateTime.ofInstant(m.getCreatedAt(), ZoneId.of("UTC")))
                                    .users(m.getFilters() != null ? m.getFilters().getUsers() : "")
                                    .cohort(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                    .direction(m.getFilters() != null ? m.getFilters().getDirection() : "")
                                    .curator(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                    .city(m.getFilters() != null ? m.getFilters().getCity() : "")
                                    .status(m.getStatus().name())
                                    .build())
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> mailingRepository.countByName(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }
}
