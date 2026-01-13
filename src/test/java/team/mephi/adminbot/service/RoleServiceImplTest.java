package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @Test
    void getByCodeReturnsInitializedRole() {
        when(roleRepository.findAll()).thenReturn(List.of(Role.builder().code("ADMIN").name("Admin").build()));
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);

        Optional<RoleDto> result = service.getByCode("ADMIN");

        assertThat(result)
                .isPresent()
                .get()
                .extracting(RoleDto::getCode, RoleDto::getName)
                .containsExactly("ADMIN", "Admin");
        verify(roleRepository).findAll();
    }
}
