package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.dto.UserDto;
import eu.senlainc.course.foodpricing.entities.User;
import eu.senlainc.course.foodpricing.enums.UserRole;
import eu.senlainc.course.foodpricing.expectations.UserAlreadyExistsException;
import eu.senlainc.course.foodpricing.expectations.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String USER_NOT_FOUND_BY_ID_MESSAGE = "User not found with id: ";
    private static final String PROFILE_EDIT_SUCCESS_MESSAGE = "Profile edited successfully for user with ID: {}";
    private static final String USER_ALREADY_EXISTS_MESSAGE = "Username is already taken: ";
    private static final String USER_REGISTER_SUCCESS_MESSAGE = "User registered successfully. Username: {}";
    private static final String USER_NOT_FOUND_BY_USERNAME_MESSAGE = "User not found with username: ";

    @Transactional
    public UserDto editProfile(Integer userId, UserDto userDto) {
        User existingUser = userDao.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_BY_ID_MESSAGE + userId));

        existingUser.setUsername(userDto.getUsername());
        existingUser.setPassword(userDto.getPassword());
        existingUser.setEmail(userDto.getEmail());

        userDao.save(existingUser);

        logger.info(PROFILE_EDIT_SUCCESS_MESSAGE, userId);

        return convertToDto(existingUser);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) {
        if (userDao.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE + userDto.getUsername());
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(userDto.getPassword());
        newUser.setEmail(userDto.getEmail());
        newUser.setUserRole(UserRole.USER);

        userDao.save(newUser);

        logger.info(USER_REGISTER_SUCCESS_MESSAGE, userDto.getUsername());

        return convertToDto(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME_MESSAGE + username);
        }
        return user;
    }
}