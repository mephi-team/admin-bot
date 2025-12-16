package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Юнит-тесты для сущности MailTemplate.
 * <p>
 * Проверяет корректность работы equals()/hashCode() и базовую функциональность сущности.
 * Примечание: тестирование @CreationTimestamp и @UpdateTimestamp требует интеграционных тестов
 * с реальной базой данных, так как эти аннотации обрабатываются Hibernate при сохранении.
 */
class MailTemplateTest {

    @Test
    void equals_shouldReturnTrueForSameId() {
        // given
        MailTemplate template1 = MailTemplate.builder()
                .id(1L)
                .name("Template 1")
                .subject("Subject 1")
                .build();

        MailTemplate template2 = MailTemplate.builder()
                .id(1L)
                .name("Template 2")
                .subject("Subject 2")
                .build();

        // then
        assertEquals(template1, template2, "Шаблоны с одинаковым ID должны быть равны");
        assertEquals(template1.hashCode(), template2.hashCode(), "HashCode должен совпадать для одинаковых ID");
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        // given
        MailTemplate template1 = MailTemplate.builder()
                .id(1L)
                .name("Template")
                .subject("Subject")
                .build();

        MailTemplate template2 = MailTemplate.builder()
                .id(2L)
                .name("Template")
                .subject("Subject")
                .build();

        // then
        assertNotEquals(template1, template2, "Шаблоны с разными ID не должны быть равны");
    }

    @Test
    void equals_shouldReturnFalseForNullId() {
        // given
        MailTemplate template1 = MailTemplate.builder()
                .id(null)
                .name("Template")
                .subject("Subject")
                .build();

        MailTemplate template2 = MailTemplate.builder()
                .id(null)
                .name("Template")
                .subject("Subject")
                .build();

        // then
        assertNotEquals(template1, template2, "Шаблоны с null ID не должны быть равны");
    }

    @Test
    void equals_shouldReturnTrueForSameInstance() {
        // given
        MailTemplate template = MailTemplate.builder()
                .id(1L)
                .name("Template")
                .subject("Subject")
                .build();

        // then
        assertEquals(template, template, "Объект должен быть равен самому себе");
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        // given
        MailTemplate template = MailTemplate.builder()
                .id(1L)
                .name("Template")
                .subject("Subject")
                .build();

        Object other = new Object();

        // then
        assertNotEquals(template, other, "Шаблон не должен быть равен объекту другого класса");
    }
}
