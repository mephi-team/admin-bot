//package team.mephi.adminbot.controller;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.ui.ExtendedModelMap;
//import org.springframework.ui.Model;
//import team.mephi.adminbot.model.User;
//import team.mephi.adminbot.repository.UserRepository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
/// **
// * Юнит-тесты для UserController без поднятия Spring-контекста.
// */
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Test
//    void usersPage_whenStatusAll_shouldCallFindAllAndSetModelAttributes() {
//        // given
//        List<User> users = List.of(new User(), new User());
//        when(userRepository.findAll()).thenReturn(users);
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        //String viewName = userController.usersPage("all", model);
//
//        // then
//        //assertEquals("users", viewName);
//
//        verify(userRepository).findAll();
//        verify(userRepository, never()).findByStatus(anyString());
//
//        assertSame(users, model.getAttribute("users"));
//        assertEquals("all", model.getAttribute("currentStatus"));
//    }
//
//    @Test
//    void usersPage_whenStatusActive_shouldCallFindByStatusActive() {
//        // given
//        List<User> activeUsers = List.of(new User());
//        when(userRepository.findByStatus("active")).thenReturn(activeUsers);
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        //String viewName = userController.usersPage("active", model);
//
//        // then
//        //assertEquals("users", viewName);
//
//        verify(userRepository).findByStatus("active");
//        verify(userRepository, never()).findAll();
//
//        assertSame(activeUsers, model.getAttribute("users"));
//        assertEquals("active", model.getAttribute("currentStatus"));
//    }
//
//    @Test
//    void usersPage_whenStatusBlocked_shouldCallFindByStatusBlocked() {
//        // given
//        List<User> blockedUsers = List.of(new User());
//        when(userRepository.findByStatus("blocked")).thenReturn(blockedUsers);
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        String viewName = userController.usersPage("blocked", model);
//
//        // then
//        assertEquals("users", viewName);
//
//        verify(userRepository).findByStatus("blocked");
//        verify(userRepository, never()).findAll();
//
//        assertSame(blockedUsers, model.getAttribute("users"));
//        assertEquals("blocked", model.getAttribute("currentStatus"));
//    }
//
//    @Test
//    void usersPage_whenStatusUnknown_shouldFallbackToFindAll() {
//        // given
//        List<User> users = List.of(new User());
//        when(userRepository.findAll()).thenReturn(users);
//
//        Model model = new ExtendedModelMap();
//
//        // when
//        String viewName = userController.usersPage("unknown", model);
//
//        // then
//        assertEquals("users", viewName);
//
//        verify(userRepository).findAll();
//        verify(userRepository, never()).findByStatus(anyString());
//
//        assertSame(users, model.getAttribute("users"));
//        assertEquals("unknown", model.getAttribute("currentStatus"));
//    }
//}
