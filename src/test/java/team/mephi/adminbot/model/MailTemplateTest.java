package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности MailTemplate (проверка @PrePersist и @PreUpdate).
 */
class MailTemplateTest {

    @Test
    void onCreate_shouldSetCreatedAtAndUpdatedAt() {
        // given
        MailTemplate template = MailTemplate.builder()
                .name("Template")
                .subject("Subject")
                .build();

        // when
        template.onCreate();

        // then
        assertNotNull(template.getCreatedAt(), "createdAt должен быть установлен");
        assertNotNull(template.getUpdatedAt(), "updatedAt должен быть установлен");
        assertEquals(
                template.getCreatedAt(),
                template.getUpdatedAt(),
                "createdAt и updatedAt должны совпадать при создании"
        );
    }

    @Test
    void onUpdate_shouldUpdateOnlyUpdatedAt() throws InterruptedException {
        // given
        MailTemplate template = new MailTemplate();
        template.onCreate();

        LocalDateTime createdAtBefore = template.getCreatedAt();
        LocalDateTime updatedAtBefore = template.getUpdatedAt();

        Thread.sleep(50);

        // when
        template.onUpdate();

        // then
        assertEquals(
                createdAtBefore,
                template.getCreatedAt(),
                "createdAt не должен меняться при onUpdate"
        );
        assertTrue(
                template.getUpdatedAt().isAfter(updatedAtBefore),
                "updatedAt должен обновиться"
        );
    }
}
