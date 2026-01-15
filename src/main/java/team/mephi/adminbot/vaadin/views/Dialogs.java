package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.components.layout.DialogsLayout;
import team.mephi.adminbot.vaadin.dialogs.components.ChatListComponent;
import team.mephi.adminbot.vaadin.dialogs.components.DialogListComponent;
import team.mephi.adminbot.vaadin.dialogs.dataproviders.ChatListDataProviderFactory;
import team.mephi.adminbot.vaadin.dialogs.dataproviders.DialogListDataProviderFactory;

/**
 * Представление страницы диалогов с разделением на список диалогов и чат.
 * Доступно только пользователям с ролью ADMIN.
 */
@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Dialogs extends VerticalLayout {

    /**
     * Конструктор для создания представления страницы диалогов.
     *
     * @param dialogListDataProviderFactory фабрика для создания провайдера данных списка диалогов.
     * @param chatListDataProviderFactory   фабрика для создания провайдера данных чата.
     */
    public Dialogs(DialogListDataProviderFactory dialogListDataProviderFactory, ChatListDataProviderFactory chatListDataProviderFactory) {
        setSizeFull();
        getElement().getStyle().set("padding-inline", "53px 120px");
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setPadding(false);
        leftColumn.setWidth("30%");
        leftColumn.add(new DialogListComponent(dialogListDataProviderFactory));

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setPadding(false);
        rightColumn.setWidth("70%");
        rightColumn.add(new ChatListComponent(chatListDataProviderFactory));

        SplitLayout contentLayout = new SplitLayout(leftColumn, rightColumn);
        contentLayout.setSizeFull();

        add(new H1(getTranslation("page_dialogs_title")), contentLayout);
    }
}
