package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

class SessionMapperTest {

    @Mock
    private TeacherService teacherService;


    @Mock
    private UserService userService;


    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity() {
        // Mock data
        SessionDto sessionDto = new SessionDto(1L, "name", new Date(), 1L, "description", Arrays.asList(1L, 2L), null, null);
        Teacher teacher = new Teacher(1L, "teacherLastName", "teacherFirstName", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user2@test.com", "lastName2", "firstName2", "password456", false, null, null);

        // Mock behavior of teacherService
        when(teacherService.findById(1L)).thenReturn(teacher);

        // Mock behavior of userService
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Test
        Session session = sessionMapper.toEntity(sessionDto);

        // Assertions
        assertThat(session.getId()).isEqualTo(sessionDto.getId());
        assertThat(session.getName()).isEqualTo(sessionDto.getName());
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(session.getTeacher().getId()).isEqualTo(sessionDto.getTeacher_id());
        assertThat(session.getUsers()).hasSize(sessionDto.getUsers().size());
    }

    @Test
    void testToDto() {
        // Mock data
        Teacher teacher = new Teacher(1L, "teacherLastName", "teacherFirstName", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user2@test.com", "lastName2", "firstName2", "password456", false, null, null);
        Session session = new Session(1L, "name", new Date(), "description", teacher, Arrays.asList(user1, user2), null, null);

        // Test
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assertions
        assertThat(sessionDto.getId()).isEqualTo(session.getId());
        assertThat(sessionDto.getName()).isEqualTo(session.getName());
        assertThat(sessionDto.getDescription()).isEqualTo(session.getDescription());
        assertThat(sessionDto.getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(sessionDto.getUsers()).hasSize(session.getUsers().size());
    }

    @Test
    void testToListEntity() {
        // Mock data
        Teacher teacher1 = new Teacher(1L, "teacherLastName1", "teacherFirstName1", null, null);
        Teacher teacher2 = new Teacher(2L, "teacherLastName2", "teacherFirstName2", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user2@test.com", "lastName2", "firstName2", "password456", false, null, null);

        SessionDto sessionDto1 = new SessionDto(1L, "name1", new Date(), 1L, "description1", Arrays.asList(1L, 2L), null,
                null);
        SessionDto sessionDto2 = new SessionDto(2L, "name2", new Date(), 2L, "description2", Arrays.asList(3L, 4L), null,
                null);

        // Mock behavior of teacherService
        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherService.findById(2L)).thenReturn(teacher2);

        // Mock behavior of userService
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Test
        List<Session> sessions = sessionMapper.toEntity(Arrays.asList(sessionDto1, sessionDto2));

        // Assertions
        assertThat(sessions).isNotNull().hasSize(2);
        assertThat(sessions.get(0).getId()).isEqualTo(sessionDto1.getId());
        assertThat(sessions.get(0).getName()).isEqualTo(sessionDto1.getName());
        assertThat(sessions.get(0).getDescription()).isEqualTo(sessionDto1.getDescription());
        assertThat(sessions.get(0).getTeacher().getId()).isEqualTo(sessionDto1.getTeacher_id());
        assertThat(sessions.get(0).getUsers()).hasSize(sessionDto1.getUsers().size());

        assertThat(sessions.get(1).getId()).isEqualTo(sessionDto2.getId());
        assertThat(sessions.get(1).getName()).isEqualTo(sessionDto2.getName());
        assertThat(sessions.get(1).getDescription()).isEqualTo(sessionDto2.getDescription());
        assertThat(sessions.get(1).getTeacher().getId()).isEqualTo(sessionDto2.getTeacher_id());
        assertThat(sessions.get(1).getUsers()).hasSize(sessionDto2.getUsers().size());
    }

    @Test
    void testToListDto() {
        // Mock data
        Teacher teacher1 = new Teacher(1L, "teacherLastName1", "teacherFirstName1", null, null);
        Teacher teacher2 = new Teacher(2L, "teacherLastName2", "teacherFirstName2", null, null);

        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user2@test.com", "lastName2", "firstName2", "password456", false, null, null);

        Session session1 = new Session(1L, "name1", new Date(), "description1", teacher1, Arrays.asList(user1, user2),
                null, null);
        Session session2 = new Session(2L, "name2", new Date(), "description2", teacher2, Collections.emptyList(),
                null, null);

        // Test
        List<SessionDto> sessionDtos = sessionMapper.toDto(Arrays.asList(session1, session2));

        // Assertions
        assertThat(sessionDtos).isNotNull().hasSize(2);

        assertThat(sessionDtos.get(0).getId()).isEqualTo(session1.getId());
        assertThat(sessionDtos.get(0).getName()).isEqualTo(session1.getName());
        assertThat(sessionDtos.get(0).getDescription()).isEqualTo(session1.getDescription());
        assertThat(sessionDtos.get(0).getTeacher_id()).isEqualTo(session1.getTeacher().getId());
        assertThat(sessionDtos.get(0).getUsers()).hasSize(session1.getUsers().size());

        assertThat(sessionDtos.get(1).getId()).isEqualTo(session2.getId());
        assertThat(sessionDtos.get(1).getName()).isEqualTo(session2.getName());
        assertThat(sessionDtos.get(1).getDescription()).isEqualTo(session2.getDescription());
        assertThat(sessionDtos.get(1).getTeacher_id()).isEqualTo(session2.getTeacher().getId());
        assertThat(sessionDtos.get(1).getUsers()).hasSize(session2.getUsers().size());
    }
}