package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.*;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MailingEditorDialog extends Dialog implements DialogWithTitle {
    private final BeanValidationBinder<SimpleMailing> binder = new BeanValidationBinder<>(SimpleMailing.class);
    private final TabSheet tabSheet = new TabSheet();
    private final Tab tab1;
    private final Tab tab2;
    private final Button next = new PrimaryButton(getTranslation("next_button"), VaadinIcon.ARROW_RIGHT.create());
    private final Button prev = new SecondaryButton(getTranslation("prev_button"), VaadinIcon.ARROW_LEFT.create());
    private SerializableConsumer<SimpleMailing> onSaveCallback;
    private SimpleMailing mailing;
    private final Button saveButton = new PrimaryButton(getTranslation("save_button"), e -> onSave());
    private Binder.Binding<SimpleMailing, String> binding1;

    public MailingEditorDialog(UserService userService, RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TemplateService templateService) {
        var form1 = new MailingForm(userService, roleService, cohortService, directionService, cityService);
        var form2 = new TemplateFormTab(templateService);

        binder.forField(form1.getChannels())
                .asRequired()
                .bind(SimpleMailing::getChannels, SimpleMailing::setChannels);
        binder.forField(form1.getUsers())
                .asRequired()
                .withConverter(RoleDto::getName, role -> roleService.getByName(role).orElse(null))
                .bind(SimpleMailing::getUsers, SimpleMailing::setUsers);
        binder.forField(form1.getCohort())
                .asRequired()
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(cohortService.getAllCohorts().getFirst()))
                .bind(SimpleMailing::getCohort, SimpleMailing::setCohort);
        binder.forField(form1.getDirection())
                .asRequired()
                .withConverter(SimpleDirection::getName, direction -> directionService.getByName(direction).orElse(null))
                .bind(SimpleMailing::getDirection, SimpleMailing::setDirection);
        binder.forField(form1.getCity())
                .asRequired()
                .withConverter(CityDto::getName, user -> cityService.getByName(user).orElse(null))
                .bind(SimpleMailing::getCity, SimpleMailing::setCity);
        binder.forField(form1.getCurator())
                .withConverter(UserDto::getUserName, user -> userService.findCuratorByUserName(user).orElse(UserDto.builder().userName("").build()))
                .bind(SimpleMailing::getCurator, SimpleMailing::setCurator);
        binder.forField(form1.getListBox())
                .withValidator(s -> !s.isEmpty(), getTranslation("form_mailing_first_name_last_name_validation_message"))
                .withConverter(
                        s -> s.stream().map(SimpleUser::getTgId).toList(),
                        users -> userService.getAllUsers()
                                .stream()
                                .filter(u -> users.contains(u.getTgName()))
                                .map(s -> SimpleUser.builder()
                                        .id(s.getId())
                                        .fullName(s.getFullName())
                                        .tgId(s.getTgName())
                                        .build()
                                )
                                .collect(Collectors.toSet())
                )
                .bind(SimpleMailing::getRecipients, SimpleMailing::setRecipients);
        binder.forField(form2.getName1())
                .bind(SimpleMailing::getName, SimpleMailing::setName);
        binding1 = binder.forField(form2.getText1())
                .withValidator(e -> !e.isBlank(), getTranslation("form_template_text_validation_message"))
                .bind(SimpleMailing::getText, SimpleMailing::setText);
        binding1.setValidatorsDisabled(true);
        binder.bindInstanceFields(form1);
        binder.bindInstanceFields(form2);

        tab1 = tabSheet.add(getTranslation("dialog_mailing_tab_recipients"), form1);
        tab2 = tabSheet.add(getTranslation("dialog_mailing_tab_message"), form2);

        setHeaderTitle("dialog_mailing_new_title");
        add(tabSheet);
        setWidth("100%");
        setMaxWidth("500px");

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

        binder.addStatusChangeListener(e -> {
            next.setEnabled(e.getBinder().isValid());
            saveButton.setEnabled(e.getBinder().isValid());
        });

        tabSheet.addSelectedChangeListener(l -> {
            if (tab1.isSelected()) {
                saveButton.setVisible(false);
                prev.setVisible(false);
                next.setVisible(true);
                binding1.setValidatorsDisabled(true);
            } else if (tab2.isSelected()) {
                saveButton.setVisible(true);
                prev.setVisible(true);
                next.setVisible(false);
                binding1.setValidatorsDisabled(false);
            }
        });
    }

    @Override
    public void showDialog(Object mailing, SerializableConsumer<?> callback) {
        this.mailing = Objects.isNull(mailing)
                ? SimpleMailing.builder()
                .channels(Set.of("Email", "Telegram"))
                .recipients(List.of())
                .build()
                : (SimpleMailing) mailing;
        this.onSaveCallback = (SerializableConsumer<SimpleMailing>) callback;
        binder.readBean(this.mailing);
        binder.setReadOnly(false);
        tabSheet.setSelectedTab(tab1);
        open();
    }

    private void onSave() {
        if (binder.validate().isOk()) {
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
