package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
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

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Rollback
public class TeacherControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TeacherRepository teacherRepository;

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
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenTeacher_whenFindById_thenStatus200() throws Exception {
        Teacher teacher = new Teacher(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.save(teacher);

        mvc.perform(get("/api/teacher/" + teacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lastName", is(teacher.getLastName())))
                .andExpect(jsonPath("$.firstName", is(teacher.getFirstName())));

    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenTeachers_whenFindAll_thenStatus200() throws Exception {
        Teacher teacher1 = new Teacher(
                2L,
                "Smith",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Teacher teacher2 = new Teacher(
                3L,
                "Smith",
                "Jane",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        teacherRepository.saveAll(Arrays.asList(teacher1, teacher2));

        mvc.perform(get("/api/teacher/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].lastName", is(teacher1.getLastName())))
                .andExpect(jsonPath("$[0].firstName", is(teacher1.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(teacher2.getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(teacher2.getFirstName())));

    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenNonExistentTeacher_whenFindById_thenStatus404() throws Exception {
        mvc.perform(get("/api/teacher/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}