package team.mephi.adminbot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.repository.DialogRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API-тесты для DialogController c использованием MockMvc (без поднятия полного Spring-контекста).
 */
@ExtendWith(MockitoExtension.class)
class DialogControllerApiTest {

    @Mock
    private DialogRepository dialogRepository;

    @InjectMocks
    private DialogController dialogController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(dialogController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getDialogs_withoutSearch_shouldReturnDialogsListView() throws Exception {
        // given
        List<Dialog> dialogs = List.of(new Dialog(), new Dialog());
        when(dialogRepository.findAllWithUsers()).thenReturn(dialogs);

        // when / then
        mockMvc.perform(get("/dialogs"))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/list"))
                .andExpect(model().attributeExists("dialogs"))
                .andExpect(model().attribute("searchQuery", ""))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));

        verify(dialogRepository).findAllWithUsers();
        verify(dialogRepository, never()).searchByUserName(anyString());
    }

    @Test
    void getDialogs_withBlankSearch_shouldFallbackToFindAllWithUsers() throws Exception {
        // given
        String search = "   ";
        List<Dialog> dialogs = List.of(new Dialog(), new Dialog(), new Dialog());
        when(dialogRepository.findAllWithUsers()).thenReturn(dialogs);

        // when / then
        mockMvc.perform(get("/dialogs").param("search", search))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/list"))
                .andExpect(model().attributeExists("dialogs"))
                // ВАЖНО: контроллер кладёт searchQuery как есть, без trim
                .andExpect(model().attribute("searchQuery", search))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));

        verify(dialogRepository).findAllWithUsers();
        verify(dialogRepository, never()).searchByUserName(anyString());
    }

    @Test
    void getDialogs_withSearch_shouldUseSearchByUserName() throws Exception {
        // given
        String search = "Иван";
        List<Dialog> dialogs = List.of(new Dialog());
        when(dialogRepository.searchByUserName(search)).thenReturn(dialogs);

        // when / then
        mockMvc.perform(get("/dialogs").param("search", search))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/list"))
                .andExpect(model().attributeExists("dialogs"))
                .andExpect(model().attribute("searchQuery", search))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));

        verify(dialogRepository).searchByUserName(search);
        verify(dialogRepository, never()).findAllWithUsers();
    }

    @Test
    void viewDialog_withoutSearch_shouldLoadDialogsAndSelectedDialog() throws Exception {
        // given
        Long dialogId = 42L;

        List<Dialog> dialogs = List.of(new Dialog(), new Dialog());
        when(dialogRepository.findAllWithUsers()).thenReturn(dialogs);

        Dialog dialog = new Dialog();
        dialog.setId(dialogId);

        Message m1 = new Message();
        m1.setCreatedAt(LocalDateTime.now().minusHours(2));
        Message m2 = new Message();
        m2.setCreatedAt(LocalDateTime.now());
        dialog.setMessages(List.of(m1, m2));

        when(dialogRepository.findById(dialogId)).thenReturn(Optional.of(dialog));

        // when / then
        mockMvc.perform(get("/dialogs/{id}", dialogId))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/detail"))
                .andExpect(model().attributeExists("dialogs"))
                .andExpect(model().attributeExists("dialog"))
                .andExpect(model().attributeExists("messageGroups"))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));

        verify(dialogRepository).findAllWithUsers();
        verify(dialogRepository).findById(dialogId);
        verify(dialogRepository, never()).searchByUserName(anyString());
    }

    @Test
    void viewDialog_withSearch_shouldUseSearchAndLoadDialog() throws Exception {
        // given
        Long dialogId = 100L;
        String search = "Петя";

        List<Dialog> dialogs = List.of(new Dialog());
        when(dialogRepository.searchByUserName(search)).thenReturn(dialogs);

        Dialog dialog = new Dialog();
        dialog.setId(dialogId);

        Message m = new Message();
        m.setCreatedAt(LocalDateTime.now().minusDays(1));
        dialog.setMessages(List.of(m));

        when(dialogRepository.findById(dialogId)).thenReturn(Optional.of(dialog));

        // when / then
        mockMvc.perform(get("/dialogs/{id}", dialogId).param("search", search))
                .andExpect(status().isOk())
                .andExpect(view().name("dialogs/detail"))
                .andExpect(model().attributeExists("dialogs"))
                .andExpect(model().attributeExists("dialog"))
                .andExpect(model().attributeExists("messageGroups"))
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("currentUri", "dialogs"));

        verify(dialogRepository).searchByUserName(search);
        verify(dialogRepository, never()).findAllWithUsers();
        verify(dialogRepository).findById(dialogId);
    }
}
