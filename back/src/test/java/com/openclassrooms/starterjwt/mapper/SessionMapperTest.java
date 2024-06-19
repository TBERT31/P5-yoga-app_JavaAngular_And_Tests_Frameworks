package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.tuple;
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
        Teacher teacher1 = new Teacher(1L, "teacherLastName", "teacherFirstName", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user2@test.com", "lastName2", "firstName2", "password456", false, null, null);

        // Mock behavior of teacherService
        when(teacherService.findById(1L)).thenReturn(teacher1);

        // Mock behavior of userService
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Test
        Session session = sessionMapper.toEntity(sessionDto);

        // Create maps to get Teacher and User objects by ID
        Map<Long, Teacher> teacherMap = Stream.of(teacher1)
                .collect(Collectors.toMap(Teacher::getId, teacher -> teacher));
        Map<Long, User> userMap = Stream.of(user1, user2)
                .collect(Collectors.toMap(User::getId, user -> user));

        // Assertions
        assertThat(session)
                .isNotNull()
                .extracting("id", "name", "description", "teacher", "users")
                .containsExactlyInAnyOrder(
                        sessionDto.getId(),
                        sessionDto.getName(),
                        sessionDto.getDescription(),
                        teacherMap.get(sessionDto.getTeacher_id()),
                        sessionDto.getUsers().stream().map(userMap::get).collect(Collectors.toList())
                );
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
        assertThat(sessionDto)
                .isNotNull()
                .extracting("id", "name", "description", "teacher_id", "users")
                .containsExactlyInAnyOrder(
                        session.getId(),
                        session.getName(),
                        session.getDescription(),
                        session.getTeacher().getId(),
                        session.getUsers().stream().map(User::getId).collect(Collectors.toList())
                );
    }

    @Test
    void testToListEntity() {
        // Mock data
        Teacher teacher1 = new Teacher(1L, "teacherLastName1", "teacherFirstName1", null, null);
        Teacher teacher2 = new Teacher(2L, "teacherLastName2", "teacherFirstName2", null, null);
        User user1 = new User(1L, "user1@test.com", "lastName1", "firstName1", "password123", false, null, null);
        User user2 = new User(2L, "user2@test.com", "lastName2", "firstName2", "password456", false, null, null);

        SessionDto sessionDto1 = new SessionDto(1L, "name1", new Date(), 1L, "description1", Arrays.asList(1L, 2L), null, null);
        SessionDto sessionDto2 = new SessionDto(2L, "name2", new Date(), 2L, "description2", Arrays.asList(1L, 2L), null, null);

        // Mock behavior of teacherService
        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherService.findById(2L)).thenReturn(teacher2);

        // Mock behavior of userService
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Test
        List<Session> sessions = sessionMapper.toEntity(Arrays.asList(sessionDto1, sessionDto2));

        // Create maps to get Teacher and User objects by ID
        Map<Long, Teacher> teacherMap = Stream.of(teacher1, teacher2)
                .collect(Collectors.toMap(Teacher::getId, teacher -> teacher));
        Map<Long, User> userMap = Stream.of(user1, user2)
                .collect(Collectors.toMap(User::getId, user -> user));

        // Assertions
        assertThat(sessions)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "name", "description", "teacher", "users")
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(sessionDto1, sessionDto2)
                                .map(sessionDto -> tuple(
                                        sessionDto.getId(),
                                        sessionDto.getName(),
                                        sessionDto.getDescription(),
                                        teacherMap.get(sessionDto.getTeacher_id()),
                                        sessionDto.getUsers().stream().map(userMap::get).collect(Collectors.toList())
                                ))
                                .collect(Collectors.toList())
                );
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
        assertThat(sessionDtos)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "name", "description", "teacher_id", "users")
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(session1, session2)
                                .map(session -> tuple(
                                        session.getId(),
                                        session.getName(),
                                        session.getDescription(),
                                        session.getTeacher().getId(),
                                        session.getUsers().stream().map(User::getId).collect(Collectors.toList())
                                ))
                                .collect(Collectors.toList())
                );
    }

    @Test
    void testToEntity_NullDtoList() {
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);
        assertThat(sessions).isNull();
    }

    @Test
    void testToDto_NullEntityList() {
        List<SessionDto> sessionDtos = sessionMapper.toDto((List<Session>) null);
        assertThat(sessionDtos).isNull();
    }

    @Test
    void testToEntity_NullSessionDto() {
        Session session = sessionMapper.toEntity((SessionDto) null);
        assertThat(session).isNull();
    }

    @Test
    void testToDto_NullSession() {
        SessionDto sessionDto = sessionMapper.toDto((Session) null);
        assertThat(sessionDto).isNull();
    }
}