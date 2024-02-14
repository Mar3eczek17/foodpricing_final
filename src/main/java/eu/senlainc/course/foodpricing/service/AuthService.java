package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.dto.AuthRequest;
import eu.senlainc.course.foodpricing.entities.User;
import eu.senlainc.course.foodpricing.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String LOGIN_REQUEST_PROCESSING_MESSAGE = "Processing login request for username '{}'";
    private static final String LOGIN_SUCCESSFUL_MESSAGE = "Login successful for user '{}'";
    private static final String LOGIN_SUCCESS_RESPONSE = "Login successful. Token: ";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found for username '{}'";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials for username '{}'";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String INVALID_CREDENTIALS = "Invalid credentials";

    public ApiResponse login(AuthRequest authRequest) {
        try {
            logger.info(LOGIN_REQUEST_PROCESSING_MESSAGE, authRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            User user = userDao.findByUsername(username);

            if (user != null) {
                String token = jwtTokenUtil.generateAccessToken(user);
                logger.info(LOGIN_SUCCESSFUL_MESSAGE, username);

                return new ApiResponse(LOGIN_SUCCESS_RESPONSE + token);
            } else {
                logger.warn(USER_NOT_FOUND_MESSAGE, username);
                return new ApiResponse(USER_NOT_FOUND);
            }
        } catch (BadCredentialsException ex) {
            logger.warn(INVALID_CREDENTIALS_MESSAGE, authRequest.getUsername());
            return new ApiResponse(INVALID_CREDENTIALS);
        }
    }
}