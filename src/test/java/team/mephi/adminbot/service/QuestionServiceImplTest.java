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
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.model.enums.AnswerStatus;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {
    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQuestionRepository questionRepository;

    @Mock
    private UserAnswerRepository answerRepository;

    private QuestionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);
    }

    @Test
    void saveAnswerPersistsAnswer() {
        DefaultOidcUser userInfo = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                new OidcIdToken("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("email", "expert@example.com")),
                "email"
        );
        User expert = User.builder().id(4L).email("expert@example.com").role(Role.builder().code("EXPERT").build()).build();
        when(authService.getUserInfo()).thenReturn(userInfo);
        when(userRepository.findByEmail("expert@example.com")).thenReturn(Optional.of(expert));

        SimpleQuestion input = SimpleQuestion.builder().id(101L).answer("Answer text").build();

        service.saveAnswer(input);

        ArgumentCaptor<UserAnswer> captor = ArgumentCaptor.forClass(UserAnswer.class);
        verify(answerRepository).save(captor.capture());
        UserAnswer saved = captor.getValue();
        assertThat(saved.getAnswerText()).isEqualTo("Answer text");
        assertThat(saved.getStatus()).isEqualTo(AnswerStatus.SENT);
        assertThat(saved.getQuestion().getId()).isEqualTo(101L);
    }

    @Test
    void findByIdWithDepsMapsLatestAnswer() {
        UserQuestion question = UserQuestion.builder()
                .id(9L)
                .text("Question?")
                .createdAt(Instant.now())
                .user(User.builder().id(2L).userName("Author").role(Role.builder().code("USER").build()).build())
                .direction(Direction.builder().name("Design").build())
                .role("USER")
                .build();
        question.getAnswers().add(UserAnswer.builder().id(1L).answerText("First").build());
        question.getAnswers().add(UserAnswer.builder().id(2L).answerText("Second").build());

        when(questionRepository.findByIdWithDeps(9L)).thenReturn(Optional.of(question));

        Optional<SimpleQuestion> result = service.findByIdWithDeps(9L);

        assertThat(result)
                .isPresent()
                .get()
                .extracting(SimpleQuestion::getAnswer, SimpleQuestion::getAuthor)
                .containsExactly("Second", "Author");
    }
}
