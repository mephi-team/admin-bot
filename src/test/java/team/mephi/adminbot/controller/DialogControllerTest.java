package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import team.mephi.adminbot.dto.MessagesGroup;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.repository.DialogRepository;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для DialogController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class DialogControllerTest {

    @Mock
    private DialogRepository dialogRepository;

    @InjectMocks
    private DialogController dialogController;

    @SuppressWarnings("unchecked")
    private List<MessagesGroup> invokeGroupMessagesByDate(List<Message> messages) throws Exception {
        Method method = DialogController.class.getDeclaredMethod("groupMessagesByDate", List.class);
        method.setAccessible(true);
        return (List<MessagesGroup>) method.invoke(dialogController, messages);
    }

//    private Message createMessage(LocalDateTime createdAt, long id) {
//        Message message = new Message();
//        message.setId(id);
//        message.setCreatedAt(createdAt);
//        return message;
//    }

    private Dialog createDialogWithMessages(Long id, List<Message> messages) {
        Dialog dialog = new Dialog();
        dialog.setId(id);
        dialog.setMessages(messages);
        return dialog;
    }

    @Test
    void dialogsPage_whenNoSearch_shouldLoadAllDialogsAndSetModelAttributes() {
        // given
        List<Dialog> dialogs = List.of(new Dialog(), new Dialog());
        when(dialogRepository.findAllWithUsers()).thenReturn(dialogs);

        Model model = new ExtendedModelMap();

        // when
        String viewName = dialogController.dialogsPage(null, model);

        // then
        assertEquals("dialogs/list", viewName);

        verify(dialogRepository).findAllWithUsers();
        verify(dialogRepository, never()).searchByUserName(anyString());

        assertSame(dialogs, model.getAttribute("dialogs"));
        assertEquals("", model.getAttribute("searchQuery"));
        assertNotNull(model.getAttribute("today"));
        assertEquals("dialogs", model.getAttribute("currentUri"));
    }

    @Test
    void dialogsPage_whenSearchIsBlank_shouldFallbackToFindAllWithUsers() {
        // given
        String search = "   ";
        List<Dialog> dialogs = List.of(new Dialog(), new Dialog(), new Dialog());
        when(dialogRepository.findAllWithUsers()).thenReturn(dialogs);

        Model model = new ExtendedModelMap();

        // when
        String viewName = dialogController.dialogsPage(search, model);

        // then
        assertEquals("dialogs/list", viewName);

        verify(dialogRepository).findAllWithUsers();
        verify(dialogRepository, never()).searchByUserName(anyString());

        assertSame(dialogs, model.getAttribute("dialogs"));
        // ВАЖНО: контроллер кладёт searchQuery как есть, без trim
        assertEquals(search, model.getAttribute("searchQuery"));
        assertNotNull(model.getAttribute("today"));
        assertEquals("dialogs", model.getAttribute("currentUri"));
    }

    @Test
    void dialogsPage_whenSearchProvided_shouldUseSearchByUserName() {
        // given
        String search = "Вася";
        List<Dialog> dialogs = List.of(new Dialog());
        when(dialogRepository.searchByUserName(search)).thenReturn(dialogs);

        Model model = new ExtendedModelMap();

        // when
        String viewName = dialogController.dialogsPage(search, model);

        // then
        assertEquals("dialogs/list", viewName);

        verify(dialogRepository).searchByUserName(search);
        verify(dialogRepository, never()).findAllWithUsers();

        assertSame(dialogs, model.getAttribute("dialogs"));
        assertEquals(search, model.getAttribute("searchQuery"));
        assertNotNull(model.getAttribute("today"));
        assertEquals("dialogs", model.getAttribute("currentUri"));
    }

//    @Test
//    void viewDialog_whenNoSearch_shouldLoadDialogsAndDialogAndMessageGroups() {
//        // given
//        Long dialogId = 42L;
//
//        List<Dialog> dialogs = List.of(new Dialog(), new Dialog());
//        when(dialogRepository.findAllWithUsers()).thenReturn(dialogs);
//
//        List<Message> messages = List.of(
//                createMessage(LocalDateTime.now().minusHours(2), 1L),
//                createMessage(LocalDateTime.now(), 2L)
//        );
//        Dialog dialog = createDialogWithMessages(dialogId, messages);
//        when(dialogRepository.findById(dialogId)).thenReturn(Optional.of(dialog));
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        String viewName = dialogController.viewDialog(dialogId, null, model);
//
//        // then
//        assertEquals("dialogs/detail", viewName);
//
//        verify(dialogRepository).findAllWithUsers();
//        verify(dialogRepository).findById(dialogId);
//        verify(dialogRepository, never()).searchByUserName(anyString());
//
//        assertSame(dialogs, model.getAttribute("dialogs"));
//        assertSame(dialog, model.getAttribute("dialog"));
//
//        Object messageGroups = model.getAttribute("messageGroups");
//        assertNotNull(messageGroups);
//        assertTrue(messageGroups instanceof List);
//        assertFalse(((List<?>) messageGroups).isEmpty());
//
//        assertNotNull(model.getAttribute("today"));
//        assertEquals("dialogs", model.getAttribute("currentUri"));
//    }

//    @Test
//    void viewDialog_whenSearchProvided_shouldUseSearchByUserName() {
//        // given
//        Long dialogId = 100L;
//        String search = "Петя";
//
//        List<Dialog> dialogs = List.of(new Dialog());
//        when(dialogRepository.searchByUserName(search)).thenReturn(dialogs);
//
//        List<Message> messages = List.of(
//                createMessage(LocalDateTime.now().minusDays(1), 10L)
//        );
//        Dialog dialog = createDialogWithMessages(dialogId, messages);
//        when(dialogRepository.findById(dialogId)).thenReturn(Optional.of(dialog));
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        String viewName = dialogController.viewDialog(dialogId, search, model);
//
//        // then
//        assertEquals("dialogs/detail", viewName);
//
//        verify(dialogRepository).searchByUserName(search);
//        verify(dialogRepository, never()).findAllWithUsers();
//        verify(dialogRepository).findById(dialogId);
//
//        assertSame(dialogs, model.getAttribute("dialogs"));
//        assertSame(dialog, model.getAttribute("dialog"));
//
//        Object messageGroups = model.getAttribute("messageGroups");
//        assertNotNull(messageGroups);
//        assertTrue(messageGroups instanceof List);
//        assertFalse(((List<?>) messageGroups).isEmpty());
//
//        assertNotNull(model.getAttribute("today"));
//        assertEquals("dialogs", model.getAttribute("currentUri"));
//    }

//    @Test
//    void groupMessagesByDate_shouldGroupMessagesAndSetTodayYesterdayAndDateLabels() throws Exception {
//        // given
//        LocalDate today = LocalDate.now();
//        LocalDateTime todayMorning = today.atTime(10, 0);
//        LocalDateTime todayEvening = today.atTime(18, 30);
//
//        LocalDate yesterday = today.minusDays(1);
//        LocalDateTime yesterdayNoon = yesterday.atTime(12, 0);
//
//        LocalDate twoDaysAgo = today.minusDays(2);
//        LocalDateTime twoDaysAgoMorning = twoDaysAgo.atTime(9, 0);
//
//        List<Message> messages = Arrays.asList(
//                createMessage(yesterdayNoon, 1L),
//                createMessage(twoDaysAgoMorning, 2L),
//                createMessage(todayMorning, 3L),
//                createMessage(todayEvening, 4L)
//        );
//
//        // when
//        List<MessagesGroup> groups = invokeGroupMessagesByDate(messages);
//
//        // then
//        assertNotNull(groups);
//        assertEquals(3, groups.size());
//
//        MessagesGroup todayGroup = groups.stream()
//                .filter(g -> "Сегодня".equals(g.getDateLabel()))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(todayGroup);
//        assertEquals(2, todayGroup.getMessages().size());
//
//        MessagesGroup yesterdayGroup = groups.stream()
//                .filter(g -> "Вчера".equals(g.getDateLabel()))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(yesterdayGroup);
//        assertEquals(1, yesterdayGroup.getMessages().size());
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));
//        String expectedOldDateLabel = twoDaysAgo.format(formatter);
//
//        MessagesGroup oldDateGroup = groups.stream()
//                .filter(g -> expectedOldDateLabel.equals(g.getDateLabel()))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(oldDateGroup);
//        assertEquals(1, oldDateGroup.getMessages().size());
//    }

    @Test
    void groupMessagesByDate_withEmptyList_shouldReturnEmptyList() throws Exception {
        // given
        List<Message> messages = Collections.emptyList();

        // when
        List<MessagesGroup> groups = invokeGroupMessagesByDate(messages);

        // then
        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }
}
