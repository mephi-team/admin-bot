package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.grid.AbstractGridView;
import team.mephi.adminbot.vaadin.components.grid.GridViewConfig;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.users.dataproviders.GuestsDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.BlockingPresenter;

import java.util.Set;

public class GuestsView extends AbstractGridView<SimpleUser> {

    private final BlockingPresenter actions;

    public GuestsView(BlockingPresenter actions) {
        super();

        this.actions = actions;

        GuestsDataProvider provider = (GuestsDataProvider) actions.getDataProvider();

        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new SecondaryButton(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds, DialogType.DELETE_USERS);
                })
        );

        var config = GridViewConfig.<SimpleUser>builder()
                .gsa(gsa)
                .dataProvider(provider.getDataProvider())
                .filterSetter(s -> provider.getFilterableProvider().setFilter(s))
                .searchPlaceholder(getTranslation("grid_guests_search_placeholder"))
                .emptyLabel(getTranslation("grid_guests_empty_label"))
                .visibleColumns(Set.of("lastName", "email", "phoneNumber", "city"))
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
        grid.addColumn(SimpleUser::getTgName).setHeader(getTranslation("grid_guests_header_name_label")).setSortable(true).setResizable(true).setKey("tgName");
        grid.addColumn(SimpleUser::getTgId).setHeader(getTranslation("grid_guests_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgId");
        grid.addColumn(SimpleUser::getFullName).setHeader(getTranslation("grid_candidate_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleUser::getEmail).setHeader(getTranslation("grid_candidate_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleUser::getPhoneNumber).setHeader(getTranslation("grid_candidate_header_phone_label")).setSortable(true).setResizable(true).setKey("phoneNumber");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_candidate_header_city_label")).setSortable(true).setResizable(true).setKey("city");
        grid.addColumn(MyRenderers.createPdRenderer()).setHeader(getTranslation("grid_guests_header_pd_consent_label")).setSortable(true).setResizable(true).setKey("pdConsent");
    }

    @Override
    protected void configureActionColumn(com.vaadin.flow.component.grid.Grid<SimpleUser> grid) {
        grid.addComponentColumn(item -> {
            Button viewButton = new IconButton(VaadinIcon.EYE.create(), ignoredEvent -> actions.onView(item, DialogType.USERS_VIEW));
            Button blockButton = new IconButton(VaadinIcon.BAN.create(), ignoredEvent -> actions.onBlock(item, DialogType.USERS_BLOCKED));
            if (UserStatus.BLOCKED.name().equals(item.getStatus())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new ButtonGroup(viewButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("112px").setFlexGrow(0).setKey("actions");
    }

    @Override
    protected Long extractId(SimpleUser item) {
        return item.getId();
    }
}