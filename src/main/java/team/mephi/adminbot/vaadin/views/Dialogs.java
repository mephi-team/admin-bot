package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.*;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.vaadin.components.ChatListComponent;
import team.mephi.adminbot.vaadin.components.DialogListComponent;

@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
public class Dialogs extends VerticalLayout {
    private final VerticalLayout leftColumn = new VerticalLayout();
    private final VerticalLayout rightColumn = new VerticalLayout();

    public Dialogs(DialogRepository dialogRepository, MessageRepository messageRepository) {
        setSizeFull();

        leftColumn.setPadding(false);
        leftColumn.setWidth("30%");
        leftColumn.add(new DialogListComponent(dialogRepository));

        rightColumn.setPadding(false);
        rightColumn.setWidth("70%");
        rightColumn.add(new ChatListComponent(messageRepository));

        SplitLayout contentLayout = new SplitLayout(leftColumn, rightColumn);
        contentLayout.setSizeFull();

        add(new H1("Диалоги"), contentLayout);
    }
}
