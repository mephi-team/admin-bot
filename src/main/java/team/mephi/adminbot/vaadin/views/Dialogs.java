package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.*;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.vaadin.components.ChatListComponent;
import team.mephi.adminbot.vaadin.components.DialogListComponent;

@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
public class Dialogs extends VerticalLayout {

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

        add(new H1("Диалоги"), contentLayout);
    }

    VerticalLayout createEmpty() {
        VerticalLayout message = new VerticalLayout();
        message.getElement().getStyle().set("border", "1px solid #eaeaee");
        message.getElement().getStyle().set("border-radius", "12px");
        message.add("Выберите диалог, чтобы продолжить общение");
        message.setSizeFull();
        VerticalLayout out = new VerticalLayout(message);
        out.getElement().getStyle().set("padding-block-start", "0");
        out.setSizeFull();
        return out;
    }
}
