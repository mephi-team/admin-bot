package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.dataproviders.StudentDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.StudentPresenter;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentView extends VerticalLayout {
    private List<Long> selectedIds;

    public StudentView(StudentPresenter actions) {
        StudentDataProvider provider = (StudentDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(SimpleUser.class, false);
        grid.addColumn(SimpleUser::getFullName).setHeader(getTranslation("grid_student_header_name_label")).setSortable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleUser::getEmail).setHeader(getTranslation("grid_student_header_email_label")).setSortable(true).setKey("email");
        grid.addColumn(SimpleUser::getTgName).setHeader(getTranslation("grid_student_header_telegram_label")).setSortable(true).setKey("tgName");
        grid.addColumn(SimpleUser::getPhoneNumber).setHeader(getTranslation("grid_student_header_phone_label")).setSortable(true).setKey("phoneNumber");
        grid.addColumn(SimpleUser::getCohort).setHeader(getTranslation("grid_student_header_cohort_label")).setSortable(true).setKey("cohort");
        grid.addColumn(SimpleUser::getDirection).setHeader(getTranslation("grid_student_header_direction_label")).setSortable(true).setKey("direction");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_student_header_city_label")).setSortable(true).setKey("city");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_student_header_tutor_label")).setKey("tutor");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button dropButton = new Button(getTranslation("grid_student_action_drop_label"), new Icon(VaadinIcon.CLOSE), e -> actions.onExpel(List.of(item.getId())));
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", item.getId().toString())));
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> actions.onEdit(item));
            Button blockButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onBlock(item));
            if (item.getStatus().equals(UserStatus.BLOCKED.name())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            group.add(dropButton, viewButton, chatButton, editButton, blockButton);
            return group;
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("340px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<SimpleUser> selectionModel = (GridMultiSelectionModel<SimpleUser>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(SimpleUser::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField(getTranslation("grid_student_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new Button(VaadinIcon.DOWNLOAD_ALT.create());

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }
}