package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для RoleServiceImpl.
 * Покрывают: инициализацию списка ролей и поиск.
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    /**
     * Проверяет инициализацию списка ролей при создании сервиса.
     */
    @Test
    void Given_repositoryRoles_When_getAllRoles_Then_returnsListWithAll() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(List.of(new Role("ADMIN", "Администратор", "Admin")));
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);

        // Act
        List<RoleDto> result = service.getAllRoles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Все", result.getFirst().getName());
    }

    /**
     * Проверяет возврат списка ролей с пагинацией.
     */
    @Test
    void Given_pageable_When_getAllRolesPaged_Then_returnsList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(List.of(new Role("ADMIN", "Администратор", "Admin")));
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);

        // Act
        List<RoleDto> result = service.getAllRoles(PageRequest.of(0, 1), "");

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Проверяет поиск роли по коду.
     */
    @Test
    void Given_code_When_getByCode_Then_returnsRole() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(List.of(new Role("ADMIN", "Администратор", "Admin")));
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);

        // Act
        Optional<RoleDto> result = service.getByCode("ADMIN");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Администратор", result.get().getName());
    }

    /**
     * Проверяет поиск роли по имени.
     */
    @Test
    void Given_name_When_getByName_Then_returnsRole() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(List.of(new Role("ADMIN", "Администратор", "Admin")));
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);

        // Act
        Optional<RoleDto> result = service.getByName("Администратор");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getCode());
    }
}
