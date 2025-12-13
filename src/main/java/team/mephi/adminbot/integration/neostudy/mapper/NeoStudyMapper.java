package team.mephi.adminbot.integration.neostudy.mapper;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.integration.neostudy.dto.*;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
public class NeoStudyMapper {

    public NeoStudyUserRequest toNeoStudyUserRequest(User user) {
        if (user == null) {
            return null;
        }

        return NeoStudyUserRequest.builder()
                .externalId(user.getTgId())
                .name(user.getFullName())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .build();
    }

    public User toUser(NeoStudyUserResponse response) {
        if (response == null) {
            return null;
        }

        return User.builder()
                .tgId(response.getExternalId())
                .fullName(response.getName())
                .status(response.getStatus() != null
                        ? UserStatus.valueOf(response.getStatus().toUpperCase())
                        : null)
                .createdAt(response.getCreatedAt() != null
                        ? response.getCreatedAt().atZone(ZoneId.of("UTC")).toInstant()
                        : Instant.now())
                .updatedAt(response.getUpdatedAt() != null
                        ? response.getUpdatedAt().atZone(ZoneId.of("UTC")).toInstant()
                        : Instant.now())
                .build();
    }

    public void updateUserFromNeoStudy(User user, NeoStudyUserResponse response) {
        if (user == null || response == null) {
            return;
        }

        if (response.getName() != null) {
            user.setFullName(response.getName());
        }

        if (response.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(response.getStatus().toUpperCase()));
        }

        if (response.getUpdatedAt() != null) {
            user.setUpdatedAt(response.getUpdatedAt()
                    .atZone(ZoneId.of("UTC"))
                    .toInstant());
        }
    }

    public NeoStudyCourseResponse toNeoStudyCourse(Direction direction) {
        if (direction == null) {
            return null;
        }

        return NeoStudyCourseResponse.builder()
                .code(direction.getCode())
                .name(direction.getName())
                .build();
    }

    public Direction toDirection(NeoStudyCourseResponse response) {
        if (response == null) {
            return null;
        }

        return Direction.builder()
                .code(response.getCode())
                .name(response.getName())
                .build();
    }

    public void updateDirectionFromNeoStudy(Direction direction, NeoStudyCourseResponse response) {
        if (direction == null || response == null) {
            return;
        }

        if (response.getName() != null) {
            direction.setName(response.getName());
        }
        if (response.getCode() != null) {
            direction.setCode(response.getCode());
        }
    }

    public NeoStudyEnrollmentRequest toNeoStudyEnrollmentRequest(
            String userId,
            String courseId,
            String status) {

        return NeoStudyEnrollmentRequest.builder()
                .userId(userId)
                .courseId(courseId)
                .status(status)
                .enrollmentDate(Instant.now().toString())
                .build();
    }

    public NeoStudyEnrollmentRequest toNeoStudyEnrollmentRequest(
            String userId,
            String courseId,
            String status,
            Map<String, Object> metadata) {

        return NeoStudyEnrollmentRequest.builder()
                .userId(userId)
                .courseId(courseId)
                .status(status)
                .enrollmentDate(Instant.now().toString())
                .metadata(metadata != null ? metadata : new HashMap<>())
                .build();
    }
}
