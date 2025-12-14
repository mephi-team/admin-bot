package team.mephi.adminbot.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.repository.QuestionRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E-тесты для QuestionController (полный контекст + PostgreSQL Testcontainers).
 */
class QuestionControllerE2EIT extends BaseE2EIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        // given
        questionRepository.deleteAll();
    }

    @Test
    void addQuestion_thenGetQuestions_shouldContainNewQuestion() throws Exception {
        // given
        String q = "Как подать документы?";
        String a = "Заполните форму на сайте.";

        // when
        mockMvc.perform(post("/questions")
                        .param("questionText", q)
                        .param("answerText", a))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/questions"));

        // then
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(view().name("questions"))
                .andExpect(model().attributeExists("questions"))
                .andExpect(model().attributeExists("newQuestion"))
                .andExpect(model().attribute("currentUri", "questions"));
    }
}
