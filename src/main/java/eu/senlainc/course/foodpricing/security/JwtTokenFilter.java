package eu.senlainc.course.foodpricing.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.hibernate.internal.util.StringHelper.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDao userDao;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String NO_TOKEN_MESSAGE = "No Bearer Token found in the request headers";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid or expired Bearer Token";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith(BEARER_PREFIX)) {
            logger.info(NO_TOKEN_MESSAGE);
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validateToken(token)) {
            logger.warn(INVALID_TOKEN_MESSAGE);

            sendErrorResponse(response, INVALID_TOKEN_MESSAGE);
            return;
        }

        User user = null;
        User foundUser = userDao.findByUsername(jwtTokenUtil.getUsernameFromToken(token));
        if (foundUser != null) {
            user = foundUser;
        }

        if (user == null) {
            sendErrorResponse(response, USER_NOT_FOUND_MESSAGE);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null,
                user.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ApiResponse apiResponse = new ApiResponse(message);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(tokenPrefix);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}