package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.dialog.Dialog;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.vaadin.SimpleDialog;
import team.mephi.adminbot.vaadin.components.SimpleConfirmDialog;
import team.mephi.adminbot.vaadin.mailings.components.MailingEditorDialogFactory;
import team.mephi.adminbot.vaadin.mailings.components.TemplateEditorDialogFactory;
import team.mephi.adminbot.vaadin.questions.components.AnswerDialogFactory;
import team.mephi.adminbot.vaadin.users.components.BlockDialogFactory;
import team.mephi.adminbot.vaadin.users.components.TutoringDialogFactory;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class DialogFactoryImpl implements DialogFactory {
    private final Map<String, Supplier<SimpleDialog>> registry = new HashMap<>();

    public DialogFactoryImpl(
            UserEditorDialogFactory userEditorDialogFactory,
            BlockDialogFactory blockDialogFactory,
            TutoringDialogFactory tutoringDialogFactory,
            MailingEditorDialogFactory mailingDialogFactory,
            TemplateEditorDialogFactory templateDialogFactory,
            AnswerDialogFactory answerDialogFactory
    ) {
        registry.put("users_created", userEditorDialogFactory::create);
        registry.put("users_view", userEditorDialogFactory::create);
        registry.put("users_edit", userEditorDialogFactory::create);
        registry.put("users_blocked", blockDialogFactory::create);
        registry.put("tutors_updated", tutoringDialogFactory::create);
        registry.put("sent_created", mailingDialogFactory::create);
        registry.put("templates_created", templateDialogFactory::create);
        registry.put("draft_created", mailingDialogFactory::create);
        registry.put("mailing_saved", mailingDialogFactory::create);
        registry.put("template_saved", templateDialogFactory::create);
        registry.put("answer_send", answerDialogFactory::create);
    }

    @Override
    public SimpleDialog getDialog(String name) {
        return registry.get(name).get();
    }

    @Override
    public SimpleConfirmDialog getConfirmDialog(String name) {
        return new SimpleConfirmDialog("dialog_" + name + "_title","dialog_" + name + "_text","dialog_" + name + "_action");
    }
}
