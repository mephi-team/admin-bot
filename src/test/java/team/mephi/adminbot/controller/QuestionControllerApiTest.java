package team.mephi.adminbot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.QuestionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для QuestionController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class QuestionControllerApiTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionController questionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(questionController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getQuestions_shouldReturnQuestionsViewWithModelAttributes() throws Exception {
        // given
        List<Question> questions = List.of(
                Question.builder().id(1L).questionText("Q1").answerText("A1").build(),
                Question.builder().id(2L).questionText("Q2").answerText("A2").build()
        );
        when(questionRepository.findAll()).thenReturn(questions);

        // when / then
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(view().name("questions"))
                .andExpect(model().attributeExists("questions"))
                .andExpect(model().attributeExists("newQuestion"))
                .andExpect(model().attribute("currentUri", "questions"));

        verify(questionRepository).findAll();
    }

    @Test
    void addQuestion_shouldSaveQuestionAndRedirect() throws Exception {
        // given
        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);

        // when / then
        mockMvc.perform(post("/questions")
                        .param("questionText", "Как подать документы?")
                        .param("answerText", "Заполните форму на сайте."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/questions"));

        verify(questionRepository).save(captor.capture());
        Question saved = captor.getValue();

        assertNotNull(saved, "Сохранённый вопрос не должен быть null");
        assertEquals("Как подать документы?", saved.getQuestionText());
        assertEquals("Заполните форму на сайте.", saved.getAnswerText());
    }
}
