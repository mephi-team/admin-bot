package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сущности {@link UtmReport}.
 */
class UtmReportTest {

    /**
     * Проверяет значения метрик по умолчанию.
     */
    @Test
    void givenNewReport_WhenCreated_ThenDefaultMetricsAreZero() {
        // Arrange
        UtmReport report = new UtmReport();

        // Act
        int clicks = report.getClicks();
        int uniqueUsers = report.getUniqueUsers();
        int registrations = report.getRegistrations();
        BigDecimal ctr = report.getCtr();
        BigDecimal conversion = report.getConversion();

        // Assert
        assertEquals(0, clicks);
        assertEquals(0, uniqueUsers);
        assertEquals(0, registrations);
        assertEquals(BigDecimal.ZERO, ctr);
        assertEquals(BigDecimal.ZERO, conversion);
    }

    /**
     * Проверяет заполнение полей через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        // Act
        UtmReport report = UtmReport.builder()
                .id(1L)
                .periodStart(startDate)
                .periodEnd(endDate)
                .utmSource("s")
                .utmMedium("m")
                .utmCampaign("c")
                .utmTerm("t")
                .utmContent("cnt")
                .clicks(10)
                .uniqueUsers(5)
                .registrations(2)
                .ctr(new BigDecimal("0.5000"))
                .conversion(new BigDecimal("0.2000"))
                .build();

        // Assert
        assertEquals(1L, report.getId());
        assertEquals(startDate, report.getPeriodStart());
        assertEquals(endDate, report.getPeriodEnd());
        assertEquals("s", report.getUtmSource());
        assertEquals(10, report.getClicks());
        assertEquals(new BigDecimal("0.5000"), report.getCtr());
        assertNotNull(report.toString());
    }

    /**
     * Проверяет сравнение отчётов по бизнес-ключу.
     */
    @Test
    void givenReportsWithSameBusinessKey_WhenCompared_ThenEqualsUsesBusinessKey() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        UtmReport report1 = UtmReport.builder()
                .id(1L)
                .periodStart(startDate)
                .periodEnd(endDate)
                .utmSource("google")
                .utmMedium("cpc")
                .utmCampaign("spring_sale")
                .utmTerm("keyword")
                .utmContent("ad1")
                .clicks(100)
                .build();

        UtmReport report2 = UtmReport.builder()
                .id(2L)
                .periodStart(startDate)
                .periodEnd(endDate)
                .utmSource("google")
                .utmMedium("cpc")
                .utmCampaign("spring_sale")
                .utmTerm("keyword")
                .utmContent("ad1")
                .clicks(200)
                .build();

        UtmReport report3 = UtmReport.builder()
                .id(3L)
                .periodStart(startDate)
                .periodEnd(endDate)
                .utmSource("facebook")
                .utmMedium("cpc")
                .utmCampaign("spring_sale")
                .utmTerm("keyword")
                .utmContent("ad1")
                .clicks(100)
                .build();

        // Act
        boolean equalsSameKey = report1.equals(report2);
        boolean equalsDifferentKey = report1.equals(report3);

        // Assert
        assertEquals(true, equalsSameKey);
        assertNotEquals(true, equalsDifferentKey);
        assertEquals(report1.hashCode(), report2.hashCode());
        assertNotEquals(report1.hashCode(), report3.hashCode());
    }
}
