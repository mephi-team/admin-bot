package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.buttons.TextButton;
import team.mephi.adminbot.vaadin.components.grid.AbstractGridView;
import team.mephi.adminbot.vaadin.components.grid.GridViewConfig;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.users.dataproviders.TutorDataProviderImpl;
import team.mephi.adminbot.vaadin.users.presenter.TutorPresenter;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.Set;

public class TutorView extends AbstractGridView<SimpleTutor> {

    private final TutorPresenter actions;

    public TutorView(TutorPresenter actions) {
        super();

        this.actions = actions;

        TutorDataProviderImpl provider = (TutorDataProviderImpl) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new SecondaryButton(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds, DialogType.DELETE_USERS);
                })
        );

        var config = GridViewConfig.<SimpleTutor>builder()
                .gsa(gsa)
                .dataProvider(provider.getDataProvider())
                .filterSetter(s -> provider.getFilterableProvider().setFilter(s))
                .searchPlaceholder(getTranslation("grid_tutor_search_placeholder"))
                .emptyLabel(getTranslation("grid_tutor_empty_label"))
                .visibleColumns(Set.of())
                .hiddenColumns(Set.of("actions"))
                .build();

        setup(config);
    }

    @Override
    protected Class<SimpleTutor> getItemClass() {
        return SimpleTutor.class;
    }

    @Override
    protected void configureColumns(com.vaadin.flow.component.grid.Grid<SimpleTutor> grid) {
        grid.addColumn(SimpleTutor::getFullName).setHeader(getTranslation("grid_tutor_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleTutor::getCompetenceCenter).setHeader(getTranslation("grid_tutor_header_competence_center_label")).setSortable(true).setResizable(true).setKey("competenceCenter");
        grid.addColumn(SimpleTutor::getEmail).setHeader(getTranslation("grid_tutor_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleTutor::getTgId).setHeader(getTranslation("grid_tutor_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgId");
        grid.addColumn(MyRenderers.createTutorDirections()).setHeader(getTranslation("grid_tutor_header_direction_label")).setResizable(true).setKey("direction");
        grid.addColumn(MyRenderers.createCuratorshipRenderer()).setHeader(getTranslation("grid_tutor_header_curatorship_label")).setResizable(true).setKey("curatorship");
    }

    @Override
    protected void configureActionColumn(com.vaadin.flow.component.grid.Grid<SimpleTutor> grid) {
        grid.addComponentColumn(item -> {
            Button dropButton = new TextButton(getTranslation("grid_tutor_action_curatorship_label"), e -> actions.onTutoring(item, DialogType.TUTORS_UPDATED));
            Button viewButton = new IconButton(VaadinIcon.EYE.create(), e -> actions.onView(item, DialogType.TUTORS_VIEW));
            Button chatButton = new IconButton(VaadinIcon.CHAT.create(), e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", item.getId().toString())));
            Button editButton = new IconButton(VaadinIcon.PENCIL.create(), e -> actions.onEdit(item, DialogType.TUTORS_EDIT));
            Button blockButton = new IconButton(VaadinIcon.BAN.create(), e -> actions.onBlock(item, DialogType.TUTORS_BLOCKED));
            if (UserStatus.BLOCKED.name().equals(item.getStatus())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new ButtonGroup(dropButton, viewButton, chatButton, editButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("314px").setFlexGrow(0).setKey("actions");
    }

    @Override
    protected Long extractId(SimpleTutor item) {
        return item.getId();
    }
}