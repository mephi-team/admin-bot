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

class NeoStudyMapperTest {

    private final NeoStudyMapper mapper = new NeoStudyMapper();

    @Test
    void toNeoStudyUserRequest_returnsNullWhenUserNull() {
        assertNull(mapper.toNeoStudyUserRequest(null));
    }

    @Test
    void toNeoStudyUserRequest_mapsFields() {
        User user = new User();
        user.setTgId("tg-123");
        user.setUserName("Alex");
        user.setStatus(UserStatus.ACTIVE);

        NeoStudyUserRequest request = mapper.toNeoStudyUserRequest(user);

        assertNotNull(request);
        assertEquals("tg-123", request.getExternalId());
        assertEquals("Alex", request.getName());
        assertEquals("ACTIVE", request.getStatus());
    }

    @Test
    void toUser_mapsResponseFieldsAndTimes() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 2, 10, 12, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 2, 11, 8, 5);
        NeoStudyUserResponse response = NeoStudyUserResponse.builder()
                .externalId("ext-9")
                .name("Ivan")
                .status("blocked")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        User user = mapper.toUser(response);

        assertNotNull(user);
        assertEquals("ext-9", user.getTgId());
        assertEquals("Ivan", user.getUserName());
        assertEquals(UserStatus.BLOCKED, user.getStatus());
        assertEquals(createdAt.toInstant(ZoneOffset.UTC), user.getCreatedAt());
        assertEquals(updatedAt.toInstant(ZoneOffset.UTC), user.getUpdatedAt());
    }

    @Test
    void updateUserFromNeoStudy_updatesOnlyNonNullFields() {
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

        mapper.updateUserFromNeoStudy(user, response);

        assertEquals("Old Name", user.getUserName());
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        assertEquals(responseUpdatedAt.toInstant(ZoneOffset.UTC), user.getUpdatedAt());
    }

    @Test
    void courseMapping_roundTripsDirectionFields() {
        Direction direction = Direction.builder()
                .code("JAVA")
                .name("Java Backend")
                .build();

        NeoStudyCourseResponse response = mapper.toNeoStudyCourse(direction);
        assertNotNull(response);
        assertEquals("JAVA", response.getCode());
        assertEquals("Java Backend", response.getName());

        Direction mapped = mapper.toDirection(response);
        assertNotNull(mapped);
        assertEquals("JAVA", mapped.getCode());
        assertEquals("Java Backend", mapped.getName());
    }

    @Test
    void enrollmentRequest_includesDefaults() {
        NeoStudyEnrollmentRequest request = mapper.toNeoStudyEnrollmentRequest(
                "user-1",
                "course-2",
                "active",
                null
        );

        assertNotNull(request);
        assertEquals("user-1", request.getUserId());
        assertEquals("course-2", request.getCourseId());
        assertEquals("active", request.getStatus());
        assertNotNull(request.getEnrollmentDate());
        assertNotNull(request.getMetadata());
        assertTrue(request.getMetadata().isEmpty());

        NeoStudyEnrollmentRequest requestWithMetadata = mapper.toNeoStudyEnrollmentRequest(
                "user-3",
                "course-4",
                "pending",
                Map.of("source", "batch")
        );
        assertEquals("batch", requestWithMetadata.getMetadata().get("source"));
    }
}
