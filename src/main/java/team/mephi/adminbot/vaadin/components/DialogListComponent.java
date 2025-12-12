package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import team.mephi.adminbot.dto.DialogWithLastMessageDto;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@AnonymousAllowed
public class DialogListComponent extends VirtualList<DialogWithLastMessageDto> implements AfterNavigationObserver {
    private final LocalDateTime today;
    private Long activeDialogId;

    ComponentRenderer<RouterLink, DialogWithLastMessageDto> cardRenderer = new ComponentRenderer<>(item -> {
        RouterLink link = new RouterLink();
        link.setClassName("dialog-item text-body");

        link.setRoute(Dialogs.class, new RouteParameters("dialogId", item.getDialogId().toString()));

        Div content = new Div();
        content.addClassNames("d-flex", "align-items-start");
        content.getElement().getStyle().set("padding", "12px");
        content.getElement().getStyle().set("border-radius", "12px");

        if (item.getDialogId().equals(activeDialogId)) {
            link.addClassNames("bg-primary", "bg-opacity-10");
            content.getElement().getStyle().set("background-color", "#d3e1f9");
        }

        Div mainContent = new Div();
        mainContent.addClassNames("flex-grow-1", "overflow-hidden");
        content.add(mainContent);

        // Заголовок: ФИО + дата
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.START);

        Span fullName = new Span();
        fullName.getStyle().set("font-weight", "bold");
        fullName.setText(item.getUserLastName() + " " + item.getUserFirstName());
        header.add(fullName);

        Span date = new Span();
        date.addClassName("text-muted");
        date.setText(formatDate((LocalDateTime) item.getLastMessageAt()));
        header.add(date);

        mainContent.add(header);

        // Роль и externalId
        Span roleInfo = new Span();
        roleInfo.addClassNames("text-muted", "small");
        roleInfo.setText(
                item.getUserRoleDescription().toLowerCase() +
                        " | @" + item.getUserExternalId()
        );
        mainContent.add(roleInfo);

        // Последнее сообщение
        Span lastMessage = new Span();
        lastMessage.addClassNames("text-truncate", "mt-1");
        lastMessage.getElement().getStyle().set("display", "block");
        lastMessage.getElement().getStyle().set("white-space", "nowrap");
        lastMessage.getElement().getStyle().set("text-overflow", "ellipsis");
        lastMessage.getElement().getStyle().set("overflow", "hidden");

        Span senderName = new Span();
        senderName.addClassName("text-primary");
        senderName.setText(getSenderDisplayName(item.getLastMessageSenderName(), item.getLastMessageSenderType()));

        Span messageText = new Span();
        messageText.setText(item.getLastMessageText());

        lastMessage.add(senderName, new Text(" "), messageText);
        mainContent.add(lastMessage);

        link.add(content);
        return link;
    });

    public DialogListComponent(ConfigurableFilterDataProvider<DialogWithLastMessageDto, Void, String> provider) {
        this.today = LocalDateTime.now();

        setDataProvider(provider);
        setRenderer(cardRenderer);
    }

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datePart = dateTime.format(todayFormatter);
        String todayPart = today.format(todayFormatter);

        if (datePart.equals(todayPart)) {
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern("dd MMMM"));
        }
    }

    private String getSenderDisplayName(String msg, String type) {
        if ("user".equals(type)) {
            return msg;
        } else {
            return "Администратор";
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // Извлекаем ID из текущего URL (вам нужно адаптировать эту логику под ваши маршруты)
        Optional<String> dialogId = event.getRouteParameters().get("dialogId");
        if (dialogId.isPresent()) {
            try {
                this.activeDialogId = Long.parseLong(dialogId.get());
                // После получения ID, перерендериваем список, чтобы применились стили
                getDataProvider().refreshAll();
            } catch (NumberFormatException e) {
                // Обработка ошибки, если ID не число
            }
        } else {
            this.activeDialogId = null;
            getDataProvider().refreshAll();
        }
    }
}
