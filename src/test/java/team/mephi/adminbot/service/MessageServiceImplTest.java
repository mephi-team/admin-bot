package team.mephi.adminbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.MessageSenderType;
import team.mephi.adminbot.model.enums.MessageStatus;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private MessageServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MessageServiceImpl(authService, messageRepository, dialogRepository, userRepository);
    }

    @Test
    void sendCreatesMessageAndUpdatesDialog() {
        DefaultOidcUser userInfo = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                new OidcIdToken("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("email", "expert@example.com")),
                "email"
        );
        User sender = User.builder().id(3L).email("expert@example.com").role(Role.builder().code("EXPERT").build()).build();
        Dialog dialog = new Dialog();
        dialog.setId(12L);
        dialog.setUser(User.builder().id(7L).userName("Student").role(Role.builder().code("USER").build()).build());
        dialog.setDirection(Direction.builder().id(2L).name("Design").build());

        when(dialogRepository.findById(12L)).thenReturn(Optional.of(dialog));
        when(authService.getUserInfo()).thenReturn(userInfo);
        when(userRepository.findByEmail("expert@example.com")).thenReturn(Optional.of(sender));

        service.send(12L, "Hello");

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(messageCaptor.capture());
        Message savedMessage = messageCaptor.getValue();
        assertThat(savedMessage.getText()).isEqualTo("Hello");
        assertThat(savedMessage.getSenderType()).isEqualTo(MessageSenderType.EXPERT);
        assertThat(savedMessage.getStatus()).isEqualTo(MessageStatus.SENT);

        ArgumentCaptor<Dialog> dialogCaptor = ArgumentCaptor.forClass(Dialog.class);
        verify(dialogRepository).save(dialogCaptor.capture());
        assertThat(dialogCaptor.getValue().getUnreadCount()).isZero();
    }
}
