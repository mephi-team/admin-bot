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
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailingServiceImplTest {
    @Mock
    private AuthService authService;

    @Mock
    private MailingRepository mailingRepository;

    @Mock
    private UserRepository userRepository;

    private MailingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MailingServiceImpl(authService, mailingRepository, userRepository);
    }

    @Test
    void saveAssignsCreatedByAndDefaultsStatus() {
        DefaultOidcUser userInfo = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                new OidcIdToken("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("email", "author@example.com")),
                "email"
        );
        User author = User.builder().id(9L).email("author@example.com").role(Role.builder().code("ADMIN").build()).build();
        SimpleMailing input = SimpleMailing.builder()
                .name("Weekly")
                .text("Hello")
                .channels(Set.of("Email"))
                .recipients(List.of("u1"))
                .build();

        when(authService.getUserInfo()).thenReturn(userInfo);
        when(userRepository.findByEmail("author@example.com")).thenReturn(Optional.of(author));
        when(mailingRepository.save(any(Mailing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SimpleMailing result = service.save(input);

        ArgumentCaptor<Mailing> captor = ArgumentCaptor.forClass(Mailing.class);
        verify(mailingRepository).save(captor.capture());
        Mailing saved = captor.getValue();

        assertThat(saved.getCreatedBy()).isEqualTo(author);
        assertThat(saved.getStatus()).isEqualTo(MailingStatus.DRAFT);
        assertThat(result.getName()).isEqualTo("Weekly");
        assertThat(result.getStatus()).isEqualTo("DRAFT");
    }
}
