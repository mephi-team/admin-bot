package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.users.components.RoleService;

import java.util.Objects;

public class MailingEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleMailing> binder = new BeanValidationBinder<>(SimpleMailing.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());
    private final TabSheet tabSheet = new TabSheet();
    private final Tab tab1;
    private final Tab tab2;
    private final Button next = new Button(getTranslation("next_button"), VaadinIcon.ARROW_RIGHT.create());
    private final Button prev = new Button(getTranslation("prev_button"), VaadinIcon.ARROW_LEFT.create());

    private SerializableConsumer<SimpleMailing> onSaveCallback;
    private SimpleMailing mailing;

    public MailingEditorDialog(UserService userService, RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        var form1 = new MailingForm(userService, roleService, cohortService, directionService, cityService);
        var form2 = new TemplateFormTab();

        binder.forField(form1.getCurator())
                .withValidator(Objects::nonNull, getTranslation("form_mailing_curator_validation_message"))
                .withConverter(UserDto::getUserName, user -> userService.findByUserName(user).orElse(null))
                .bind(SimpleMailing::getCurator, SimpleMailing::setCurator);
        binder.forField(form1.getUsers())
                .withValidator(Objects::nonNull, getTranslation("form_mailing_users_validation_message"))
                .withConverter(RoleDto::getCode, role -> roleService.getByCode(role).orElse(roleService.getAllRoles().getFirst()))
                .bind(SimpleMailing::getUsers, SimpleMailing::setUsers);
        binder.forField(form1.getCohort())
                .withValidator(Objects::nonNull, getTranslation("form_mailing_direction_validation_message"))
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(null))
                .bind(SimpleMailing::getCohort, SimpleMailing::setCohort);
        binder.forField(form1.getDirection())
                .withValidator(Objects::nonNull, getTranslation("form_mailing_direction_validation_message"))
                .withConverter(SimpleDirection::getName, direction -> directionService.getByName(direction).orElse(null))
                .bind(SimpleMailing::getDirection, SimpleMailing::setDirection);
        binder.forField(form1.getCity())
                .withValidator(Objects::nonNull, getTranslation("form_mailing_city_validation_message"))
                .withConverter(CityDto::getName, user -> cityService.getByName(user).orElse(null))
                .bind(SimpleMailing::getCity, SimpleMailing::setCity);
        binder.bindInstanceFields(form1);
        binder.bindInstanceFields(form2);

        tab1 = tabSheet.add(getTranslation("dialog_mailing_tab_recipients"), form1);
        tab2 = tabSheet.add(getTranslation("dialog_mailing_tab_message"), form2);

        setHeaderTitle("dialog_mailing_new_title");
        add(tabSheet);

        saveButton.setVisible(false);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.setIconAfterText(true);
        next.addClassNames(LumoUtility.Margin.Right.AUTO);
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.addClickListener(s -> {
            tabSheet.setSelectedTab(tab2);
        });
        prev.addClassNames(LumoUtility.Margin.Right.AUTO);
        prev.addClickListener(s -> {
            tabSheet.setSelectedTab(tab1);
        });
        prev.setVisible(false);
        getFooter().add(
                next,
                prev,
                saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));

        tabSheet.addSelectedChangeListener(l -> {
           if (tab1.isSelected()) {
               saveButton.setVisible(false);
               prev.setVisible(false);
               next.setVisible(true);
           } else if (tab2.isSelected()) {
               saveButton.setVisible(true);
               prev.setVisible(true);
               next.setVisible(false);
           }
        });
    }

    public void showDialogForNew(SerializableConsumer<SimpleMailing> callback) {
        this.mailing = new SimpleMailing();
        this.onSaveCallback = callback;
        binder.readBean(mailing);
        binder.setReadOnly(false);
        tabSheet.setSelectedTab(tab1);
        open();
    }

    public void showDialogForEdit(SimpleMailing mailing, SerializableConsumer<SimpleMailing> callback) {
        this.mailing = mailing;
        this.onSaveCallback = callback;
        binder.readBean(mailing);
        binder.setReadOnly(false);
        tabSheet.setSelectedTab(tab1);
        open();
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                binder.writeBeanIfValid(mailing);
                onSaveCallback.accept(mailing);
            }
            close();
        }
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
