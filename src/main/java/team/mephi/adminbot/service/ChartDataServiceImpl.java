package team.mephi.adminbot.service;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Service;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.OrderStatus;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Реализация сервиса для получения данных диаграмм по различным фильтрам.
 */
@Service
public class ChartDataServiceImpl implements ChartDataService {
    private static final String DEFAULT_BLUE = "#2168df";
    private static final Locale RU = Locale.of("ru");
    private final java.util.Random random = new java.util.Random();

    /** Генерация меток для указанного интервала и данных фильтра.
     *
     * @param interval   интервал активности (час, день, месяц).
     * @param filterData данные фильтра для извлечения диапазона дат.
     * @return массив строковых меток для диаграммы.
     */
    private String[] labelsForInterval(ActivityIntervals interval, Object filterData) {
        try {
            switch (interval) {
                case HOUR:
                    // всегда 24 метки от 00:00 до 23:00
                    return generateHourLabels();
                case DAY: {
                    DateRange dr = extractDateRange(filterData);
                    if (dr != null) {
                        long days = ChronoUnit.DAYS.between(dr.start, dr.end) + 1;
                        int count = (int) Math.max(1, Math.min(days, Integer.MAX_VALUE));
                        return generateDayLabels(count, dr.start);
                    } else {
                        return generateDayLabels(12, LocalDate.now().minusDays(11));
                    }
                }
                default: { // MONTH
                    DateRange dr = extractDateRange(filterData);
                    if (dr != null) {
                        int months = monthsBetweenInclusive(dr.start.withDayOfMonth(1), dr.end.withDayOfMonth(1));
                        months = Math.max(1, months);
                        return generateMonthLabels(months, YearMonth.from(dr.start));
                    } else {
                        return generateMonthLabels(12, YearMonth.now().minusMonths(11));
                    }
                }
            }
        } catch (Exception e) {
            return generateMonthLabels(12, YearMonth.now().minusMonths(11));
        }
    }

