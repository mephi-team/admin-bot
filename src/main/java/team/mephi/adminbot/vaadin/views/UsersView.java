package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
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
            UsersPresenter presenter
    ) {
        this.editorDialog = new UserEditorDialog();
        setSizeFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, editorDialog);
        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));
        // Создаём вкладки
        for (var provider : tabProviders) {
            var dataProvider = presenter.createDataProvider(provider.getTabId());
            var actions = createActions(provider.getTabId(), dataProvider);
            var content = provider.createTabContent(dataProvider, actions);
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
                dataProvider.findUserById(id).ifPresent(editorDialog::openForEdit);
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
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.addToStart(new H1("Пользователи"));
        var addButton = new Button("Добавить", click -> {
//            var role = getCurrentRole(); // по активной вкладке
            editorDialog.openForNew("visitor");
        });
        header.addToEnd(addButton);
        return header;
    }
}
