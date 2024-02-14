package eu.senlainc.course.foodpricing.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senlainc.course.foodpricing.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String UNAUTHORIZED_ACCESS_MESSAGE = "Unauthorized access";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/reset-password").permitAll()
                .requestMatchers("/api/users/register").hasRole("ADMIN")
                .requestMatchers("/api/users/{userId}/profile").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/price-tracking/link-price").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/price-tracking/dynamics").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/price-tracking/compare-prices/{productId}").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/price-tracking/price-graph/{productId}").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/price-tracking/batch-upload").hasRole("ADMIN")
                .requestMatchers("/api/product-categories").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/products/by-category/{categoryId}").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/products/add").hasRole("ADMIN")
                .requestMatchers("/api/products/update").hasRole("ADMIN")
                .requestMatchers("/api/products/batch-upload").hasRole("ADMIN")
                .requestMatchers("/api/products/delete/{productId}").hasRole("ADMIN")
                .requestMatchers("/api/trading-points").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint((request, response, authException) -> {
                            sendErrorResponse(response);
                        })
                );
        return http.build();
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = new ApiResponse(UNAUTHORIZED_ACCESS_MESSAGE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(tokenPrefix);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        DaoAuthenticationProvider provider = authenticationProvider();
        return new ProviderManager(Collections.singletonList(provider));
    }
}