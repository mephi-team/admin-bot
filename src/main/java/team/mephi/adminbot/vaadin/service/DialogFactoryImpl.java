package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.core.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.dialogs.SimpleConfirmDialog;
import team.mephi.adminbot.vaadin.mailings.components.MailingEditorDialogFactory;
import team.mephi.adminbot.vaadin.mailings.components.TemplateEditorDialogFactory;
import team.mephi.adminbot.vaadin.questions.components.AnswerDialogFactory;
import team.mephi.adminbot.vaadin.users.components.BlockDialogFactory;
import team.mephi.adminbot.vaadin.users.components.TutorEditorDialogFactory;
import team.mephi.adminbot.vaadin.users.components.TutoringDialogFactory;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Реализация фабрики для создания диалогов различных типов.
 */
@Service
public class DialogFactoryImpl implements DialogFactory {
    private final Map<DialogType, Supplier<DialogWithTitle>> registry = new HashMap<>();

    /**
     * Конструктор фабрики диалогов, регистрирующий различные типы диалогов.
     *
     * @param userEditorDialogFactory  Фабрика для создания диалогов редактирования пользователей.
     * @param tutorEditorDialogFactory Фабрика для создания диалогов редактирования тьюторов.
     * @param blockDialogFactory       Фабрика для создания диалогов блокировки.
     * @param tutoringDialogFactory    Фабрика для создания диалогов обновления тьюторов.
     * @param mailingDialogFactory     Фабрика для создания диалогов рассылок.
     * @param templateDialogFactory    Фабрика для создания диалогов шаблонов.
     * @param answerDialogFactory      Фабрика для создания диалогов ответов.
     */
    public DialogFactoryImpl(
            UserEditorDialogFactory userEditorDialogFactory,
            TutorEditorDialogFactory tutorEditorDialogFactory,
            BlockDialogFactory blockDialogFactory,
            TutoringDialogFactory tutoringDialogFactory,
            MailingEditorDialogFactory mailingDialogFactory,
            TemplateEditorDialogFactory templateDialogFactory,
            AnswerDialogFactory answerDialogFactory
    ) {
        registry.put(DialogType.USERS_CREATED, userEditorDialogFactory::create);
        registry.put(DialogType.TUTORS_CREATED, tutorEditorDialogFactory::create);
        registry.put(DialogType.USERS_VIEW, userEditorDialogFactory::create);
        registry.put(DialogType.USERS_EDIT, userEditorDialogFactory::create);
        registry.put(DialogType.USERS_BLOCKED, () -> blockDialogFactory.create(SimpleUser.class));
        registry.put(DialogType.TUTORS_BLOCKED, () -> blockDialogFactory.create(SimpleTutor.class));
        registry.put(DialogType.TUTORS_VIEW, tutorEditorDialogFactory::create);
        registry.put(DialogType.TUTORS_UPDATED, tutoringDialogFactory::create);
        registry.put(DialogType.TUTORS_EDIT, tutorEditorDialogFactory::create);
        registry.put(DialogType.SENT_CREATED, mailingDialogFactory::create);
        registry.put(DialogType.TEMPLATES_CREATED, templateDialogFactory::create);
        registry.put(DialogType.DRAFT_CREATED, mailingDialogFactory::create);
        registry.put(DialogType.MAILING_SAVED, mailingDialogFactory::create);
        registry.put(DialogType.TEMPLATE_SAVED, templateDialogFactory::create);
        registry.put(DialogType.ANSWER_SEND, answerDialogFactory::create);
    }

    @Override
    public DialogWithTitle getDialog(DialogType type) {
        var supplier = registry.get(type);
        if (supplier == null) throw new IllegalArgumentException("Dialog not registered: " + type);
        var dialog = supplier.get();
        dialog.setHeaderTitle(type.getDialogTitleKey());
        return dialog;
    }

    @Override
    public SimpleConfirmDialog getConfirmDialog(DialogType type, Icon icon) {
        var name = type.name().toLowerCase();
        return new SimpleConfirmDialog("dialog_" + name + "_title", "dialog_" + name + "_text", "dialog_" + name + "_action", icon, null);
    }
}
