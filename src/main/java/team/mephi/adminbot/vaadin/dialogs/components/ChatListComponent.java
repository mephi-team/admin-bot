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

import java.util.Optional;

public class ChatListComponent extends VerticalLayout implements AfterNavigationObserver {
    private final ChatListDataProvider provider;
    private final MessageInput chatInput;
    private final VirtualList<ChatListItem> chatList;
    private final Div emptyMessage;
    private final VerticalLayout header;

    private Long dialogId;

    public ChatListComponent(ChatListDataProviderFactory dataProviderFactory) {
        this.provider = dataProviderFactory.createDataProvider();
        setHeightFull();
        addClassNames(LumoUtility.Padding.Top.NONE);

        emptyMessage = new Div(getTranslation("page_dialogs_chat_not_selected"));
        emptyMessage.setVisible(false);

        header = new VerticalLayout();
        header.setPadding(false);
        header.setSpacing(0, Unit.PIXELS);

        chatList = new VirtualList<>();
        chatList.setDataProvider(provider.getFilterableProvider());
        chatList.setRenderer(createMessageRenderer());

        chatInput = createChatInput();

        add(buildChatArea(header, chatList, emptyMessage), chatInput);
    }

    private ComponentRenderer<Div, ChatListItem> createMessageRenderer() {
        return new ComponentRenderer<>(this::renderChatItem);
    }

    private Div renderChatItem(ChatListItem item) {
        if (item.isHeader()) {
            return renderDateHeader(item.getDateLabel());
        }
        return renderMessage(item);
    }

    private Div renderMessage(ChatListItem item) {
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

    private Div renderDateHeader(String label) {
        Div header = new Div(label);
        header.addClassNames(LumoUtility.TextAlignment.CENTER, LumoUtility.FontSize.XXSMALL, LumoUtility.FontWeight.SEMIBOLD);
        header.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Vertical.SMALL);
        return header;
    }

    private VerticalLayout buildChatArea(Component... components) {
        var container = new VerticalLayout(components);
        container.setHeightFull();
        container.setSpacing(0, Unit.PIXELS);
        container.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
        container.getElement().getStyle().set("border-radius", "12px");
        return container;
    }

    private MessageInput createChatInput() {
        var input = new MessageInput();
        var i18n = new MessageInputI18n();
        i18n.setSend(getTranslation("chat.send"));
        i18n.setMessage(getTranslation("chat.placeholder"));
        input.setI18n(i18n);
        input.setWidthFull();
        input.addSubmitListener(submitEvent -> onMessage(submitEvent.getValue()));
        return input;
    }

    private void onMessage(String message) {
        if (dialogId == null) return;
        provider.save(dialogId, message);
        provider.getFilterableProvider().refreshAll();
        chatList.scrollToEnd();
    }

    private void updateVisibility(boolean hasDialog) {
        header.setVisible(hasDialog);
        chatInput.setVisible(hasDialog);
        chatList.setVisible(hasDialog);
        emptyMessage.setVisible(!hasDialog);
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

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Optional<String> optionalId = event.getRouteParameters().get("dialogId");
        boolean hasDialog = optionalId.isPresent();

        if (hasDialog) {
            dialogId = Long.parseLong(optionalId.get());
            provider.getFilterableProvider().setFilter(dialogId);
        } else {
            dialogId = null;
        }

        updateVisibility(hasDialog);

        if (dialogId != null) {
            header.removeAll();
            provider.findById(dialogId).ifPresent(this::renderHeader);
        }

        provider.getFilterableProvider().refreshAll();
    }
}
