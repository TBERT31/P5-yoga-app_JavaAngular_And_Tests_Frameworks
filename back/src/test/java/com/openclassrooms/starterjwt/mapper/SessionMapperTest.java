package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SessionMapperTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @Autowired
    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionMapper = new SessionMapperImpl();
    }

    @Test
    void testToEntity() {
        SessionDto sessionDto = new SessionDto(1L, "name", new Date(), 1L, "description", Arrays.asList(1L, 2L), null, null);
        Teacher teacher = new Teacher(1L, "teacherLastName", "teacherFirstName", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user21@test.com", "lastName2", "firstName2", "password456", false, null, null);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session session = sessionMapper.toEntity(sessionDto);

        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
    }

    @Test
    void testToDto() {
        Teacher teacher = new Teacher(1L, "teacherLastName", "teacherFirstName", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user21@test.com", "lastName2", "firstName2", "password456", false, null, null);
        Session session = new Session(1L, "name", new Date(), "description", teacher, Arrays.asList(user1, user2), null, null);

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
    }
}