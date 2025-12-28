package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.ChatListComponent;
import team.mephi.adminbot.vaadin.components.DialogListComponent;

@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Dialogs extends VerticalLayout {

    public Dialogs(AuthenticationContext authContext, DialogRepository dialogRepository, MessageRepository messageRepository, UserRepository userRepository) {
        setSizeFull();

        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setPadding(false);
        leftColumn.setWidth("30%");
        leftColumn.add(new DialogListComponent(dialogRepository));

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setPadding(false);
        rightColumn.setWidth("70%");
        rightColumn.add(new ChatListComponent(authContext, dialogRepository, messageRepository, userRepository));

        SplitLayout contentLayout = new SplitLayout(leftColumn, rightColumn);
        contentLayout.setSizeFull();

        add(new H1(getTranslation("page_dialogs_title")), contentLayout);
    }
}
