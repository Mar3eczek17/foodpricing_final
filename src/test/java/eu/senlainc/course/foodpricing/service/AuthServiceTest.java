package eu.senlainc.course.foodpricing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.dto.AuthRequest;
import eu.senlainc.course.foodpricing.entities.User;
import eu.senlainc.course.foodpricing.enums.UserRole;
import eu.senlainc.course.foodpricing.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private AuthService authService;
    private static final AuthRequest AUTH_REQUEST = new AuthRequest("username", "password");
    private static final User USER = new User(1, "username", "password", "user@example.com", UserRole.USER);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_SuccessfulLogin() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");
        when(userDao.findByUsername("username")).thenReturn(USER);
        when(jwtTokenUtil.generateAccessToken(USER)).thenReturn("fakeToken");

        ApiResponse response = authService.login(AUTH_REQUEST);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDao, times(1)).findByUsername("username");
        verify(jwtTokenUtil, times(1)).generateAccessToken(USER);

        assertNotNull(response);
        assertEquals("Login successful. Token: fakeToken", response.getMessage());
    }
}