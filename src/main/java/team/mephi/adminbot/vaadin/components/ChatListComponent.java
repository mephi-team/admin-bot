package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import team.mephi.adminbot.dto.MessagesForListDto;
import team.mephi.adminbot.repository.MessageRepository;

import java.util.Optional;

public class ChatListComponent extends VerticalLayout implements AfterNavigationObserver {
    private final CallbackDataProvider<MessagesForListDto, Long> provider;
    private Long dialogId;
    MessageInput chatInput;
    VirtualList<MessagesForListDto> chatList;
    Div emptyMessage = new Div("Выберите диалог, чтобы продолжить общение");

    ComponentRenderer<Div, MessagesForListDto> cardRenderer = new ComponentRenderer<>(item -> {
        var card = new Div(item.getText());
        card.getStyle().set("min-height", "50px");
        card.getStyle().set("max-width", "50%");
        card.getStyle().set("padding", "16px");
        card.getStyle().set("margin", "16px");
        card.getStyle().set("border-radius", "24px");
        if (item.getSenderType().equals("USER")) {
            card.getElement().getStyle().set("background-color", "#2168df1a");
            card.getStyle().set("border-end-end-radius", "0");
            card.getStyle().set("justify-self", "end");
        } else {
            card.getElement().getStyle().set("background-color", "#eaeaee");
            card.getStyle().set("border-end-start-radius", "0");
        }
        return card;
    });

    public ChatListComponent(MessageRepository messageRepository) {
        this.provider = getProvider(messageRepository);

        chatList = new VirtualList<>();
        chatList.setDataProvider(provider);
        chatList.setRenderer(cardRenderer);

        emptyMessage.setVisible(false);
        emptyMessage.getElement().getStyle().set("padding", "1em");

        VerticalLayout v = new VerticalLayout();
        v.setHeightFull();
        v.getElement().getStyle().set("border", "1px solid #eaeaee");
        v.getElement().getStyle().set("border-radius", "12px");
        v.setPadding(false);
        v.add(chatList, emptyMessage);

        chatInput = new MessageInput();
        chatInput.setWidthFull();
        add(v, chatInput);

        setHeightFull();
        getElement().getStyle().set("padding-block-start", "0");
    }

    private CallbackDataProvider<MessagesForListDto, Long> getProvider(MessageRepository messageRepository) {
        return new CallbackDataProvider<>(
                query -> {
                    return messageRepository.findAllByDialogId(dialogId)
                            .stream().map(a -> new MessagesForListDto(a.getId(), a.getText(), a.getSenderType().name()))
                            .skip(query.getOffset())
                            .limit(query.getLimit());
                },
                query -> messageRepository.countByDialogId(dialogId)
        );
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Optional<String> optionalId = event.getRouteParameters().get("dialogId");
        if (optionalId.isPresent()) {
            dialogId = Long.parseLong(optionalId.get());
            provider.withConfigurableFilter().setFilter(dialogId);
            chatInput.setVisible(true);
            chatList.setVisible(true);
            emptyMessage.setVisible(false);
        } else {
            chatInput.setVisible(false);
            chatList.setVisible(false);
            emptyMessage.setVisible(true);
        }
        provider.refreshAll();
    }
}
