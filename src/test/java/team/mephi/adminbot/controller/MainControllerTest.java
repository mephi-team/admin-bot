package team.mephi.adminbot.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.UserRepository;

/**
 * Юнит-тесты для MainController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DialogRepository dialogRepository;

//    @Mock
//    private QuestionRepository questionRepository;

    @InjectMocks
    private MainController mainController;

//    @Test
//    void mainPage_shouldPopulateDashboardMetrics() {
//        // given
//        long expectedTotalUsers = 10L;
//        long expectedActiveDialogs = 5L;
//        long expectedNewQuestions = 3L;
//
//        when(userRepository.count()).thenReturn(expectedTotalUsers);
//        when(dialogRepository.countByLastMessageAtAfter(any(Instant.class)))
//                .thenReturn(expectedActiveDialogs);
//        when(questionRepository.countByCreatedAtAfter(any(LocalDateTime.class)))
//                .thenReturn(expectedNewQuestions);
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        String viewName = mainController.mainPage(model);
//
//        // then
//        assertEquals("main", viewName);
//
//        verify(userRepository).count();
//        verify(dialogRepository).countByLastMessageAtAfter(any(Instant.class));
//        verify(questionRepository).countByCreatedAtAfter(any(LocalDateTime.class));
//
//        assertEquals(expectedTotalUsers, model.getAttribute("totalUsers"));
//        assertEquals(expectedActiveDialogs, model.getAttribute("activeDialogs"));
//        assertEquals(expectedNewQuestions, model.getAttribute("newQuestions"));
//    }
}
