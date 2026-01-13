package team.mephi.adminbot.integration.neostudy.mapper;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyCourseResponse;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyEnrollmentRequest;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyUserRequest;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyUserResponse;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для маппера {@link NeoStudyMapper}.
 */
class NeoStudyMapperTest {

    private final NeoStudyMapper mapper = new NeoStudyMapper();

    /**
     * Проверяет обработку null пользователя при построении запроса.
     */
    @Test
    void givenNullUser_WhenMappingToRequest_ThenResultIsNull() {
        // Arrange
        User user = null;

        // Act
        NeoStudyUserRequest request = mapper.toNeoStudyUserRequest(user);

        // Assert
        assertNull(request);
    }

    /**
     * Проверяет маппинг полей пользователя в запрос NeoStudy.
     */
    @Test
    void givenUser_WhenMappingToRequest_ThenFieldsMapped() {
        // Arrange
        User user = new User();
        user.setTgId("tg-123");
        user.setUserName("Alex");
        user.setStatus(UserStatus.ACTIVE);

        // Act
        NeoStudyUserRequest request = mapper.toNeoStudyUserRequest(user);

        // Assert
        assertNotNull(request);
        assertEquals("tg-123", request.getExternalId());
        assertEquals("Alex", request.getName());
        assertEquals("ACTIVE", request.getStatus());
    }

    /**
     * Проверяет маппинг ответа NeoStudy в доменную модель пользователя.
     */
    @Test
    void givenUserResponse_WhenMappingToUser_ThenFieldsMapped() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2024, 2, 10, 12, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 2, 11, 8, 5);
        NeoStudyUserResponse response = NeoStudyUserResponse.builder()
                .externalId("ext-9")
                .name("Ivan")
                .status("blocked")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Act
        User user = mapper.toUser(response);

        // Assert
        assertNotNull(user);
        assertEquals("ext-9", user.getTgId());
        assertEquals("Ivan", user.getUserName());
        assertEquals(UserStatus.BLOCKED, user.getStatus());
        assertEquals(createdAt.toInstant(ZoneOffset.UTC), user.getCreatedAt());
        assertEquals(updatedAt.toInstant(ZoneOffset.UTC), user.getUpdatedAt());
    }

    /**
     * Проверяет обновление пользователя только ненулевыми значениями.
     */
    @Test
    void givenResponseWithNulls_WhenUpdatingUser_ThenOnlyNonNullFieldsApplied() {
        // Arrange
        User user = new User();
        user.setUserName("Old Name");
        user.setStatus(UserStatus.ACTIVE);
        user.setUpdatedAt(Instant.EPOCH);

        LocalDateTime responseUpdatedAt = LocalDateTime.of(2024, 3, 1, 9, 15);
        NeoStudyUserResponse response = NeoStudyUserResponse.builder()
                .name(null)
                .status("inactive")
                .updatedAt(responseUpdatedAt)
                .build();

        // Act
        mapper.updateUserFromNeoStudy(user, response);

        // Assert
        assertEquals("Old Name", user.getUserName());
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        assertEquals(responseUpdatedAt.toInstant(ZoneOffset.UTC), user.getUpdatedAt());
    }

    /**
     * Проверяет двусторонний маппинг направления в курс и обратно.
     */
    @Test
    void givenDirection_WhenMappedToCourse_ThenFieldsRoundTrip() {
        // Arrange
        Direction direction = Direction.builder()
                .code("JAVA")
                .name("Java Backend")
                .build();

        // Act
        NeoStudyCourseResponse response = mapper.toNeoStudyCourse(direction);
        Direction mapped = mapper.toDirection(response);

        // Assert
        assertNotNull(response);
        assertEquals("JAVA", response.getCode());
        assertEquals("Java Backend", response.getName());
        assertNotNull(mapped);
        assertEquals("JAVA", mapped.getCode());
        assertEquals("Java Backend", mapped.getName());
    }

    /**
     * Проверяет установку значений по умолчанию в запросе зачисления.
     */
    @Test
    void givenEnrollmentRequest_WhenMetadataNull_ThenDefaultsApplied() {
        // Arrange
        // Act
        NeoStudyEnrollmentRequest request = mapper.toNeoStudyEnrollmentRequest(
                "user-1",
                "course-2",
                "active",
                null
        );

        NeoStudyEnrollmentRequest requestWithMetadata = mapper.toNeoStudyEnrollmentRequest(
                "user-3",
                "course-4",
                "pending",
                Map.of("source", "batch")
        );

        // Assert
        assertNotNull(request);
        assertEquals("user-1", request.getUserId());
        assertEquals("course-2", request.getCourseId());
        assertEquals("active", request.getStatus());
        assertNotNull(request.getEnrollmentDate());
        assertNotNull(request.getMetadata());
        assertTrue(request.getMetadata().isEmpty());
        assertEquals("batch", requestWithMetadata.getMetadata().get("source"));
    }
}
