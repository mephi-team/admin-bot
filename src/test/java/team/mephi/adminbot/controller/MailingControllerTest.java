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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для MailingController без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class MailingControllerTest {

    @Mock
    private MailingRepository mailingRepository;

    @InjectMocks
    private MailingController mailingController;

    @Test
    void broadcastsPage_shouldLoadMailingsAndPopulateModel() {
        // given
        List<Mailing> mailings = List.of(
                Mailing.builder().build(),
                Mailing.builder().build()
        );
        when(mailingRepository.findAllByOrderByCreatedAtDesc()).thenReturn(mailings);

        Model model = new ExtendedModelMap();

        // when
        String viewName = mailingController.broadcastsPage(model);

        // then
        assertEquals("broadcasts", viewName);

        verify(mailingRepository).findAllByOrderByCreatedAtDesc();

        assertSame(mailings, model.getAttribute("broadcasts"));
        assertNotNull(model.getAttribute("newBroadcast"));
        assertEquals("broadcasts", model.getAttribute("currentUri"));
    }

    @Test
    void createMailing_shouldSaveMailingAndRedirect() {
        // given
        Mailing mailing = Mailing.builder().build();

        // when
        String viewName = mailingController.createBroadcast(mailing);

        // then
        assertEquals("redirect:/broadcasts", viewName);
        verify(mailingRepository).save(mailing);
    }
}
