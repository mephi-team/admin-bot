package team.mephi.adminbot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.UserRepository;

/**
 * API-тесты для MainController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class MainControllerApiTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DialogRepository dialogRepository;

//    @Mock
//    private QuestionRepository questionRepository;

    @InjectMocks
    private MainController mainController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(mainController)
                .setViewResolvers(viewResolver)
                .build();
    }

//    @Test
//    void getMainPage_shouldReturnMainViewWithDashboardMetrics() throws Exception {
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
//        // when / then
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("main"))
//                .andExpect(model().attribute("totalUsers", expectedTotalUsers))
//                .andExpect(model().attribute("activeDialogs", expectedActiveDialogs))
//                .andExpect(model().attribute("newQuestions", expectedNewQuestions));
//
//        verify(userRepository).count();
//        verify(dialogRepository).countByLastMessageAtAfter(any(Instant.class));
//        verify(questionRepository).countByCreatedAtAfter(any(LocalDateTime.class));
//    }
}
