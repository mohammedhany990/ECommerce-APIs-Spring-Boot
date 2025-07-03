package com.ECom.ECom.config;

import com.ECom.ECom.entity.User;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.repository.UserRepo;
import com.ECom.ECom.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService; // Service to generate JWT tokens

    @Autowired
    private UserRepo userRepository; // Repository to access User data from DB

    @Autowired
    private ObjectMapper objectMapper; // Jackson ObjectMapper to write JSON responses

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Get the authenticated OAuth2 user details from Spring Security context
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Extract the user's email from OAuth2 user attributes
        String email = oAuth2User.getAttribute("email");

        // Try to find existing user in DB by email
        // If not found, create a new User entity with email and name from OAuth2 data
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .fullName(oAuth2User.getAttribute("name")) // get full name from OAuth2 info
                            .password("") // password empty since OAuth2 users authenticate via Google
                            .build();
                    return userRepository.save(newUser); // save new user to DB
                });

        // Generate JWT token for the authenticated user
        String token = jwtService.generateToken(user);

        // Build a standardized API response containing the JWT token
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("OAuth2 login successful")
                .data(token) // put JWT token inside data field
                .build();

        // Set response content type to JSON and encoding to UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Set HTTP status to 200 OK
        response.setStatus(HttpServletResponse.SC_OK);

        // Write the ApiResponse object as JSON into the HTTP response body
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
