package team.mephi.adminbot.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.ChatListItem;
import team.mephi.adminbot.dto.MessagesForListDto;
import team.mephi.adminbot.dto.SimpleDialog;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.model.enums.MessageSenderType;
import team.mephi.adminbot.model.enums.MessageStatus;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для управления сообщениями и диалогами.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final LocalDateTime today = LocalDateTime.now();

    private final AuthService authService;
    private final MessageRepository messageRepository;
    private final DialogRepository dialogRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param authService       сервис для аутентификации и авторизации пользователей.
     * @param messageRepository репозиторий для управления сообщениями.
     * @param dialogRepository  репозиторий для управления диалогами.
     * @param userRepository    репозиторий для управления пользователями.
     */
    public MessageServiceImpl(AuthService authService, MessageRepository messageRepository, DialogRepository dialogRepository, UserRepository userRepository) {
        this.authService = authService;
        this.messageRepository = messageRepository;
        this.dialogRepository = dialogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ChatListItem> findAllByDialogId(Long dialogId) {
        List<MessagesForListDto> messages = messageRepository.findAllByDialogId(dialogId)
                .stream()
                .map(m -> new MessagesForListDto(
                        m.getId(),
                        m.getText(),
                        m.getCreatedAt(),
                        m.getSenderType().name()
                ))
                .sorted(Comparator.comparing(m -> m.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDate())) // сортировка по дате
                .toList();

        // --- Группировка по дате ---
        Map<LocalDate, List<MessagesForListDto>> grouped = messages.stream()
                .collect(Collectors.groupingBy(m -> m.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDate(), LinkedHashMap::new, Collectors.toList()));

        // --- Преобразование в список с заголовками ---
        List<ChatListItem> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<MessagesForListDto>> entry : grouped.entrySet()) {
            result.add(ChatListItem.header(formatDate(entry.getKey())));
            entry.getValue().forEach(msg -> result.add(ChatListItem.message(msg)));
        }

        return result;
    }

    @Override
    public Optional<SimpleDialog> findById(Long dialogId) {
        return dialogRepository.findByIdWithUser(dialogId).map(d -> SimpleDialog.builder()
                .id(d.getId())
                .userName(d.getUser().getUserName())
                .tgId(d.getUser().getTgId())
                .role(d.getUser().getRole().getDescription())
                .direction(d.getDirection().getName())
                .cohort(d.getUser().getCohort())
                .build());
    }

    @Override
    public Integer countByDialogId(Long dialogId) {
        return messageRepository.countByDialogId(dialogId) + messageRepository.countByDialogIdAndCreatedAt(dialogId);
    }

    @Override
    public void send(Long dialogId, String messageText) {
        var message = new Message();
        var dialog = dialogRepository.findById(dialogId).orElseThrow();
        var createdAt = Instant.now();
        message.setDialog(dialog);
        message.setCreatedAt(createdAt);
        message.setUpdatedAt(createdAt);
        message.setSenderType(MessageSenderType.EXPERT);
        var email = authService.getUserInfo().getEmail();
        message.setSender(userRepository.findByEmail(email).orElseThrow());
        message.setStatus(MessageStatus.SENT);
        message.setText(messageText);
        messageRepository.save(message);
        dialog.setLastMessageAt(createdAt);
        dialog.setUnreadCount(0);
        dialogRepository.save(dialog);
    }

    @Override
    public Integer unreadCount() {
        return dialogRepository.unreadCount();
    }

    /**
     * Форматирует дату для отображения в заголовке.
     *
     * @param date дата для форматирования
     * @return отформатированная строка даты
     */
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
}
