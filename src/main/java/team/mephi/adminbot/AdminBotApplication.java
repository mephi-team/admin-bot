package team.mephi.adminbot;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.components.fields.FullNameField;

@SpringBootApplication
//@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
@Theme(value = "neoflex", variant = Lumo.LIGHT)
@Uses(ComboBox.class)
@Uses(FullNameField.class)
@Uses(EmailField.class)
@Uses(TextField.class)
@Uses(MultiSelectComboBox.class)
@Uses(FormLayout.class)
@Uses(Grid.class)
@Uses(ConfirmDialog.class)
@Uses(MultiSelectListBox.class)
@Uses(Accordion.class)
@Uses(AccordionPanel.class)
@Uses(Notification.class)
@Uses(Popover.class)
@Uses(CheckboxGroup.class)
@Uses(ChartContainer.class)
@Uses(RadioButtonGroup.class)
@Uses(DatePicker.class)
public class AdminBotApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(AdminBotApplication.class, args);
    }
}
