package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.model.MailTemplate;
import team.mephi.adminbot.repository.MailTemplateRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для TemplateServiceImpl.
 * Покрывают: сохранение и выборки шаблонов писем.
 */
@ExtendWith(MockitoExtension.class)
class TemplateServiceImplTest {
    @Mock
    private MailTemplateRepository mailTemplateRepository;

    /**
     * Проверяет маппинг списка шаблонов в DTO.
     */
    @Test
    void Given_templates_When_findAll_Then_mapsToSimple() {
        // Arrange
        MailTemplate template = new MailTemplate();
        template.setId(3L);
        template.setName("Welcome");
        template.setBodyText("Body");
        template.setCreatedAt(Instant.parse("2024-01-01T10:00:00Z"));
        when(mailTemplateRepository.findAll()).thenReturn(List.of(template));
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        List<SimpleTemplate> result = service.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Welcome", result.getFirst().getName());
        assertEquals("Body", result.getFirst().getText());
    }

    /**
     * Проверяет поиск шаблонов по имени с пагинацией.
     */
    @Test
    void Given_query_When_findAllPaged_Then_usesRepository() {
        // Arrange
        MailTemplate template = new MailTemplate();
        template.setId(1L);
        template.setName("Alpha");
        template.setBodyText("Text");
        when(mailTemplateRepository.findAllByName(eq("Alpha"), eq(PageRequest.of(0, 2))))
                .thenReturn(List.of(template));
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        List<SimpleTemplate> result = service.findAll(PageRequest.of(0, 2), "Alpha");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Alpha", result.getFirst().getName());
    }

    /**
     * Проверяет сохранение нового шаблона.
     */
    @Test
    void Given_newTemplate_When_save_Then_persistsAndReturnsDto() {
        // Arrange
        SimpleTemplate dto = SimpleTemplate.builder()
                .name("Name")
                .text("Content")
                .build();
        when(mailTemplateRepository.save(any(MailTemplate.class))).thenAnswer(invocation -> {
            MailTemplate saved = invocation.getArgument(0, MailTemplate.class);
            saved.setId(11L);
            return saved;
        });
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        SimpleTemplate result = service.save(dto);

        // Assert
        assertEquals(11L, result.getId());
        assertEquals("Name", result.getName());
        assertEquals("Content", result.getText());
    }

    /**
     * Проверяет обновление существующего шаблона.
     */
    @Test
    void Given_existingTemplate_When_save_Then_updatesFields() {
        // Arrange
        MailTemplate existing = new MailTemplate();
        existing.setId(5L);
        existing.setName("Old");
        when(mailTemplateRepository.findById(eq(5L))).thenReturn(Optional.of(existing));
        when(mailTemplateRepository.save(eq(existing))).thenReturn(existing);
        SimpleTemplate dto = SimpleTemplate.builder()
                .id(5L)
                .name("New")
                .text("Body")
                .build();
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        SimpleTemplate result = service.save(dto);

        // Assert
        assertEquals("New", result.getName());
        assertEquals("Body", result.getText());
        assertEquals("New", existing.getSubject());
    }

    /**
     * Проверяет поиск шаблона по идентификатору.
     */
    @Test
    void Given_id_When_findById_Then_mapsOptional() {
        // Arrange
        MailTemplate template = new MailTemplate();
        template.setId(7L);
        template.setName("Invite");
        template.setBodyText("Hello");
        when(mailTemplateRepository.findById(eq(7L))).thenReturn(Optional.of(template));
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        Optional<SimpleTemplate> result = service.findById(7L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Invite", result.get().getName());
    }

    /**
     * Проверяет стриминг шаблонов по имени.
     */
    @Test
    void Given_name_When_findAllByName_Then_streamsDtos() {
        // Arrange
        MailTemplate template = new MailTemplate();
        template.setId(2L);
        template.setName("Report");
        template.setBodyText("Body");
        when(mailTemplateRepository.findAllByName(eq("Report"), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of(template));
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        List<SimpleTemplate> result = service.findAllByName("Report", PageRequest.of(0, 1)).collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("Report", result.getFirst().getName());
    }

    /**
     * Проверяет подсчёт шаблонов по имени.
     */
    @Test
    void Given_name_When_countByName_Then_returnsCount() {
        // Arrange
        when(mailTemplateRepository.countByName(eq("Mail"))).thenReturn(4);
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);

        // Act
        Integer result = service.countByName("Mail");

        // Assert
        assertEquals(4, result);
    }

    /**
     * Проверяет удаление шаблонов по идентификаторам.
     */
    @Test
    void Given_ids_When_deleteAllById_Then_callsRepository() {
        // Arrange
        TemplateServiceImpl service = new TemplateServiceImpl(mailTemplateRepository);
        List<Long> ids = List.of(1L, 2L);

        // Act
        service.deleteAllById(ids);

        // Assert
        verify(mailTemplateRepository).deleteAllById(eq(ids));
    }
}
