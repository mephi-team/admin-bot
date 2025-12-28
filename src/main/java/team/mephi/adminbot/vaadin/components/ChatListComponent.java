package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import team.mephi.adminbot.dto.MessagesForListDto;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.model.enums.MessageSenderType;
import team.mephi.adminbot.model.enums.MessageStatus;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ChatListComponent extends VerticalLayout implements AfterNavigationObserver {
    private static final LocalDateTime today = LocalDateTime.now();
    private final CallbackDataProvider<ChatListItem, Long> provider;
    MessageInput chatInput;
    VirtualList<ChatListItem> chatList;
    Div emptyMessage = new Div(getTranslation("page_dialogs_chat_not_selected"));
    ComponentRenderer<Div, ChatListItem> cardRenderer = new ComponentRenderer<>(item -> {
        Div container = new Div();
        if (item.isHeader) {
            // Заголовок даты
            Div header = new Div(item.dateLabel);
            header.getStyle()
                    .set("text-align", "center")
                    .set("font-size", "0.8rem")
                    .set("color", "#6c757d")
                    .set("margin", "12px 0")
                    .set("font-weight", "600");
            container.add(header);
        } else {
            Div message = new Div(item.message.getText());
            String date = formatDateForDisplay(item.message.getDate());
            Div time = new Div(date);
            message.getStyle()
                    .set("padding", "12px")
                    .set("margin", "4px 8px")
                    .set("border-radius", "12px")
                    .set("max-width", "70%")
                    .set("background", item.message.getSenderType().equals("USER") ? "#e1f5fe" : "#f1f1f1");
            if (item.message.getSenderType().equals("USER")) {
                message.getStyle().set("border-end-end-radius", "0");
                message.getStyle().set("justify-self", "end");
            } else {
                message.getStyle().set("border-end-start-radius", "0");
                time.getStyle().set("justify-self", "start");
            }
            message.add(time);
            time.getStyle().set("font-size", "0.75rem").set("color", "#888").set("text-align", "right");
            container.add(message);
        }
        return container;
    });

    private String formatDateForDisplay(Instant instant) {
        if (instant == null) return "";
        LocalDateTime date = instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
        return date.format(DateTimeFormatter.ofPattern("HH:mm", new Locale("ru")));
    }

    private Long dialogId;

    public ChatListComponent(AuthenticationContext authContext, DialogRepository dialogRepository, MessageRepository messageRepository, UserRepository userRepository) {
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
        chatInput.addSubmitListener(submitEvent -> {
            var message = new Message();
            message.setDialog(dialogRepository.findById(dialogId).orElseThrow());
            message.setCreatedAt(Instant.now());
            message.setUpdatedAt(Instant.now());
            message.setSenderType(MessageSenderType.EXPERT);
            var user = authContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();
            var email = user.getUserInfo().getEmail();
            message.setSender(userRepository.findByEmail(email).orElseThrow());
            message.setStatus(MessageStatus.SENT);
            message.setText(submitEvent.getValue());
            messageRepository.save(message);
            provider.refreshAll();
            chatList.scrollToEnd();
        });
        add(v, chatInput);

        setHeightFull();
        getElement().getStyle().set("padding-block-start", "0");
    }

    private CallbackDataProvider<ChatListItem, Long> getProvider(MessageRepository messageRepository) {
        return new CallbackDataProvider<>(
                query -> {
                    List<MessagesForListDto> messages = messageRepository.findAllByDialogId(dialogId)
                            .stream()
                            .map(m -> new MessagesForListDto(
                                    m.getId(),
                                    m.getText(),
                                    m.getCreatedAt(),
                                    m.getSenderType().name()
                            ))
                            .sorted(Comparator.comparing(m -> m.getDate().atZone(ZoneId.of("UTC")).toLocalDate())) // сортировка по дате
                            .toList();

                    // --- Группировка по дате ---
                    Map<LocalDate, List<MessagesForListDto>> grouped = messages.stream()
                            .collect(Collectors.groupingBy(m -> m.getDate().atZone(ZoneId.of("UTC")).toLocalDate(), LinkedHashMap::new, Collectors.toList()));

                    // --- Преобразование в список с заголовками ---
                    List<ChatListItem> result = new ArrayList<>();
                    for (Map.Entry<LocalDate, List<MessagesForListDto>> entry : grouped.entrySet()) {
                        result.add(ChatListItem.header(formatDate(entry.getKey())));
                        entry.getValue().forEach(msg -> result.add(ChatListItem.message(msg)));
                    }

                    return result.stream()
                            .skip(query.getOffset())
                            .limit(query.getLimit());
                },
                query -> messageRepository.countByDialogId(dialogId) + messageRepository.countByDialogIdAndCreatedAt(dialogId)
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

    private String formatDate(LocalDate date) {
        if (date == null) return "";

        DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datePart = date.format(todayFormatter);
        String todayPart = today.format(todayFormatter);

        if (datePart.equals(todayPart)) {
            return "Сегодня";
        } else {
            return date.format(DateTimeFormatter.ofPattern("dd MMMM"));
        }
    }

    private static class ChatListItem {
        private final boolean isHeader;
        private final String dateLabel; // только для isHeader = true
        private final MessagesForListDto message; // только для isHeader = false

        private ChatListItem(String dateLabel) {
            this.isHeader = true;
            this.dateLabel = dateLabel;
            this.message = null;
        }

        private ChatListItem(MessagesForListDto message) {
            this.isHeader = false;
            this.dateLabel = null;
            this.message = message;
        }

        public static ChatListItem header(String label) {
            return new ChatListItem(label);
        }

        public static ChatListItem message(MessagesForListDto msg) {
            return new ChatListItem(msg);
        }
    }
}
