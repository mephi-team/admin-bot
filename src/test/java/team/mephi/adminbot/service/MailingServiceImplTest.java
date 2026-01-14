package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.Channels;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для MailingServiceImpl.
 * Покрывают: сохранение рассылок и выборки по имени.
 */
@ExtendWith(MockitoExtension.class)
class MailingServiceImplTest {
    @Mock
    private AuthService authService;
    @Mock
    private MailingRepository mailingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DefaultOidcUser userInfo;

    /**
     * Проверяет сохранение рассылки с заполнением полей.
     */
    @Test
    void Given_newMailing_When_save_Then_setsDefaultsAndMaps() {
        // Arrange
        when(authService.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getEmail()).thenReturn("user@example.com");
        User creator = User.builder().id(1L).build();
        when(userRepository.findByEmail(eq("user@example.com"))).thenReturn(Optional.of(creator));
        when(mailingRepository.save(any(Mailing.class))).thenAnswer(invocation -> invocation.getArgument(0, Mailing.class));
        SimpleMailing dto = SimpleMailing.builder()
                .name("Mailing")
                .text("Text")
                .channels(Set.of("EMAIL"))
                .recipients(List.of("user1"))
                .build();
        MailingServiceImpl service = new MailingServiceImpl(authService, mailingRepository, userRepository);

        // Act
        SimpleMailing result = service.save(dto);

        // Assert
        assertEquals("Mailing", result.getName());
        assertEquals("DRAFT", result.getStatus());
    }

    /**
     * Проверяет поиск рассылки по идентификатору.
     */
    @Test
    void Given_id_When_findById_Then_mapsToDto() {
        // Arrange
        Mailing mailing = new Mailing();
        mailing.setId(4L);
        mailing.setName("Mail");
        mailing.setStatus(MailingStatus.DRAFT);
        mailing.setChannels(List.of(Channels.EMAIL));
        when(mailingRepository.findById(eq(4L))).thenReturn(Optional.of(mailing));
        MailingServiceImpl service = new MailingServiceImpl(authService, mailingRepository, userRepository);

        // Act
        Optional<SimpleMailing> result = service.findById(4L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Mail", result.get().getName());
    }

    /**
     * Проверяет выборку рассылок по имени и статусам.
     */
    @Test
    void Given_filters_When_findMailingByName_Then_mapsStream() {
        // Arrange
        Mailing mailing = new Mailing();
        mailing.setId(5L);
        mailing.setName("Alpha");
        mailing.setStatus(MailingStatus.DRAFT);
        mailing.setChannels(List.of(Channels.EMAIL));
        when(mailingRepository.findMailingByName(eq("Al"), eq(List.of("DRAFT")), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of(mailing));
        MailingServiceImpl service = new MailingServiceImpl(authService, mailingRepository, userRepository);

        // Act
        List<SimpleMailing> result = service.findMailingByName("Al", List.of("DRAFT"), PageRequest.of(0, 1))
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("Alpha", result.getFirst().getName());
    }

    /**
     * Проверяет подсчёт рассылок по имени.
     */
    @Test
    void Given_name_When_countByName_Then_returnsCount() {
        // Arrange
        when(mailingRepository.countByName(eq("Mail"), eq(List.of(MailingStatus.DRAFT)))).thenReturn(2);
        MailingServiceImpl service = new MailingServiceImpl(authService, mailingRepository, userRepository);

        // Act
        Integer result = service.countByName("Mail", List.of(MailingStatus.DRAFT));

        // Assert
        assertEquals(2, result);
    }

    /**
     * Проверяет удаление рассылок по идентификаторам.
     */
    @Test
    void Given_ids_When_deleteAllById_Then_callsRepository() {
        // Arrange
        MailingServiceImpl service = new MailingServiceImpl(authService, mailingRepository, userRepository);
        List<Long> ids = List.of(1L, 2L);

        // Act
        service.deleteAllById(ids);

        // Assert
        verify(mailingRepository).deleteAllById(eq(ids));
    }
}
