package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryIconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryIconButton;
import team.mephi.adminbot.vaadin.components.grid.AbstractGridView;
import team.mephi.adminbot.vaadin.components.grid.GridViewConfig;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.users.dataproviders.CandidateDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Set;

/**
 * Представление для отображения кандидатов в системе.
 * Расширяет абстрактный класс AbstractGridView для работы с сущностями SimpleUser.
 */
public class CandidateView extends AbstractGridView<SimpleUser> {

    private final UsersPresenter actions;

    /**
     * Конструктор для создания представления кандидатов.
     *
     * @param actions презентер с действиями для пользователей.
     */
    public CandidateView(UsersPresenter actions) {
        super();

        this.actions = actions;

        CandidateDataProvider provider = (CandidateDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new SecondaryButton(getTranslation("grid_candidate_actions_accept_label"), VaadinIcon.CHECK.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty())
                        actions.onAccept(selectedIds, selectedIds.size() > 1 ? DialogType.ACCEPT_USERS_ALL : DialogType.ACCEPT_USERS, "" + selectedIds.size());
                }),
                new SecondaryButton(getTranslation("grid_candidate_actions_reject_label"), VaadinIcon.CLOSE.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty())
                        actions.onReject(selectedIds, selectedIds.size() > 1 ? DialogType.REJECT_USERS_ALL : DialogType.REJECT_USERS, "" + selectedIds.size());
                }),
                new SecondaryButton(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds, DialogType.DELETE_USERS);
                })
        );

        var config = GridViewConfig.<SimpleUser>builder()
                .gsa(gsa)
                .dataProvider(provider.getDataProvider())
                .filterSetter(s -> provider.getFilterableProvider().setFilter(s))
                .searchPlaceholder(getTranslation("grid_candidate_search_placeholder"))
                .emptyLabel(getTranslation("grid_candidate_empty_label"))
                .visibleColumns(Set.of())
                .hiddenColumns(Set.of("actions"))
                .build();

        setup(config);
    }

    @Override
    protected Class<SimpleUser> getItemClass() {
        return SimpleUser.class;
    }

    @Override
    protected void configureColumns(com.vaadin.flow.component.grid.Grid<SimpleUser> grid) {
        grid.addColumn(SimpleUser::getFullName).setHeader(getTranslation("grid_candidate_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleUser::getEmail).setHeader(getTranslation("grid_candidate_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleUser::getTgId).setHeader(getTranslation("grid_candidate_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgId");
        grid.addColumn(SimpleUser::getPhoneNumber).setHeader(getTranslation("grid_candidate_header_phone_label")).setSortable(true).setResizable(true).setKey("phoneNumber");
        grid.addColumn(UserRenderers.createPdRenderer()).setHeader(getTranslation("grid_candidate_header_pd_consent_label")).setSortable(true).setResizable(true).setKey("pdConsent");
        grid.addColumn(SimpleUser::getCohort).setHeader(getTranslation("grid_candidate_header_cohort_label")).setSortable(true).setResizable(true).setKey("cohort");
        grid.addColumn(UserRenderers.createUserDirections()).setHeader(getTranslation("grid_candidate_header_direction_label")).setSortable(true).setResizable(true).setKey("direction");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_candidate_header_city_label")).setSortable(true).setResizable(true).setKey("city");
    }

    @Override
    protected void configureActionColumn(com.vaadin.flow.component.grid.Grid<SimpleUser> grid) {
        grid.addComponentColumn(item -> {
            Button rejectButton = new SecondaryIconButton(VaadinIcon.CLOSE.create(), ignoredEvent -> actions.onReject(List.of(item.getId()), DialogType.REJECT_USERS));
            Button confirmButton = new PrimaryIconButton(VaadinIcon.CHECK.create(), ignoredEvent -> actions.onAccept(List.of(item.getId()), DialogType.ACCEPT_USERS));
            Button viewButton = new IconButton(VaadinIcon.EYE.create(), ignoredEvent -> actions.onView(item, DialogType.USERS_VIEW));
            Button chatButton = new IconButton(VaadinIcon.CHAT.create(), ignoredEvent -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", item.getId().toString())));
            Button editButton = new IconButton(VaadinIcon.PENCIL.create(), ignoredEvent -> actions.onEdit(item, DialogType.USERS_EDIT));
            Button blockButton = new IconButton(VaadinIcon.BAN.create(), ignoredEvent -> actions.onBlock(item, DialogType.USERS_BLOCKED));
            if (UserStatus.BLOCKED.name().equals(item.getStatus())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new ButtonGroup(rejectButton, confirmButton, viewButton, chatButton, editButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("280px").setFlexGrow(0).setKey("actions");
    }

    @Override
    protected Long extractId(SimpleUser item) {
        return item.getId();
    }
}