package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.vaadin.components.ChatListComponent;
import team.mephi.adminbot.vaadin.components.DialogListComponent;

@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Dialogs extends VerticalLayout implements BeforeEnterObserver {

    public Dialogs(DialogRepository dialogRepository, MessageRepository messageRepository) {
        setSizeFull();

        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setPadding(false);
        leftColumn.setWidth("30%");
        leftColumn.add(new DialogListComponent(dialogRepository));

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setPadding(false);
        rightColumn.setWidth("70%");
        rightColumn.add(new ChatListComponent(messageRepository));

        SplitLayout contentLayout = new SplitLayout(leftColumn, rightColumn);
        contentLayout.setSizeFull();

        add(new H1(getTranslation("page_dialogs_title")), contentLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println("!!! q = " + event.getLocation().getQueryParameters());
    }
}