    /** Генерация часовых меток за последние 24 часа
     *
     * @return массив строковых меток для каждого часа.
     */
    private String[] generateHourLabels() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:00");
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        String[] labels = new String[24];
        for (int i = 0; i < 24; i++) {
            labels[i] = now.minusHours(24 - i).format(fmt);
        }
        return labels;
    }

    /** Генерация дней: start + count-1 дней
     *
     * @param count количество дней.
     * @param start дата начала.
     * @return массив строковых меток для каждого дня.
     */
    private String[] generateDayLabels(int count, LocalDate start) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] labels = new String[count];
        for (int i = 0; i < count; i++) {
            labels[i] = start.plusDays(i).format(fmt);
        }
        return labels;
    }

    /** Генерация месяцев: startYm + count-1 месяцев
     *
     * @param count   количество месяцев.
     * @param startYm год-месяц начала.
     * @return массив строковых меток для каждого месяца.
     */
    private String[] generateMonthLabels(int count, YearMonth startYm) {
        String[] labels = new String[count];
        for (int i = 0; i < count; i++) {
            YearMonth m = startYm.plusMonths(i);
            String monthName = m.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, RU);
            labels[i] = monthName.substring(0, 1).toUpperCase(RU) + monthName.substring(1);
        }
        return labels;
    }

    /** Подсчет количества месяцев между двумя датами включительно
     *
     * @param from дата начала.
     * @param to   дата окончания.
     * @return количество месяцев между датами.
     */
    private int monthsBetweenInclusive(LocalDate from, LocalDate to) {
        return (int) (ChronoUnit.MONTHS.between(from, to) + 1);
    }

    /** Извлечение диапазона дат из объекта фильтра
     *
     * @param filter объект фильтра с методами получения дат.
     * @return диапазон дат или null, если не удалось извлечь.
     */
    private DateRange extractDateRange(Object filter) {
        if (filter == null) return null;
        try {
            LocalDate start = null, end = null;
            String[] startNames = new String[]{"getFrom", "getStart", "getDateFrom", "getFromDate"};
            String[] endNames = new String[]{"getTo", "getEnd", "getDateTo", "getToDate"};
            for (String name : startNames) {
                start = tryGetAsLocalDate(filter, name);
                if (start != null) break;
            }
            for (String name : endNames) {
                end = tryGetAsLocalDate(filter, name);
                if (end != null) break;
            }
            if (start != null && end != null) {
                if (end.isBefore(start)) {
                    LocalDate tmp = start;
                    start = end;
                    end = tmp;
                }
                return new DateRange(start, end);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /** Попытка вызова метода объекта и преобразование результата в LocalDate
     *
     * @param obj        объект с методом.
     * @param methodName имя метода для вызова.
     * @return результат преобразования в LocalDate или null.
     */
    private LocalDate tryGetAsLocalDate(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            Object val = m.invoke(obj);
            return toLocalDate(val);
        } catch (Exception ignored) {
        }
        return null;
    }

    /** Преобразование объекта в LocalDate
     *
     * @param val объект для преобразования.
     * @return LocalDate или null, если преобразование невозможно.
     */
    private LocalDate toLocalDate(Object val) {
        if (val == null) return null;
        if (val instanceof LocalDate) return (LocalDate) val;
        if (val instanceof LocalDateTime) return ((LocalDateTime) val).toLocalDate();
        if (val instanceof Instant) return LocalDateTime.ofInstant((Instant) val, ZoneId.systemDefault()).toLocalDate();
        if (val instanceof java.util.Date)
            return Instant.ofEpochMilli(((java.util.Date) val).getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (val instanceof String) {
            String s = ((String) val).trim();
            DateTimeFormatter[] fmts = new DateTimeFormatter[]{
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                    DateTimeFormatter.ISO_LOCAL_DATE,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
            };
            for (DateTimeFormatter f : fmts) {
                try {
                    if (f == DateTimeFormatter.ISO_LOCAL_DATE_TIME) {
                        return LocalDateTime.parse(s, f).toLocalDate();
                    } else {
                        return LocalDate.parse(s, f);
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /** Создание набора данных для диаграммы
     *
     * @param label           метка набора данных.
     * @param backgroundColor цвет фона набора данных.
     * @param labels          метки для данных.
     * @return созданный набор данных.
     */
    private BarDataset createDataset(String label, String backgroundColor, String[] labels) {
        BarDataset dataset = new BarDataset().setLabel(label);
        if (backgroundColor != null) {
            dataset.setBackgroundColor(backgroundColor);
        }
        for (int i = 0; i < labels.length; i++) {
            dataset.addData(random.nextInt(0, 100));
        }
        return dataset;
    }

    /** Создание данных диаграммы с несколькими наборами данных
     *
     * @param labels метки для диаграммы.
     * @param specs  спецификации наборов данных.
     * @return созданные данные диаграммы.
     */
    private BarData createBarData(String[] labels, DatasetSpec... specs) {
        BarData barData = new BarData().addLabels(labels);
        for (DatasetSpec spec : specs) {
            barData.addDataset(createDataset(spec.label(), spec.color(), labels));
        }
        return barData;
    }

    @Override
    public BarData forActivity(ActivityView.ActivityFilterData data) {
        String[] labels = labelsForInterval(data.getInterval(), data);
        return createBarData(labels, new DatasetSpec(I18NProvider.translate("page_analytics_form_activity_activity_label"), DEFAULT_BLUE));
    }

    @Override
    public BarData forPreorders(PreordersView.PreorderFilterData data) {
        List<String> list = List.of(DEFAULT_BLUE, "#d3e1f9", "#ccc", "#f2d391", "#91f2b1", "#9199f2", "#f291f2");
        Iterator<String> it = Stream.generate(() -> list).flatMap(List::stream).iterator();

        String[] labels = labelsForInterval(data.getInterval(), data);
        return createBarData(labels,
                new DatasetSpec(I18NProvider.translate(OrderStatus.ALL.getTranslationKey()), it.next()),
                new DatasetSpec(I18NProvider.translate(OrderStatus.ACTIVE.getTranslationKey()), it.next()),
                new DatasetSpec(I18NProvider.translate(OrderStatus.REVOKED.getTranslationKey()), it.next()));
    }

    @Override
    public BarData forOrders(OrdersView.OrderFilterData data) {
        List<String> list = List.of(DEFAULT_BLUE, "#d3e1f9", "#ccc", "#f2d391", "#91f2b1", "#9199f2", "#f291f2");
        Iterator<String> it = Stream.generate(() -> list).flatMap(List::stream).iterator();

        String[] labels = labelsForInterval(data.getInterval(), data);

        return createBarData(labels, data.getStatuses()
                .stream()
                .map(s -> new DatasetSpec(I18NProvider.translate(s.getTranslationKey()), it.next()))
                .toArray(DatasetSpec[]::new));
    }

    @Override
    public BarData forUtm(UtmView.UtmFilterData data) {
        String[] labels = labelsForInterval(data.getInterval(), data);
        return createBarData(labels, new DatasetSpec("UTM", DEFAULT_BLUE));
    }

    /**
     * Вспомогательный класс для хранения диапазона дат.
     */
    private static final class DateRange {
        final LocalDate start;
        final LocalDate end;

        /** Конструктор диапазона дат.
         *
         * @param s дата начала.
         * @param e дата окончания.
         */
        DateRange(LocalDate s, LocalDate e) {
            this.start = s;
            this.end = e;
        }
    }

    /**
     * Спецификация набора данных для диаграммы.
     * @param label метка набора данных.
     * @param color цвет набора данных.
     */
    private record DatasetSpec(String label, String color) {
    }
}
