package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.SimpleQuestion;

public class AnswerDialog extends Dialog {
    private final BeanValidationBinder<SimpleQuestion> binder = new BeanValidationBinder<>(SimpleQuestion.class);
    private final Button answerButton = new Button(getTranslation("dialog_answer_button"), VaadinIcon.PAPERPLANE_O.create(), e -> onAnswer());

    @Setter
    private SerializableRunnable onSaveCallback;

    public AnswerDialog() {
        var form = new AnswerForm();
        binder.bindInstanceFields(form);

        answerButton.setIconAfterText(true);

        setHeaderTitle("dialog_answer_title");
        add(form);
        getFooter().add(answerButton);

        binder.addStatusChangeListener(e ->
                answerButton.setEnabled(e.getBinder().isValid()));
    }

    public SimpleQuestion getEditedItem() {
        SimpleQuestion question = new SimpleQuestion();
        binder.writeBeanIfValid(question);
        return question;
    }

    private void onAnswer() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            close();
        }
    }

    public void showDialogForEdit(SimpleQuestion question) {
        binder.readBean(question);
        binder.setReadOnly(false);

        open();
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
