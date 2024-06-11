package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Rollback
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenValidUser_whenLogin_thenStatus200() throws Exception {
        User user = new User(
                "user444@example.com",
                "Doey",
                "Jammy",
                passwordEncoder.encode("password123"),
                false
        );

        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user444@example.com");
        loginRequest.setPassword("password123");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtUtils.toJson(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenInvalidPassword_whenLogin_thenStatus401() throws Exception {
        User user = new User(
                "user555@example.com",
                "Doe",
                "John",
                passwordEncoder.encode("password123"),
                false
        );

        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user555@example.com");
        loginRequest.setPassword("wrongpassword");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenNonExistentUser_whenLogin_thenStatus401() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenValidUser_whenRegister_thenStatus200() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user555@example.com");
        signupRequest.setFirstName("Janny");
        signupRequest.setLastName("Doey");
        signupRequest.setPassword("password123");

        System.out.println(jwtUtils.toJson(signupRequest));

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtUtils.toJson(signupRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenExistingEmail_whenRegister_thenStatus400() throws Exception {
        User existingUser = new User(
                "user555@example.com",
                "Doe",
                "John",
                passwordEncoder.encode("password123"),
                false
        );

        userRepository.save(existingUser);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user555@example.com");
        signupRequest.setFirstName("Janny");
        signupRequest.setLastName("Doey");
        signupRequest.setPassword("password123");

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }
}