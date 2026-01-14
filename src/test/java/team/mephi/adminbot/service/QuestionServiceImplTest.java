package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.model.enums.AnswerStatus;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для QuestionServiceImpl.
 * Покрывают: сохранение ответов и маппинг вопросов.
 */
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
    @Mock
    private DefaultOidcUser userInfo;

    /**
     * Проверяет сохранение ответа на вопрос.
     */
    @Test
    void Given_question_When_saveAnswer_Then_persistsUserAnswer() {
        // Arrange
        when(authService.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getEmail()).thenReturn("user@example.com");
        User author = User.builder().id(9L).build();
        when(userRepository.findByEmail(eq("user@example.com"))).thenReturn(Optional.of(author));
        when(answerRepository.save(any(UserAnswer.class))).thenAnswer(invocation -> invocation.getArgument(0, UserAnswer.class));
        QuestionServiceImpl service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);
        SimpleQuestion question = SimpleQuestion.builder().id(5L).answer("Answer").build();

        // Act
        SimpleQuestion result = service.saveAnswer(question);

        // Assert
        assertEquals(5L, result.getId());
        ArgumentCaptor<UserAnswer> captor = ArgumentCaptor.forClass(UserAnswer.class);
        verify(answerRepository).save(captor.capture());
        assertEquals(AnswerStatus.SENT, captor.getValue().getStatus());
        assertEquals("Answer", captor.getValue().getAnswerText());
        assertEquals(5L, captor.getValue().getQuestion().getId());
    }

    /**
     * Проверяет маппинг вопросов при поиске по тексту.
     */
    @Test
    void Given_questions_When_findAllByText_Then_mapsLatestAnswer() {
        // Arrange
        UserQuestion question = UserQuestion.builder()
                .id(2L)
                .text("Question")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .user(User.builder().id(1L).userName("User").build())
                .direction(Direction.builder().name("Math").build())
                .role("STUDENT")
                .answers(List.of(
                        UserAnswer.builder().id(1L).answerText("First").build(),
                        UserAnswer.builder().id(2L).answerText("Last").build()
                ))
                .build();
        when(questionRepository.findAllByText(eq("q"), eq(PageRequest.of(0, 1)))).thenReturn(List.of(question));
        QuestionServiceImpl service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);

        // Act
        List<SimpleQuestion> result = service.findAllByText("q", PageRequest.of(0, 1)).collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("Last", result.getFirst().getAnswer());
    }

    /**
     * Проверяет подсчёт вопросов по тексту.
     */
    @Test
    void Given_query_When_countByText_Then_returnsCount() {
        // Arrange
        when(questionRepository.countByText(eq("q"))).thenReturn(3);
        QuestionServiceImpl service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);

        // Act
        Integer result = service.countByText("q");

        // Assert
        assertEquals(3, result);
    }

    /**
     * Проверяет поиск вопроса с зависимостями.
     */
    @Test
    void Given_id_When_findByIdWithDeps_Then_mapsQuestion() {
        // Arrange
        UserQuestion question = UserQuestion.builder()
                .id(8L)
                .text("Text")
                .user(User.builder().id(4L).userName("User").build())
                .direction(Direction.builder().name("Bio").build())
                .role("STUDENT")
                .answers(List.of())
                .build();
        when(questionRepository.findByIdWithDeps(eq(8L))).thenReturn(Optional.of(question));
        QuestionServiceImpl service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);

        // Act
        Optional<SimpleQuestion> result = service.findByIdWithDeps(8L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("", result.get().getAnswer());
    }

    /**
     * Проверяет удаление вопросов по идентификаторам.
     */
    @Test
    void Given_ids_When_deleteAllById_Then_callsRepository() {
        // Arrange
        QuestionServiceImpl service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);
        List<Long> ids = List.of(1L, 2L);

        // Act
        service.deleteAllById(ids);

        // Assert
        verify(questionRepository).deleteAllById(eq(ids));
    }

    /**
     * Проверяет подсчёт новых вопросов.
     */
    @Test
    void Given_repository_When_countNewQuestion_Then_returnsCount() {
        // Arrange
        when(questionRepository.countNewQuestion()).thenReturn(7);
        QuestionServiceImpl service = new QuestionServiceImpl(authService, userRepository, questionRepository, answerRepository);

        // Act
        Integer result = service.countNewQuestion();

        // Assert
        assertEquals(7, result);
    }
}
