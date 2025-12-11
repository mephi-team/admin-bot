package team.mephi.adminbot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для BroadcastController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class MailingControllerTest {

    @Mock
    private MailingRepository mailingRepository;

    @InjectMocks
    private MailingController broadcastController;

    @Test
    void broadcastsPage_shouldLoadBroadcastsAndPopulateModel() {
        // given
        List<Mailing> broadcasts = List.of(
                Mailing.builder().build(),
                Mailing.builder().build()
        );
        when(mailingRepository.findAllByOrderByCreatedAtDesc()).thenReturn(broadcasts);

        Model model = new ExtendedModelMap();

        // when
        String viewName = broadcastController.broadcastsPage(model);

        // then
        assertEquals("broadcasts", viewName);

        verify(mailingRepository).findAllByOrderByCreatedAtDesc();

        assertSame(broadcasts, model.getAttribute("broadcasts"));
        assertNotNull(model.getAttribute("newBroadcast"));
        assertEquals("broadcasts", model.getAttribute("currentUri"));
    }

    @Test
    void createBroadcast_shouldSaveBroadcastAndRedirect() {
        // given
        Mailing broadcast = Mailing.builder().build();

        // when
        String viewName = broadcastController.createBroadcast(broadcast);

        // then
        assertEquals("redirect:/broadcasts", viewName);
        verify(mailingRepository).save(broadcast);
    }
}
