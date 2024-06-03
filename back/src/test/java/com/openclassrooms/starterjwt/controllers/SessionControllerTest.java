package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_SessionExists() {
        // Given
        Session session = new Session(
                1L,
                "Session 1",
                new Date(),
                "Description of session 1",
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        SessionDto sessionDto = new SessionDto(
                1L,
                "Session 1",
                new Date(),
                null,
                "Description of session 1",
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Configure mock service and mapper
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        // When
        ResponseEntity<?> responseEntity = sessionController.findById("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(SessionDto.class);
        SessionDto responseDto = (SessionDto) responseEntity.getBody();
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("Session 1");
        assertThat(responseDto.getDescription()).isEqualTo("Description of session 1");
    }

    @Test
    void testFindById_SessionDoesNotExist() {
        // Given
        when(sessionService.getById(1L)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = sessionController.findById("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void testFindAll() {
        // Given
        Session session1 = new Session(1L, "Session 1", new Date(), "Description of session 1", null, null, LocalDateTime.now(), LocalDateTime.now());
        Session session2 = new Session(2L, "Session 2", new Date(), "Description of session 2", null, null, LocalDateTime.now(), LocalDateTime.now());

        List<Session> sessions = Arrays.asList(session1, session2);
        SessionDto sessionDto1 = new SessionDto(1L, "Session 1", new Date(), null, "Description of session 1", null, LocalDateTime.now(), LocalDateTime.now());
        SessionDto sessionDto2 = new SessionDto(2L, "Session 2", new Date(), null, "Description of session 2", null, LocalDateTime.now(), LocalDateTime.now());
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto1, sessionDto2);

        // Configure mock service and mapper
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // When
        ResponseEntity<?> responseEntity = sessionController.findAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(List.class);
        List<SessionDto> responseDtos = (List<SessionDto>) responseEntity.getBody();
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getId()).isEqualTo(1L);
        assertThat(responseDtos.get(0).getName()).isEqualTo("Session 1");
        assertThat(responseDtos.get(0).getDescription()).isEqualTo("Description of session 1");
        assertThat(responseDtos.get(1).getId()).isEqualTo(2L);
        assertThat(responseDtos.get(1).getName()).isEqualTo("Session 2");
        assertThat(responseDtos.get(1).getDescription()).isEqualTo("Description of session 2");
    }

    @Test
    void testCreate() {
        // Given
        SessionDto sessionDto = new SessionDto(null, "New Session", new Date(), null, "Description of new session", null, null, null);
        Session session = new Session(null, "New Session", new Date(), "Description of new session", null, null, null, null);
        Session createdSession = new Session(1L, "New Session", new Date(), "Description of new session", null, null, null, null);
        SessionDto createdSessionDto = new SessionDto(1L, "New Session", new Date(), null, "Description of new session", null, null, null);

        // Configure mock service and mapper
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(createdSession);
        when(sessionMapper.toDto(createdSession)).thenReturn(createdSessionDto);

        // When
        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(SessionDto.class);
        SessionDto responseDto = (SessionDto) responseEntity.getBody();
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("New Session");
        assertThat(responseDto.getDescription()).isEqualTo("Description of new session");
    }

    @Test
    void testUpdate_SessionExists() {
        // Given
        SessionDto sessionDto = new SessionDto(1L, "Updated Session", new Date(), null, "Updated description", null, LocalDateTime.now(), LocalDateTime.now());
        Session session = new Session(1L, "Session", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());
        Session updatedSession = new Session(1L, "Updated Session", new Date(), "Updated description", null, null, LocalDateTime.now(), LocalDateTime.now());
        SessionDto updatedSessionDto = new SessionDto(1L, "Updated Session", new Date(), null, "Updated description", null, LocalDateTime.now(), LocalDateTime.now());

        // Configure mock service and mapper
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(updatedSession);
        when(sessionMapper.toDto(updatedSession)).thenReturn(updatedSessionDto);

        // When
        ResponseEntity<?> responseEntity = sessionController.update("1", sessionDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(SessionDto.class);
        SessionDto responseDto = (SessionDto) responseEntity.getBody();
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("Updated Session");
        assertThat(responseDto.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testUpdate_SessionDoesNotExist() {
        // Given
        SessionDto sessionDto = new SessionDto(
                1L,
                "Updated Session",
                new Date(),
                null,
                "Updated description",
                null,
                null,
                null
        );


        // Configure mock service and mapper
        when(sessionMapper.toEntity(sessionDto)).thenReturn(null);
        when(sessionService.update(
                1L,
                new Session(
                        1L,
                        "Updated Session",
                        new Date(),
                        "Updated description",
                        null,
                        null,
                        null,
                        null))
        ).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = sessionController.update("1", sessionDto);

        // Then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void testDelete_SessionExists() {
        // Given
        Session session = new Session(1L, "Session 1", new Date(), "Description of session 1", null, null, LocalDateTime.now(), LocalDateTime.now());

        // Configure mock service
        when(sessionService.getById(1L)).thenReturn(session);

        // When
        ResponseEntity<?> responseEntity = sessionController.save("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    void testDelete_SessionDoesNotExist() {
        // Given
        when(sessionService.getById(1L)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = sessionController.save("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(sessionService, never()).delete(any());
    }

    @Test
    void testParticipate() {
        // Given
        doNothing().when(sessionService).participate(1L, 1L);

        // When
        ResponseEntity<?> responseEntity = sessionController.participate("1", "1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @Test
    void testNoLongerParticipate() {
        // Given
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        // When
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("1", "1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).participate(1L, 1L);
    }
}
