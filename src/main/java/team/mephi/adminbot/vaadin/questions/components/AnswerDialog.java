package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.vaadin.SimpleDialog;
import team.mephi.adminbot.vaadin.components.PrimaryButton;

public class AnswerDialog extends Dialog implements SimpleDialog {
    private final BeanValidationBinder<SimpleQuestion> binder = new BeanValidationBinder<>(SimpleQuestion.class);
    private final Button answerButton = new PrimaryButton( getTranslation("dialog_answer_button"), VaadinIcon.PAPERPLANE_O.create(), e -> onAnswer());

    private SerializableConsumer<SimpleQuestion> onSaveCallback;
    private SimpleQuestion question;

    public AnswerDialog() {
        var form = new AnswerForm();
        binder.bindInstanceFields(form);

        answerButton.setIconAfterText(true);

        setHeaderTitle("dialog_answer_title");
        add(form);
        setWidth("100%");
        setMaxWidth("500px");
        getFooter().add(answerButton);

        binder.addStatusChangeListener(e ->
                answerButton.setEnabled(e.getBinder().isValid()));
    }

    private void onAnswer() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                binder.writeBeanIfValid(question);
                onSaveCallback.accept(question);
            }
            close();
        }
    }

    @Override
    public void showDialog(Object question, SerializableConsumer<?> callback) {
        this.question = (SimpleQuestion) question;
        this.onSaveCallback = (SerializableConsumer<SimpleQuestion>) callback;

        binder.readBean(this.question);

        open();
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
