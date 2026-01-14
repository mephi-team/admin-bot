package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.SerializableConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.dialogs.SimpleConfirmDialog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DialogServiceImplTest {
    @Mock
    private DialogFactory dialogFactory;

    @Mock
    private DialogWithTitle dialogWithTitle;

    @Mock
    private SimpleConfirmDialog confirmDialog;

    @Mock
    private SerializableConsumer<String> callback;

    @Test
    void showDialogDelegatesToFactoryDialog() {
        when(dialogFactory.getDialog(eq(DialogType.USERS_CREATED))).thenReturn(dialogWithTitle);
        DialogServiceImpl<String> service = new DialogServiceImpl<>(dialogFactory);

        service.showDialog("item", DialogType.USERS_CREATED, callback);

        verify(dialogWithTitle).showDialog("item", callback);
    }

    @Test
    void showConfirmDialogRunsCallbackOnConfirm() {
        Icon icon = VaadinIcon.CHECK.create();
        when(dialogFactory.getConfirmDialog(eq(DialogType.DELETE_USERS), eq(icon))).thenReturn(confirmDialog);
        DialogServiceImpl<String> service = new DialogServiceImpl<>(dialogFactory);

        service.showConfirmDialog("item", DialogType.DELETE_USERS, icon, callback);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(confirmDialog).showForConfirm(eq("item"), runnableCaptor.capture());

        runnableCaptor.getValue().run();
        verify(callback).accept("item");
    }
}
