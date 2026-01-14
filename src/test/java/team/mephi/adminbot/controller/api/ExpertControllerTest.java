package team.mephi.adminbot.controller.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import team.mephi.adminbot.config.TestSecurityConfig;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.model.enums.QuestionStatus;
import team.mephi.adminbot.repository.UserQuestionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для ExpertController.
 * Покрывают: доступ по ролям (LC_EXPERT), статус-коды, базовые ответы и частичное обновление вопроса.
 */
@WebMvcTest(controllers = ExpertController.class)
@Import(TestSecurityConfig.class)
class ExpertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserQuestionRepository questionRepository;

    @Test
    void Given_expertRole_When_getAllQuestions_Then_returnsOkAndList() throws Exception {
        // Arrange
        UserQuestion q1 = UserQuestion.builder()
                .id(1L)
                .role("STUDENT")
                .text("Question 1")
                .status(QuestionStatus.NEW)
                .build();

        when(questionRepository.findAll()).thenReturn(List.of(q1));

        // Act + Assert
        mockMvc.perform(get("/api/expert/questions")
                        .with(authentication(expertAuth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Question 1"))
                .andExpect(jsonPath("$[0].status").value("NEW"));

        verify(questionRepository).findAll();
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    void Given_existingQuestion_When_getQuestionById_Then_returnsOk() throws Exception {
        // Arrange
        long id = 10L;
        UserQuestion q = UserQuestion.builder()
                .id(id)
                .role("STUDENT")
                .text("Some question")
                .status(QuestionStatus.IN_PROGRESS)
                .build();

        when(questionRepository.findById(id)).thenReturn(Optional.of(q));

        // Act + Assert
        mockMvc.perform(get("/api/expert/questions/{id}", id)
                        .with(authentication(expertAuth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.text").value("Some question"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(questionRepository).findById(id);
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    void Given_missingQuestion_When_getQuestionById_Then_returnsNotFound() throws Exception {
        // Arrange
        long id = 999_999L;
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        mockMvc.perform(get("/api/expert/questions/{id}", id)
                        .with(authentication(expertAuth())))
                .andExpect(status().isNotFound());

        verify(questionRepository).findById(id);
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    void Given_existingQuestion_When_updateQuestionWithNewText_Then_updatesOnlyTextAndReturnsOk() throws Exception {
        // Arrange
        long id = 7L;

        UserQuestion existing = UserQuestion.builder()
                .id(id)
                .role("STUDENT")
                .text("Old text")
                .status(QuestionStatus.NEW)
                .build();

        when(questionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(questionRepository.save(any(UserQuestion.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
                { "text": "New text" }
                """;

        // Act + Assert
        mockMvc.perform(put("/api/expert/questions/{id}", id)
                        .with(authentication(expertAuth()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.text").value("New text"))
                .andExpect(jsonPath("$.status").value("NEW"));

        verify(questionRepository).findById(id);
        verify(questionRepository).save(eq(existing));
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    void Given_missingQuestion_When_updateQuestion_Then_returnsNotFound() throws Exception {
        // Arrange
        long id = 123_456L;
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        String body = """
                { "text": "Does not matter" }
                """;

        // Act + Assert
        mockMvc.perform(put("/api/expert/questions/{id}", id)
                        .with(authentication(expertAuth()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

        verify(questionRepository).findById(id);
        verify(questionRepository, never()).save(any());
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    void Given_expertRole_When_getExpertStats_Then_returnsOkAndTotalQuestionsAndTimestamp() throws Exception {
        // Arrange
        when(questionRepository.count()).thenReturn(12L);

        // Act + Assert
        mockMvc.perform(get("/api/expert/stats")
                        .with(authentication(expertAuth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalQuestions").value(12))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(questionRepository).count();
        verifyNoMoreInteractions(questionRepository);
    }

    @ParameterizedTest
    @MethodSource("expertEndpoints")
    void Given_userRole_When_requestingExpertEndpoints_Then_returnsForbidden(HttpMethod method, String uri, String body) throws Exception {
        mockMvc.perform(withBodyIfNeeded(request(method, uri), body)
                        .with(authentication(userAuth())))
                .andExpect(status().isForbidden());

        verifyNoInteractions(questionRepository);
    }

    @ParameterizedTest
    @MethodSource("expertEndpoints")
    void Given_noAuthentication_When_requestingExpertEndpoints_Then_returnsUnauthorized(HttpMethod method, String uri, String body) throws Exception {
        mockMvc.perform(withBodyIfNeeded(request(method, uri), body))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(questionRepository);
    }

    /**
     * ВАЖНО: для PUT обязательно отдаём body, иначе до проверки @PreAuthorize
     * не дойдёт (упадёт на биндинге @RequestBody) и получишь 500.
     */
    private static Stream<Arguments> expertEndpoints() {
        return Stream.of(
                Arguments.of(HttpMethod.GET, "/api/expert/questions", null),
                Arguments.of(HttpMethod.GET, "/api/expert/questions/1", null),
                Arguments.of(HttpMethod.PUT, "/api/expert/questions/1", """
                        { "text": "any" }
                        """),
                Arguments.of(HttpMethod.GET, "/api/expert/stats", null)
        );
    }

    private static MockHttpServletRequestBuilder withBodyIfNeeded(MockHttpServletRequestBuilder builder, String body) {
        if (body == null) {
            return builder;
        }
        return builder.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    /**
     * Без JwtAuthenticationToken / oauth2-resource-server классов.
     * Нам важно только наличие authorities для @PreAuthorize.
     */
    private static AbstractAuthenticationToken expertAuth() {
        return new SimpleAuthToken(List.of(new SimpleGrantedAuthority("ROLE_LC_EXPERT")));
    }

    private static AbstractAuthenticationToken userAuth() {
        return new SimpleAuthToken(List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private static final class SimpleAuthToken extends AbstractAuthenticationToken {
        private final Object principal = "test-user";

        private SimpleAuthToken(List<SimpleGrantedAuthority> authorities) {
            super(authorities);
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return "";
        }

        @Override
        public Object getPrincipal() {
            return principal;
        }
    }
}
