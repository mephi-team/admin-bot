package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности UtmReport (проверка дефолтных значений и Lombok-методов).
 */
class UtmReportTest {

    @Test
    void newUtmReport_shouldHaveDefaultMetrics() {
        // given / when
        UtmReport report = new UtmReport();

        // then
        assertEquals(0, report.getClicks());
        assertEquals(0, report.getUniqueUsers());
        assertEquals(0, report.getRegistrations());
        assertEquals(BigDecimal.ZERO, report.getCtr());
        assertEquals(BigDecimal.ZERO, report.getConversion());
    }

    @Test
    void builder_shouldSetFields() {
        // given / when
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
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

        // then
        assertEquals(1L, report.getId());
        assertEquals(startDate, report.getPeriodStart());
        assertEquals(endDate, report.getPeriodEnd());
        assertEquals("s", report.getUtmSource());
        assertEquals(10, report.getClicks());
        assertEquals(new BigDecimal("0.5000"), report.getCtr());
        assertNotNull(report.toString());
    }

    @Test
    void equals_shouldUseBusinessKey() {
        // given
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
                .id(2L) // Different ID
                .periodStart(startDate)
                .periodEnd(endDate)
                .utmSource("google")
                .utmMedium("cpc")
                .utmCampaign("spring_sale")
                .utmTerm("keyword")
                .utmContent("ad1")
                .clicks(200) // Different clicks
                .build();

        UtmReport report3 = UtmReport.builder()
                .id(3L)
                .periodStart(startDate)
                .periodEnd(endDate)
                .utmSource("facebook") // Different source
                .utmMedium("cpc")
                .utmCampaign("spring_sale")
                .utmTerm("keyword")
                .utmContent("ad1")
                .clicks(100)
                .build();

        // then
        assertEquals(report1, report2); // Same business key, different ID and metrics
        assertNotEquals(report1, report3); // Different business key
        assertEquals(report1.hashCode(), report2.hashCode());
        assertNotEquals(report1.hashCode(), report3.hashCode());
    }
}
