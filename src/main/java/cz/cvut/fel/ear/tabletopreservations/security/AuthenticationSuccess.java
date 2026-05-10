package cz.cvut.fel.ear.tabletopreservations.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.tabletopreservations.security.model.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles successful authentication and logout events.
 */
@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccess.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationSuccess(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LOG.info("User {} successfully authenticated.", userDetails.getUsername());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Login successful");
        result.put("username", userDetails.getUsername());
        result.put("roles", userDetails.getAuthorities());

        objectMapper.writeValue(response.getWriter(), result);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        LOG.info("User {} successfully logged out.", authentication != null ? authentication.getName() : "unknown");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logout successful");

        objectMapper.writeValue(response.getWriter(), result);
    }
}
