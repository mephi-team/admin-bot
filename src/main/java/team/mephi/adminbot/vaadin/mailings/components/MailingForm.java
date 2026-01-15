package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Форма для создания и редактирования рассылок.
 */
public class MailingForm extends FormLayout {
    @Getter
    private final CheckboxGroup<String> channels = new CheckboxGroup<>();
    @Getter
    private final ComboBox<RoleDto> users = new ComboBox<>();
    @Getter
    private final ComboBox<CohortDto> cohort = new ComboBox<>();
    @Getter
    private final ComboBox<SimpleDirection> direction = new ComboBox<>();
    @Getter
    private final ComboBox<CityDto> city = new ComboBox<>();
    @Getter
    private final ComboBox<UserDto> curator = new ComboBox<>();
    @Getter
    private final MultiSelectListBox<SimpleUser> listBox = new MultiSelectListBox<>();
    private final Span counter = new Span("(0)");

    /**
     * Конструктор формы рассылки.
     *
     * @param userService      сервис для работы с пользователями
     * @param roleService      сервис для работы с ролями
     * @param cohortService    сервис для работы с когортами
     * @param directionService сервис для работы с направлениями
     * @param cityService      сервис для работы с городами
     */
    public MailingForm(UserService userService, RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        var provider = new CallbackDataProvider<SimpleUser, String>(
                query -> {
                    Pageable pageable = PageRequest.of(query.getOffset() / query.getLimit(), query.getLimit());
                    return userService.findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(
                            Optional.of(users).map(AbstractField::getValue).map(RoleDto::getCode).orElse(null),
                            Optional.of(cohort).map(AbstractField::getValue).map(CohortDto::getName).orElse(null),
                            Optional.of(direction).map(AbstractField::getValue).map(SimpleDirection::getId).orElse(null),
                            Optional.of(city).map(AbstractField::getValue).filter(c -> c.getId() != null).map(CityDto::getName).orElse(null),
                            Optional.of(curator).map(AbstractField::getValue).map(UserDto::getId).orElse(null),
                            pageable
                    );
                },
                ignoredQuery -> userService.countByRoleAndName(Objects.isNull(users.getValue()) ? "" : users.getValue().getCode(), ""),
                SimpleUser::getId
        );
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        channels.setItems("Email", "Telegram");

        users.setItemsPageable(roleService::getAllRoles);
        users.setItemLabelGenerator(RoleDto::getName);
        users.addValueChangeListener(ignoredEvent -> provider.refreshAll());

        cohort.setItemsPageable(cohortService::getAllCohorts);
        cohort.setItemLabelGenerator(c -> c.getName() + (c.getCurrent() ? " (текущий)" : ""));
        cohort.addValueChangeListener(ignoredEvent -> provider.refreshAll());

        direction.setItemsPageable(directionService::getAllDirections);
        direction.setItemLabelGenerator(SimpleDirection::getName);
        direction.addValueChangeListener(ignoredEvent -> provider.refreshAll());

        city.setItemsPageable(cityService::getAllCities);
        city.setItemLabelGenerator(CityDto::getName);
        city.addValueChangeListener(ignoredEvent -> provider.refreshAll());

        curator.setItemsPageable(userService::findAllCurators);
        curator.setItemLabelGenerator(UserDto::getUserName);
        curator.addValueChangeListener(ignoredEvent -> provider.refreshAll());

        addFormItem(channels, getTranslation("form_mailing_channels_label"));
        addFormItem(users, getTranslation("form_mailing_users_label"));
        addFormItem(cohort, getTranslation("form_mailing_cohort_label"));
        addFormItem(direction, getTranslation("form_mailing_direction_label"));
        addFormItem(city, getTranslation("form_mailing_city_label"));
        addFormItem(curator, getTranslation("form_mailing_curator_label"));

        Accordion accordion = new Accordion();

        Span name = new Span(getTranslation("form_mailing_accordion_description_label"));
        name.addClassNames(LumoUtility.FontSize.SMALL);

        listBox.setWidthFull();
        listBox.addClassNames(LumoUtility.FontSize.SMALL);
        listBox.setDataProvider(provider);
        listBox.setItemLabelGenerator(u -> u.getFullName() + ", " + u.getTgId());
        listBox.addSelectionListener(e -> counter.setText("(" + e.getAllSelectedItems().size() + ")"));
        FormItem box = addFormItem(listBox, getTranslation("form_mailing_first_name_last_name_label"));
        box.addClassNames(LumoUtility.Width.FULL);

        VerticalLayout personalInformationLayout = new VerticalLayout(name, box);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);

        accordion.add(new AccordionPanel(new Span(new Span(getTranslation("form_mailing_accordion_label")), new Span(" "), counter), personalInformationLayout));
//        accordion.close();

        add(accordion);
    }
}
