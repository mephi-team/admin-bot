package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import team.mephi.adminbot.dto.MessagesForListDto;

import java.util.Optional;

public class ChatListComponent extends VirtualList<MessagesForListDto> implements AfterNavigationObserver {
    private CallbackDataProvider<MessagesForListDto, Long> provider;

    ComponentRenderer<Div, MessagesForListDto> cardRenderer = new ComponentRenderer<>(item -> {
        System.out.println("MSG: " + item.getText());
        var card = new Div(item.getText());
//        card.getElement().getStyle().set("border", "1px solid red");
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

    public ChatListComponent(CallbackDataProvider<MessagesForListDto, Long> provider) {
        this.provider = provider;
        setDataProvider(provider);
        setRenderer(cardRenderer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // Извлекаем ID из текущего URL (вам нужно адаптировать эту логику под ваши маршруты)
        Optional<String> dialogId = event.getRouteParameters().get("dialogId");
        dialogId.ifPresent(id -> {
            var activeDialogId = Long.parseLong(id);
            provider.withConfigurableFilter().setFilter(activeDialogId);
        });
        getDataProvider().refreshAll();
    }
}
