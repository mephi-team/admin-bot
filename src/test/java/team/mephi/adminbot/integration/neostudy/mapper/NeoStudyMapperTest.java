package team.mephi.adminbot.integration.neostudy.mapper;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyCourseResponse;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyEnrollmentRequest;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyUserRequest;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyUserResponse;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для NeoStudyMapper.
 * Покрывают: преобразование пользователей и курсов NeoStudy.
 */
class NeoStudyMapperTest {

    /**
     * Проверяет возврат null при отсутствии пользователя.
     */
    @Test
    void Given_nullUser_When_toNeoStudyUserRequest_Then_returnsNull() {
        // Arrange
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        NeoStudyUserRequest result = mapper.toNeoStudyUserRequest(null);

        // Assert
        assertNull(result);
    }

    /**
     * Проверяет маппинг пользователя в запрос NeoStudy.
     */
    @Test
    void Given_user_When_toNeoStudyUserRequest_Then_mapsFields() {
        // Arrange
        User user = User.builder().tgId("tg").userName("Name").status(UserStatus.ACTIVE).build();
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        NeoStudyUserRequest result = mapper.toNeoStudyUserRequest(user);

        // Assert
        assertEquals("tg", result.getExternalId());
        assertEquals("Name", result.getName());
        assertEquals("ACTIVE", result.getStatus());
    }

    /**
     * Проверяет маппинг ответа NeoStudy в пользователя.
     */
    @Test
    void Given_response_When_toUser_Then_mapsStatusAndDates() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 13, 0);
        NeoStudyUserResponse response = NeoStudyUserResponse.builder()
                .externalId("tg")
                .name("Name")
                .status("active")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        User result = mapper.toUser(response);

        // Assert
        assertEquals("tg", result.getTgId());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertEquals(createdAt.atZone(ZoneId.of("UTC")).toInstant(), result.getCreatedAt());
        assertEquals(updatedAt.atZone(ZoneId.of("UTC")).toInstant(), result.getUpdatedAt());
    }

    /**
     * Проверяет возврат null при отсутствии ответа NeoStudy.
     */
    @Test
    void Given_nullResponse_When_toUser_Then_returnsNull() {
        // Arrange
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        User result = mapper.toUser(null);

        // Assert
        assertNull(result);
    }

    /**
     * Проверяет обновление пользователя из ответа NeoStudy.
     */
    @Test
    void Given_existingUser_When_updateUserFromNeoStudy_Then_updatesFields() {
        // Arrange
        User user = User.builder().userName("Old").status(UserStatus.BLOCKED).build();
        NeoStudyUserResponse response = NeoStudyUserResponse.builder()
                .name("New")
                .status("active")
                .updatedAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        mapper.updateUserFromNeoStudy(user, response);

        // Assert
        assertEquals("New", user.getUserName());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertNotNull(user.getUpdatedAt());
    }

    /**
     * Проверяет маппинг направления в курс NeoStudy.
     */
    @Test
    void Given_direction_When_toNeoStudyCourse_Then_mapsFields() {
        // Arrange
        Direction direction = Direction.builder().code("C1").name("Course").build();
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        NeoStudyCourseResponse result = mapper.toNeoStudyCourse(direction);

        // Assert
        assertEquals("C1", result.getCode());
        assertEquals("Course", result.getName());
    }

    /**
     * Проверяет маппинг курса NeoStudy в направление.
     */
    @Test
    void Given_course_When_toDirection_Then_mapsFields() {
        // Arrange
        NeoStudyCourseResponse response = NeoStudyCourseResponse.builder().code("C2").name("Chem").build();
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        Direction result = mapper.toDirection(response);

        // Assert
        assertEquals("C2", result.getCode());
        assertEquals("Chem", result.getName());
    }

    /**
     * Проверяет обновление направления из данных NeoStudy.
     */
    @Test
    void Given_direction_When_updateDirectionFromNeoStudy_Then_updatesFields() {
        // Arrange
        Direction direction = Direction.builder().code("OLD").name("Old").build();
        NeoStudyCourseResponse response = NeoStudyCourseResponse.builder().code("NEW").name("New").build();
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        mapper.updateDirectionFromNeoStudy(direction, response);

        // Assert
        assertEquals("NEW", direction.getCode());
        assertEquals("New", direction.getName());
    }

    /**
     * Проверяет формирование запроса на зачисление без метаданных.
     */
    @Test
    void Given_data_When_toEnrollmentRequest_Then_setsFields() {
        // Arrange
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        NeoStudyEnrollmentRequest result = mapper.toNeoStudyEnrollmentRequest("u1", "c1", "ACTIVE");

        // Assert
        assertEquals("u1", result.getUserId());
        assertEquals("c1", result.getCourseId());
        assertEquals("ACTIVE", result.getStatus());
        assertNotNull(result.getEnrollmentDate());
    }

    /**
     * Проверяет формирование запроса на зачисление с метаданными.
     */
    @Test
    void Given_metadata_When_toEnrollmentRequest_Then_usesMetadata() {
        // Arrange
        NeoStudyMapper mapper = new NeoStudyMapper();

        // Act
        NeoStudyEnrollmentRequest result = mapper.toNeoStudyEnrollmentRequest("u2", "c2", "ACTIVE", Map.of("key", "val"));

        // Assert
        assertEquals("val", result.getMetadata().get("key"));
    }
}
