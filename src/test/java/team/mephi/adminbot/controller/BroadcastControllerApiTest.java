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
import team.mephi.adminbot.model.Broadcast;
import team.mephi.adminbot.repository.BroadcastRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для BroadcastController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class BroadcastControllerApiTest {

    @Mock
    private BroadcastRepository broadcastRepository;

    @InjectMocks
    private BroadcastController broadcastController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(broadcastController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getBroadcasts_shouldReturnBroadcastsViewWithModelAttributes() throws Exception {
        // given
        List<Broadcast> broadcasts = List.of(
                Broadcast.builder().id(1L).messageText("Msg1").build(),
                Broadcast.builder().id(2L).messageText("Msg2").build()
        );
        when(broadcastRepository.findAllByOrderByCreatedAtDesc()).thenReturn(broadcasts);

        // when / then
        mockMvc.perform(get("/broadcasts"))
                .andExpect(status().isOk())
                .andExpect(view().name("broadcasts"))
                .andExpect(model().attributeExists("broadcasts"))
                .andExpect(model().attributeExists("newBroadcast"))
                .andExpect(model().attribute("currentUri", "broadcasts"));

        verify(broadcastRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void createBroadcast_shouldSaveBroadcastAndRedirect() throws Exception {
        // given
        ArgumentCaptor<Broadcast> captor = ArgumentCaptor.forClass(Broadcast.class);

        // when / then
        mockMvc.perform(post("/broadcasts")
                        .param("messageText", "Тестовая рассылка"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/broadcasts"));

        verify(broadcastRepository).save(captor.capture());

        Broadcast saved = captor.getValue();
        assertNotNull(saved, "Сохранённая рассылка не должна быть null");
        assertEquals("Тестовая рассылка", saved.getMessageText(), "messageText должен пробиндиться из формы");
    }
}
