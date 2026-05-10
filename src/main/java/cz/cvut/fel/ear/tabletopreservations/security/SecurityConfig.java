package cz.cvut.fel.ear.tabletopreservations.security;

import cz.cvut.fel.ear.tabletopreservations.security.model.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationSuccess authenticationSuccess;
    private final AuthenticationFailure authenticationFailure;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          AuthenticationSuccess authenticationSuccess,
                          AuthenticationFailure authenticationFailure) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccess = authenticationSuccess;
        this.authenticationFailure = authenticationFailure;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("PROVIDER", "PLAYER")
                .role("PROVIDER").implies("PLAYER")
                .build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(AbstractHttpConfigurer::disable)
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // Permit all GET endpoints
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                // Permit user registration
                .requestMatchers(HttpMethod.POST, "/rest/users/register").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // Configure form login with custom handlers
            .formLogin(form -> form
                .successHandler(authenticationSuccess)
                .failureHandler(authenticationFailure)
            )
            // Configure logout with custom handler
            .logout(logout -> logout
                .logoutSuccessHandler(authenticationSuccess)
            )
            // Use HTTP Basic authentication as fallback
            .httpBasic(basic -> basic
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            // Use stateless session management for REST API
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            // Configure user details service
            .userDetailsService(userDetailsService);

        return http.build();
    }
}
