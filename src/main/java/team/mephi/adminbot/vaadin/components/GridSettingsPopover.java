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
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GridSettingsPopover extends Popover {
    public GridSettingsPopover(Grid<?> grid, Set<String> disabled, Set<String> exclude) {
        setModal(true);
        setBackdropVisible(true);
        setPosition(PopoverPosition.BOTTOM_END);

        Div heading = new Div(getTranslation("grid_settings_popover_header"));
        heading.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.Padding.XSMALL);

//        List<String> columns = List.of("firstName", "lastName", "email",
//                "phone", "birthday", "profession");
        var columns = grid.getColumns().stream().map(Grid.Column::getKey).filter(c -> !exclude.contains(c)).toList();

        CheckboxGroup<String> group = new CheckboxGroup<>();
        group.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        group.addThemeName("neo");
        group.setItems(columns);
        group.setItemLabelGenerator((item) -> {
            var label = Arrays.stream(item.replaceAll("[^a-zA-Z\\s]", " ").replaceAll("(?<=\\p{Lower})(?=\\p{Upper})", " ").toLowerCase().split("\\s+")).filter(s -> !s.isEmpty()).collect(Collectors.joining("_"));
            return getTranslation("grid_settings_" + label + "_label");
        });
        group.addValueChangeListener((e) -> {
            columns.forEach((key) -> {
                grid.getColumnByKey(key).setVisible(e.getValue().contains(key));
            });
        });

        Set<String> defaultColumns = new HashSet<>(columns);//Set.of("firstName", "lastName", "email", "profession");
        defaultColumns.removeAll(disabled);
        group.setValue(defaultColumns);

        Button showAll = new Button(getTranslation("grid_settings_popover_action_show_all"), (e) -> {
            group.setValue(new HashSet<>(columns));
        });
        showAll.addThemeVariants(ButtonVariant.LUMO_SMALL);

        Button reset = new Button(getTranslation("grid_settings_popover_action_reset"), (e) -> {
            group.setValue(defaultColumns);
        });
        reset.addThemeVariants(ButtonVariant.LUMO_SMALL);

        HorizontalLayout footer = new HorizontalLayout(showAll, reset);
        footer.setSpacing(false);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        add(heading, group, footer);
    }
}
