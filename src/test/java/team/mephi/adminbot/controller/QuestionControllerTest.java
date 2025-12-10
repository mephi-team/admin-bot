package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.QuestionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для QuestionController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void questionsPage_shouldLoadQuestionsAndPopulateModel() {
        // given
        List<Question> questions = List.of(
                Question.builder().build(),
                Question.builder().build()
        );
        when(questionRepository.findAll()).thenReturn(questions);

        Model model = new ExtendedModelMap();

        // when
        String viewName = questionController.questionsPage(model);

        // then
        assertEquals("questions", viewName);

        verify(questionRepository).findAll();

        assertSame(questions, model.getAttribute("questions"));
        assertNotNull(model.getAttribute("newQuestion"));
        assertEquals("questions", model.getAttribute("currentUri"));
    }

    @Test
    void addQuestion_shouldSaveQuestionAndRedirect() {
        // given
        Question question = Question.builder().build();

        // when
        String viewName = questionController.addQuestion(question);

        // then
        assertEquals("redirect:/questions", viewName);
        verify(questionRepository).save(question);
    }
}
