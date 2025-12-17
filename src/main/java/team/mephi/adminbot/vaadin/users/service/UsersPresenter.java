package team.mephi.adminbot.vaadin.users.service;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.List;

public class UsersPresenter implements UserActions {
    private final UserViewCallback view;
    private final UserDataProvider dataProvider;

    // --- Константы текстов ---
    private static final String BLOCK_MESSAGE = "Пользователь заблокирован";
    private static final String BLOCK_ALL_MESSAGE = "Заблокировано %d пользователей";

    private static final String ACCEPT_MESSAGE = "Информация о приеме отправлена";
    private static final String ACCEPT_ALL_MESSAGE = "Информация о приеме отправлена %d кандидатам";

    private static final String REJECT_MESSAGE = "Информация об отказе отправлена кандидату";
    private static final String REJECT_ALL_MESSAGE = "Информация об отказе отправлена %d кандидатам";

    public UsersPresenter(
            UserDataProvider dataProvider,
            UserViewCallback view
    ) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onView(Long id) {
        dataProvider.findUserById(id).ifPresent(view::showUserEditorForView);
    }

    @Override
    public void onEdit(Long id) {
        dataProvider.findUserById(id).ifPresent(user -> {
            view.showUserEditorForEdit(user); // ← View открывает форму

            // Устанавливаем колбэк: что делать при нажатии "Сохранить"
            view.setOnSaveCallback(() -> {
                SimpleUser editedUser = view.getEditedUser(); // ← View отдаёт DTO
                if (editedUser != null) {
                    dataProvider.save(editedUser);
                    dataProvider.refresh();
                    view.showNotification("Пользователь сохранён");
                }
            });
        });
    }

    @Override
    public void onDelete(List<Long> ids) {
        view.confirmDelete(ids, () -> {
            dataProvider.deleteAllById(ids);
            dataProvider.refresh();
            view.showNotification(makeNotification(BLOCK_MESSAGE, BLOCK_ALL_MESSAGE, ids.size()));
        });
    }

    @Override
    public void onAccept(List<Long> ids) {
        view.confirmAccept(ids, () -> {
            view.showNotification(makeNotification(ACCEPT_MESSAGE, ACCEPT_ALL_MESSAGE, ids.size()));
        });
    }

    @Override
    public void onReject(List<Long> ids) {
        view.confirmReject(ids, () -> {
            view.showNotification(makeNotification(REJECT_MESSAGE, REJECT_ALL_MESSAGE, ids.size()));
        });
    }

    private String makeNotification(String single, String plural, int count) {
        if (count > 1) {
            return String.format(plural, count);
        }
        return single;
    }
}