package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import team.mephi.adminbot.dto.ChatListItem;
import team.mephi.adminbot.dto.SimpleDialog;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.MessageSenderType;
import team.mephi.adminbot.model.enums.MessageStatus;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для MessageServiceImpl.
 * Покрывают: выборку сообщений и отправку сообщений.
 */
@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    @Mock
    private AuthService authService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private DialogRepository dialogRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DefaultOidcUser userInfo;

    /**
     * Проверяет группировку сообщений по дате.
     */
    @Test
    void Given_messages_When_findAllByDialogId_Then_groupsByDate() {
        // Arrange
        Instant now = Instant.now();
        Message message = new Message();
        message.setId(1L);
        message.setText("Hello");
        message.setCreatedAt(now);
        message.setSenderType(MessageSenderType.USER);
        when(messageRepository.findAllByDialogId(eq(1L))).thenReturn(List.of(message));
        MessageServiceImpl service = new MessageServiceImpl(authService, messageRepository, dialogRepository, userRepository);

        // Act
        List<ChatListItem> result = service.findAllByDialogId(1L);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.getFirst().isHeader());
        assertFalse(result.get(1).isHeader());
    }

    /**
     * Проверяет получение диалога по идентификатору.
     */
    @Test
    void Given_dialog_When_findById_Then_mapsToSimpleDialog() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .userName("User Name")
                .tgId("tg")
                .role(Role.builder().description("Role").build())
                .cohort("2024")
                .build();
        Dialog dialog = new Dialog();
        dialog.setId(10L);
        dialog.setUser(user);
        dialog.setDirection(Direction.builder().name("Math").build());
        when(dialogRepository.findByIdWithUser(eq(10L))).thenReturn(Optional.of(dialog));
        MessageServiceImpl service = new MessageServiceImpl(authService, messageRepository, dialogRepository, userRepository);

        // Act
        Optional<SimpleDialog> result = service.findById(10L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("User Name", result.get().getUserName());
        assertEquals("Math", result.get().getDirection());
    }

    /**
     * Проверяет подсчёт сообщений для диалога.
     */
    @Test
    void Given_dialogId_When_countByDialogId_Then_returnsSum() {
        // Arrange
        when(messageRepository.countByDialogId(eq(2L))).thenReturn(3);
        when(messageRepository.countByDialogIdAndCreatedAt(eq(2L))).thenReturn(5);
        MessageServiceImpl service = new MessageServiceImpl(authService, messageRepository, dialogRepository, userRepository);

        // Act
        Integer result = service.countByDialogId(2L);

        // Assert
        assertEquals(8, result);
    }

    /**
     * Проверяет отправку сообщения и обновление диалога.
     */
    @Test
    void Given_messageText_When_send_Then_savesMessageAndDialog() {
        // Arrange
        Dialog dialog = new Dialog();
        dialog.setId(4L);
        User sender = User.builder().id(9L).build();
        when(dialogRepository.findById(eq(4L))).thenReturn(Optional.of(dialog));
        when(authService.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getEmail()).thenReturn("user@example.com");
        when(userRepository.findByEmail(eq("user@example.com"))).thenReturn(Optional.of(sender));
        MessageServiceImpl service = new MessageServiceImpl(authService, messageRepository, dialogRepository, userRepository);

        // Act
        service.send(4L, "Hi");

        // Assert
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(messageCaptor.capture());
        assertEquals(MessageSenderType.EXPERT, messageCaptor.getValue().getSenderType());
        assertEquals(MessageStatus.SENT, messageCaptor.getValue().getStatus());
        assertEquals("Hi", messageCaptor.getValue().getText());
        verify(dialogRepository).save(eq(dialog));
        assertEquals(0, dialog.getUnreadCount());
    }

    /**
     * Проверяет получение количества непрочитанных диалогов.
     */
    @Test
    void Given_repository_When_unreadCount_Then_returnsValue() {
        // Arrange
        when(dialogRepository.unreadCount()).thenReturn(12);
        MessageServiceImpl service = new MessageServiceImpl(authService, messageRepository, dialogRepository, userRepository);

        // Act
        Integer result = service.unreadCount();

        // Assert
        assertEquals(12, result);
    }
}
