package team.mephi.adminbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.model.MailTemplate;
import team.mephi.adminbot.repository.MailTemplateRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link TemplateServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class TemplateServiceImplTest {
    @Mock
    private MailTemplateRepository mailTemplateRepository;

    private TemplateServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TemplateServiceImpl(mailTemplateRepository);
    }

    /**
     * Проверяет обновление полей шаблона при сохранении.
     */
    @Test
    void givenExistingTemplate_WhenSaveCalled_ThenFieldsUpdated() {
        // Arrange
        MailTemplate existing = MailTemplate.builder().id(8L).name("Old").build();
        when(mailTemplateRepository.findById(8L)).thenReturn(Optional.of(existing));
        when(mailTemplateRepository.save(any(MailTemplate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SimpleTemplate input = SimpleTemplate.builder().id(8L).name("Welcome").text("Hello").build();

        // Act
        SimpleTemplate result = service.save(input);

        // Assert
        ArgumentCaptor<MailTemplate> captor = ArgumentCaptor.forClass(MailTemplate.class);
        verify(mailTemplateRepository).save(captor.capture());
        MailTemplate saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Welcome");
        assertThat(saved.getBodyText()).isEqualTo("Hello");
        assertThat(result.getName()).isEqualTo("Welcome");
    }
}
