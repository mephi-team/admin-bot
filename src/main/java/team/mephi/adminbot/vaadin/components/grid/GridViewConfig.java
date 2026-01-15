package team.mephi.adminbot.vaadin.components.grid;

import com.vaadin.flow.data.provider.DataProvider;
import lombok.Builder;
import lombok.Getter;
import team.mephi.adminbot.vaadin.components.GridSelectActions;

import java.util.Set;
import java.util.function.Consumer;

@Builder
@Getter
public final class GridViewConfig<T> {
    private final GridSelectActions gsa;
    private final Consumer<String> filterSetter;
    private final String searchPlaceholder;
    private final String emptyLabel;
    private final Set<String> visibleColumns;
    private final Set<String> hiddenColumns;
    private final DataProvider<T, ?> dataProvider;
}