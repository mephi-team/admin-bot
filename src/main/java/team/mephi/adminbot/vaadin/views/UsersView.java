package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.users.components.RoleService;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialog;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;
import team.mephi.adminbot.vaadin.users.service.UsersPresenter;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.tabs.UserTabProvider;

import java.util.Comparator;
import java.util.List;

@Route("/users1")
@RolesAllowed("ADMIN")
public class UsersView extends VerticalLayout {

    private final UserEditorDialog editorDialog;
    private final TabSheet tabSheet = new TabSheet();

    public UsersView(
            List<UserTabProvider> tabProviders,
            UsersPresenter presenter,
            RoleService roleService // ← внедряем сервис
    ) {
        this.editorDialog = new UserEditorDialog(roleService);
        setSizeFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, editorDialog);
        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));
        // Создаём вкладки
        for (var provider : tabProviders) {
            var dataProvider = presenter.createDataProvider(provider.getTabId());
            var actions = createActions(provider.getTabId(), dataProvider);
            var content = provider.createTabContent(actions);
            tabSheet.add(new Tab(provider.getTabLabel()), content, provider.getPosition());
        }
    }

    private UserActions createActions(String role, UserDataProvider dataProvider) {
        return new UserActions() {
            @Override
            public void onView(Long id) {
                dataProvider.findUserById(id).ifPresent(editorDialog::openForView);
            }
            @Override
            public void onEdit(Long id) {
                dataProvider.findUserById(id).ifPresent(u -> {
                    editorDialog.openForEdit(u);
                    editorDialog.setOnSaveCallback(() -> {
                        dataProvider.save(editorDialog.getEditedUser());
                        dataProvider.refresh();
                    });
                });
            }
            @Override
            public void onDelete(List<Long> ids) {
                dataProvider.deleteAllById(ids);
            }
            @Override
            public void onAccept(List<Long> ids) {

            }
            @Override
            public void onReject(List<Long> ids) {

            }
        };
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1("Пользователи"));
        var primaryButton = new Button("Добавить пользователя", new Icon(VaadinIcon.PLUS), e -> {
            editorDialog.openForNew(getCurrentRole());
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div buttons = new Div(new Button("Загрузить из файла", new Icon(VaadinIcon.FILE_ADD)), primaryButton);
        buttons.getElement().getStyle().set("display", "flex");
        buttons.getElement().getStyle().set("gap", "24px");
        top.addToEnd(buttons);
        return top;
    }

    private String getCurrentRole() {
        var selectedTab = tabSheet.getSelectedIndex();
        return "visitor";
    }
}
