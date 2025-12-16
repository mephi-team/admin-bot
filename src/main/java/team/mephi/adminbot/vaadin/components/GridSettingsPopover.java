package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class GridSettingsPopover extends Popover {
    public GridSettingsPopover(Grid<?> grid, Set<String> exclude) {
        setModal(true);
        setBackdropVisible(true);
        setPosition(PopoverPosition.BOTTOM_END);

        Div heading = new Div("Настройка колонок");
        heading.getStyle().set("font-weight", "600");
        heading.getStyle().set("padding", "var(--lumo-space-xs)");

//        List<String> columns = List.of("firstName", "lastName", "email",
//                "phone", "birthday", "profession");
        var columns = grid.getColumns().stream().map(Grid.Column::getKey).toList();

        CheckboxGroup<String> group = new CheckboxGroup<>();
        group.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        group.setItems(columns);
        group.setItemLabelGenerator((item) -> {
            String label = StringUtils
                    .join(StringUtils.splitByCharacterTypeCamelCase(item), " ");
            return StringUtils.capitalize(label.toLowerCase());
        });
        group.addValueChangeListener((e) -> {
            columns.stream().forEach((key) -> {
                grid.getColumnByKey(key).setVisible(e.getValue().contains(key));
            });
        });

        Set<String> defaultColumns = new HashSet<>(columns);//Set.of("firstName", "lastName", "email", "profession");
        defaultColumns.removeAll(exclude);
        group.setValue(defaultColumns);

        Button showAll = new Button("Показать все", (e) -> {
            group.setValue(new HashSet<String>(columns));
        });
        showAll.addThemeVariants(ButtonVariant.LUMO_SMALL);

        Button reset = new Button("Сброс", (e) -> {
            group.setValue(defaultColumns);
        });
        reset.addThemeVariants(ButtonVariant.LUMO_SMALL);

        HorizontalLayout footer = new HorizontalLayout(showAll, reset);
        footer.setSpacing(false);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        add(heading, group, footer);
    }
}
