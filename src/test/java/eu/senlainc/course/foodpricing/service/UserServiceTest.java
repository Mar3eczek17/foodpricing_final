package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.dto.UserDto;
import eu.senlainc.course.foodpricing.entities.User;
import eu.senlainc.course.foodpricing.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private static User createUser(Integer userId, String username, String password, String email, UserRole userRole) {
        return new User(userId, username, password, email, userRole);
    }

    private static UserDto createUserDto(Integer userId, String username, String password, String email, UserRole userRole) {
        return new UserDto(userId, username, password, email, userRole);
    }

    private static final User USER_ADMIN = createUser(1, "admin", "adminPass", "admin@example.com", UserRole.ADMIN);
    private static final User USER_USER = createUser(2, "user", "userPass", "user@example.com", UserRole.USER);
    private static final UserDto USER_DTO_NEW_USERNAME = createUserDto(null, "newUsername", "newPassword", "newEmail@example.com", UserRole.USER);
    private static final String USERNAME = "existingUser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEditProfile_SuccessfulEdit() {
        when(userDao.findUserById(1)).thenReturn(java.util.Optional.of(USER_ADMIN));

        UserDto editedProfile = userService.editProfile(1, USER_DTO_NEW_USERNAME);

        verify(userDao, times(1)).findUserById(1);
        verify(userDao, times(1)).save(any(User.class));

        assertNotNull(editedProfile);
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() {
        when(userDao.existsByUsername(USER_DTO_NEW_USERNAME.getUsername())).thenReturn(false);

        UserDto registeredUser = userService.registerUser(USER_DTO_NEW_USERNAME);

        verify(userDao, times(1)).existsByUsername(USER_DTO_NEW_USERNAME.getUsername());
        verify(userDao, times(1)).save(any(User.class));

        assertNotNull(registeredUser);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        when(userDao.findByUsername(USERNAME)).thenReturn(USER_USER);

        UserDetails userDetails = userService.loadUserByUsername(USERNAME);

        verify(userDao, times(1)).findByUsername(USERNAME);

        assertNotNull(userDetails);
    }
}