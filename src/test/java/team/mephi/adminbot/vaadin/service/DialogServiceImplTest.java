package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.function.SerializableConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.dialogs.SimpleConfirmDialog;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для DialogServiceImpl.
 * Покрывают: вызовы фабрики и обработку подтверждения.
 */
@ExtendWith(MockitoExtension.class)
class DialogServiceImplTest {
    @Mock
    private DialogFactory dialogFactory;
    @Mock
    private DialogWithTitle dialogWithTitle;
    @Mock
    private SimpleConfirmDialog confirmDialog;
    @Mock
    private Icon icon;

    /**
     * Проверяет вызов диалога через фабрику.
     */
    @Test
    void Given_itemAndType_When_showDialog_Then_delegatesToDialog() {
        // Arrange
        DialogServiceImpl<String> service = new DialogServiceImpl<>(dialogFactory);
        SerializableConsumer<String> callback = item -> {
        };
        Object item = "item";
        DialogType type = DialogType.CREATE;
        when(dialogFactory.getDialog(eq(type))).thenReturn(dialogWithTitle);

        // Act
        service.showDialog(item, type, callback);

        // Assert
        verify(dialogWithTitle).showDialog(eq(item), eq(callback));
    }

    /**
     * Проверяет передачу подтверждения в колбэк.
     */
    @Test
    void Given_confirmDialog_When_showConfirmDialog_Then_acceptsItemOnConfirm() {
        // Arrange
        DialogServiceImpl<String> service = new DialogServiceImpl<>(dialogFactory);
        SerializableConsumer<String> callback = org.mockito.Mockito.mock(SerializableConsumer.class);
        Object item = "payload";
        DialogType type = DialogType.DELETE;
        when(dialogFactory.getConfirmDialog(eq(type), eq(icon))).thenReturn(confirmDialog);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        service.showConfirmDialog(item, type, icon, callback);
        verify(confirmDialog).showForConfirm(eq(item), runnableCaptor.capture());
        runnableCaptor.getValue().run();

        // Assert
        verify(callback).accept(eq("payload"));
    }
}
