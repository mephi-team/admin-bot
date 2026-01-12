package team.mephi.adminbot.vaadin.dialogs.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.ChatListItem;
import team.mephi.adminbot.dto.SimpleDialog;
import team.mephi.adminbot.vaadin.dialogs.dataproviders.ChatListDataProvider;
import team.mephi.adminbot.vaadin.dialogs.dataproviders.ChatListDataProviderFactory;

import java.util.*;

public class ChatListComponent extends VerticalLayout implements AfterNavigationObserver {
    private final ChatListDataProvider provider;
    MessageInput chatInput;
    VirtualList<ChatListItem> chatList;
    Div emptyMessage = new Div(getTranslation("page_dialogs_chat_not_selected"));
    VerticalLayout header = new VerticalLayout();

    ComponentRenderer<Div, ChatListItem> cardRenderer = new ComponentRenderer<>(item -> {
        if (item.isHeader()) {
            // Заголовок даты
            Div header = new Div(item.getDateLabel());
            header.addClassNames(LumoUtility.TextAlignment.CENTER, LumoUtility.FontSize.XXSMALL, LumoUtility.FontWeight.SEMIBOLD);
            header.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Vertical.SMALL);
            return header;
        } else {
            Div message = new Div(item.getMessage().getText());
            String date = item.getMessage().getDate().toString(); // Z означает UTC
            Div time = new Div();
            time.getElement().executeJs("const f=new Intl.DateTimeFormat(navigator.language, {hour: 'numeric', minute: 'numeric'});this.innerHTML=f.format(new Date($0));", date);
            message.addClassNames(LumoUtility.Display.GRID, LumoUtility.Margin.Vertical.XSMALL, LumoUtility.Overflow.HIDDEN);
            message.getStyle().set("padding", "12px").set("border-radius", "12px").set("max-width", "70%");
            if (item.getMessage().getSenderType().equals("USER")) {
                message.addClassNames(LumoUtility.Background.PRIMARY_10);
                message.getStyle().set("border-end-end-radius", "0").set("justify-self", "end");
                time.addClassNames(LumoUtility.TextAlignment.RIGHT);
            } else {
                message.addClassNames(LumoUtility.Background.CONTRAST_10);
                message.getStyle().set("border-end-start-radius", "0").set("justify-self", "start");
                time.addClassNames(LumoUtility.TextAlignment.LEFT);
            }
            message.add(time);
            time.addClassNames(LumoUtility.FontSize.XXSMALL, LumoUtility.TextColor.SECONDARY);
            return message;
        }
    });

    private Long dialogId;

    public ChatListComponent(ChatListDataProviderFactory dataProviderFactory) {
        this.provider = dataProviderFactory.createDataProvider();
        setHeightFull();
        addClassNames(LumoUtility.Padding.Top.NONE);

        chatList = new VirtualList<>();
        chatList.setDataProvider(provider.getFilterableProvider());
        chatList.setRenderer(cardRenderer);

        emptyMessage.setVisible(false);

        chatInput = createChatInput();

        header.setPadding(false);
        header.setSpacing(0, Unit.PIXELS);
        add(createChatContainer(header, chatList, emptyMessage), chatInput);
    }

    private VerticalLayout createChatContainer(Component... components) {
        var container = new VerticalLayout();
        container.setHeightFull();
        container.setSpacing(0, Unit.PIXELS);
        container.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
        container.getElement().getStyle().set("border-radius", "12px");
        container.add(components);
        return container;
    }

    private MessageInput createChatInput() {
        var input = new MessageInput();
        var i18n = new MessageInputI18n();
        i18n.setSend(getTranslation("chat.send"));
        i18n.setMessage(getTranslation("chat.placeholder"));
        input.setI18n(i18n);
        input.setWidthFull();
        input.addClassName("neo");
        input.addSubmitListener(submitEvent -> onMessage(submitEvent.getValue()));
        return input;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Optional<String> optionalId = event.getRouteParameters().get("dialogId");
        if (optionalId.isPresent()) {
            dialogId = Long.parseLong(optionalId.get());
            provider.getFilterableProvider().setFilter(dialogId);
            chatInput.setVisible(true);
            chatList.setVisible(true);
            header.setVisible(true);
            emptyMessage.setVisible(false);
        } else {
            header.setVisible(false);
            chatInput.setVisible(false);
            chatList.setVisible(false);
            emptyMessage.setVisible(true);
        }
        Optional.ofNullable(dialogId).ifPresent(id -> {
            header.removeAll();
            provider.findById(id).ifPresent(this::renderHeader);
        });
        provider.getFilterableProvider().refreshAll();
    }
    private void onMessage(String message) {
        provider.save(dialogId, message);
        provider.getFilterableProvider().refreshAll();
        chatList.scrollToEnd();
    }

    private void renderHeader(SimpleDialog dialog) {
        Span user = new Span(dialog.getUserName());
        user.addClassNames(LumoUtility.FontWeight.BOLD);
        Span login = new Span(dialog.getTgId());
        header.add(new Div(user, new Span(", "), login));
        Span role = new Span(dialog.getRole());
        Span direction = new Span(dialog.getDirection());
        Span cohort = new Span(dialog.getCohort());
        var secondLine = new Div(role, new Span(" | "), direction, new Span(" , "), cohort);
        secondLine.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);
        header.add(secondLine);
    }
}
