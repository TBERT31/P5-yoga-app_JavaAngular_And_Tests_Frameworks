package com.openclassrooms.starterjwt.controllers;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;


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
@Rollback
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

    @AfterEach
    public void tearDown() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
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

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description",
                teacherWithGoodId.get(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sessionRepository.save(session);

        Optional<Session> sessionWithGoodId = sessionRepository.findByName("Session 1");

        mvc.perform(get("/api/session/" + sessionWithGoodId.get().getId())
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

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        Session session1 = new Session(
                2L,
                "Session 2",
                new Date(),
                "Description 2",
                teacherWithGoodId.get(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Session session2 = new Session(
                3L,
                "Session 3",
                new Date(),
                "Description 3",
                teacherWithGoodId.get(),
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
    @WithMockUser(roles = "ADMIN")
    public void givenSession_whenCreate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        SessionDto sessionDto = new SessionDto(
                4L,
                "Session 4",
                new Date(),
                teacherWithGoodId.get().getId(),
                "Description 4",
                Arrays.asList(),
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
                        .content(gson.toJson(sessionMapper.toDto(session))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(sessionDto.getName())))
                .andExpect(jsonPath("$.description", is(sessionDto.getDescription())));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenSession_whenUpdate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        Session session = new Session(
                5L,
                "Session 5",
                new Date(),
                "Description 5",
                teacherWithGoodId.get(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto(
                5L,
                "Updated Session 5",
                new Date(),
                teacherWithGoodId.get().getId(),
                "Updated Description 5",
                Arrays.asList(),
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
    @WithMockUser(roles = "ADMIN")
    public void givenSession_whenDelete_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        Session session = new Session(
                6L,
                "Session 6",
                new Date(),
                "Description 6",
                teacherWithGoodId.get(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sessionRepository.save(session);

        Optional<Session> sessionWithGoodId = sessionRepository.findByName("Session 6");

        mvc.perform(delete("/api/session/" + sessionWithGoodId.get().getId())
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

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        User user = new User(
                "user@example.com",
                "Doe",
                "John",
                "password",
                false
        );

        userRepository.save(user);

        Session session = new Session(
                7L,
                "Session 7",
                new Date(),
                "Description 7",
                teacherWithGoodId.get(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sessionRepository.save(session);

        Optional<User> userWithGoodId = userRepository.findByEmail("user@example.com");
        Optional<Session> sessionWithGoodId = sessionRepository.findByName("Session 7");

        System.out.println("Session id >>>>> "+sessionWithGoodId.get().getId()  + " user id >>>> "+userWithGoodId.get().getId());
        System.out.println("Session id >>>>> "+session.getId()  + " user id >>>> "+user.getId());

        mvc.perform(post("/api/session/" + sessionWithGoodId.get().getId().toString() + "/participate/" + userWithGoodId.get().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenSession_whenNoLongerParticipate_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        Optional<Teacher> teacherWithGoodId = teacherRepository.findByFirstName("John");

        User user = new User(
                "user@example.com",
                "Depp",
                "Johnny",
                "password",
                false
        );

        userRepository.save(user);

        Session session = new Session(
                8L,
                "Session 8",
                new Date(),
                "Description 8",
                teacherWithGoodId.get(),
                Arrays.asList(user),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sessionRepository.save(session);

        Optional<User> userWithGoodId = userRepository.findByEmail("user@example.com");
        Optional<Session> sessionWithGoodId = sessionRepository.findByName("Session 8");

        mvc.perform(delete("/api/session/" + sessionWithGoodId.get().getId() + "/participate/" + userWithGoodId.get().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}