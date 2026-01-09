package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.repository.UserQuestionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для QuestionController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    private UserQuestionRepository questionRepository;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void questionsPage_shouldLoadQuestionsAndPopulateModel() {
        // given
        List<UserQuestion> questions = List.of(
                UserQuestion.builder().build(),
                UserQuestion.builder().build()
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
        UserQuestion question = UserQuestion.builder().build();

        // when
        String viewName = questionController.addQuestion(question);

        // then
        assertEquals("redirect:/questions", viewName);
        verify(questionRepository).save(question);
    }
}
