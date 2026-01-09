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
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для MailingController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class MailingControllerApiTest {

    @Mock
    private MailingRepository mailingRepository;

    @InjectMocks
    private MailingController mailingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(mailingController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getMailings_shouldReturnBroadcastsViewWithModelAttributes() throws Exception {
        // given
        List<Mailing> mailings = List.of(
                Mailing.builder().id(1L).name("Msg1").build(),
                Mailing.builder().id(2L).name("Msg2").build()
        );
        when(mailingRepository.findAllByOrderByCreatedAtDesc()).thenReturn(mailings);

        // when / then
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isOk())
                .andExpect(view().name("broadcasts"))
                .andExpect(model().attributeExists("broadcasts"))
                .andExpect(model().attributeExists("newBroadcast"))
                .andExpect(model().attribute("currentUri", "broadcasts"));

        verify(mailingRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void createMailing_shouldSaveMailingAndRedirect() throws Exception {
        // given
        ArgumentCaptor<Mailing> captor = ArgumentCaptor.forClass(Mailing.class);

        // when / then
        mockMvc.perform(post("/broadcasts")
                        .param("name", "Тестовая рассылка"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/broadcasts"));

        verify(mailingRepository).save(captor.capture());

        Mailing saved = captor.getValue();
        assertNotNull(saved, "Сохранённая рассылка не должна быть null");
        assertEquals("Тестовая рассылка", saved.getName(), "name должен пробиндиться из формы");
    }
}
