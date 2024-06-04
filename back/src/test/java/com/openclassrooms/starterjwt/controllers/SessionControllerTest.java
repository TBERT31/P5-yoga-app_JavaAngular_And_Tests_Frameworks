package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenFindById_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        teacherRepository.save(teacher);

        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacher,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sessionRepository.save(session);

        mvc.perform(get("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(session.getName())))
                .andExpect(jsonPath("$.description", is(session.getDescription())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSessions_whenFindAll_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        teacherRepository.save(teacher);

        Session session1 = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacher,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Session session2 = new Session(
                2L,
                "Session 2",
                new Date(),
                "Description",
                teacher,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sessionRepository.saveAll(Arrays.asList(session1, session2));

        mvc.perform(get("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].name", is(session1.getName())))
                .andExpect(jsonPath("$[0].description", is(session1.getDescription())))
                .andExpect(jsonPath("$[1].name", is(session2.getName())))
                .andExpect(jsonPath("$[1].description", is(session2.getDescription())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenCreate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        teacherRepository.save(teacher);

        SessionDto sessionDto = new SessionDto(
                1L,
                "Session 1",
                new Date(),
                teacher.getId(),
                "Description",
                Arrays.asList(1l,2l),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Session session = sessionMapper.toEntity(sessionDto);
        sessionRepository.save(session);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        mvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(sessionDto.getName())))
                .andExpect(jsonPath("$.description", is(sessionDto.getDescription())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenUpdate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacher,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto(
                1L,
                "Updated Session",
                new Date(),
                teacher.getId(),
                "Updated Description",
                Arrays.asList(1l,2l),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        mvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(sessionDto.getName())))
                .andExpect(jsonPath("$.description", is(sessionDto.getDescription())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenDelete_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        teacherRepository.save(teacher);

        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacher,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sessionRepository.save(session);

        mvc.perform(delete("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenParticipate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        User user = new User(
                "user1@example.com",
                "Doe",
                "John",
                "password",
                false
        );
        userRepository.save(user);

        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacher,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sessionRepository.save(session);

        mvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenNoLongerParticipate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                3L,
                "Smith",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        User user = new User(
                "user3@example.com",
                "Doe",
                "John",
                "password",
                false
        );

        userRepository.save(user);

        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacher,
                Arrays.asList(user),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sessionRepository.save(session);

        mvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}