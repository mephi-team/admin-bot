package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

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
        assertEquals(0L, report.getClicks());
        assertEquals(0L, report.getUniqueUsers());
        assertEquals(0L, report.getRegistrations());
        assertEquals(0.0, report.getCtr());
        assertEquals(0.0, report.getConversion());
    }

    @Test
    void builder_shouldSetFields() {
        // given / when
        UtmReport report = UtmReport.builder()
                .id(1L)
                .utmSource("s")
                .utmMedium("m")
                .utmCampaign("c")
                .utmTerm("t")
                .utmContent("cnt")
                .clicks(10L)
                .uniqueUsers(5L)
                .registrations(2L)
                .ctr(0.5)
                .conversion(0.2)
                .build();

        // then
        assertEquals(1L, report.getId());
        assertEquals("s", report.getUtmSource());
        assertEquals(10L, report.getClicks());
        assertNotNull(report.toString());
    }
}
