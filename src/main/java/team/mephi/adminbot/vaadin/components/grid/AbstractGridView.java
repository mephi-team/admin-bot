package team.mephi.adminbot.vaadin.components.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.GridSettingsPopover;
import team.mephi.adminbot.vaadin.components.SearchFragment;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.fields.SearchField;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractGridView<T> extends VerticalLayout {
    protected final Grid<T> grid;
    protected List<Long> selectedIds;
    protected GridSelectActions gsa;
    protected Consumer<String> filterSetter;
    protected String searchPlaceholder;
    protected String emptyLabel;
    protected Set<String> visibleColumns;
    protected Set<String> hiddenColumns;

    protected AbstractGridView() {
        setSizeFull();
        setPadding(false);

        grid = new Grid<>(getItemClass(), false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setSizeFull();
    }

    protected final void setup(GridViewConfig<T> config) {
        this.gsa = config.getGsa();
        this.filterSetter = config.getFilterSetter();
        this.searchPlaceholder = config.getSearchPlaceholder();
        this.emptyLabel = config.getEmptyLabel();
        this.visibleColumns = config.getVisibleColumns();
        this.hiddenColumns = config.getHiddenColumns();
        if (config.getDataProvider() != null) {
            setProvider(config.getDataProvider());
        }
        init();
    }

    private void init() {
        GridMultiSelectionModel<T> selectionModel = (GridMultiSelectionModel<T>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);

        // вызов для добавления колонок в подклассе
        configureColumns(grid);

        // общая колонка действий, подкласс реализует создание component column
        configureActionColumn(grid);

        // обработчик селекции обновляет gsa
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(this::extractId).toList();
            gsa.setCount(selectedIds.size());
        });

        grid.setEmptyStateText(emptyLabel);

        // поиск
        var searchField = new SearchField(searchPlaceholder);
        searchField.addValueChangeListener(e -> filterSetter.accept(e.getValue()));

        // настройки колонок
        var settingsBtn = new IconButton(VaadinIcon.COG_O.create());
        var settingsPopover = new GridSettingsPopover(grid, visibleColumns, hiddenColumns);
        settingsPopover.setTarget(settingsBtn);

        // кнопка скачивания (повесить действие в подклассе при необходимости)
        var downloadBtn = new IconButton(VaadinIcon.DOWNLOAD_ALT.create(), e -> {
        });

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);

        grid.getDataProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });
    }

    // подкласс указывает реальный класс элементов грида
    protected abstract Class<T> getItemClass();

    // добавление основных колонок (имя, email и пр.) — реализует подкласс
    protected abstract void configureColumns(Grid<T> grid);

    // добавление колонки с action-кнопками — реализует подкласс
    protected abstract void configureActionColumn(Grid<T> grid);

    // как извлекать id из элемента — реализует подкласс
    protected abstract Long extractId(T item);

    public void setProvider(DataProvider<T, ?> dataProvider) {
        grid.setDataProvider(dataProvider);
    }

}