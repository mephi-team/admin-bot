package team.mephi.adminbot.init;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.model.User;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для DataInitializer (вспомогательная логика создания сообщений) без поднятия Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Test
    void createMessage_shouldFillAllRequiredFields() throws Exception {
        // given
        DataInitializer initializer = new DataInitializer();

        Dialog dialog = new Dialog();
        dialog.setId(1L);

        User sender = new User();
        sender.setId(10L);

        String text = "Привет, это тестовое сообщение";
        String senderType = "user";
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);

        Method method = DataInitializer.class.getDeclaredMethod(
                "createMessage",
                team.mephi.adminbot.model.Dialog.class,
                team.mephi.adminbot.model.User.class,
                String.class,
                String.class,
                LocalDateTime.class
        );
        method.setAccessible(true);

        // when
        Message result = (Message) method.invoke(initializer, dialog, sender, text, senderType, createdAt);

        // then
        assertNotNull(result, "Созданное сообщение не должно быть null");

        assertEquals(dialog, result.getDialog(), "Диалог должен быть установлен");
        assertEquals(sender, result.getSender(), "Отправитель должен быть установлен");
        assertEquals(text, result.getText(), "Текст сообщения должен совпадать");
        assertEquals(senderType, result.getSenderType(), "Тип отправителя должен совпадать");

        assertEquals("active", result.getStatus(), "Статус сообщения по умолчанию должен быть 'active'");
        assertEquals(createdAt, result.getCreatedAt(), "createdAt должен совпадать с переданным значением");

        assertNotNull(result.getUpdatedAt(), "updatedAt не должен быть null");
    }
}
