package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .admin(false)
                .build();

        session = Session.builder()
                .id(1L)
                .name("Yoga Class")
                .description("A relaxing yoga session")
                .users(new ArrayList<>())
                .build();

        session.getUsers().add(user);
    }

    @Test
    public void testCreate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getId()).isEqualTo(session.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDelete() {
        doNothing().when(sessionRepository).deleteById(anyLong());

        sessionService.delete(session.getId());

        verify(sessionRepository, times(1)).deleteById(session.getId());
    }

    @Test
    public void testFindAll() {
        List<Session> sessions = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        assertThat(foundSessions)
                .isNotEmpty()
                .extracting("id", "name", "description", "users")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(
                                session.getId(),
                                session.getName(),
                                session.getDescription(),
                                session.getUsers()
                        )
                        // ... We can add more tuples if we have more sessions
                );

        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetById_Success() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(session.getId());

        assertThat(foundSession)
                .isNotNull()
                .extracting("id", "name", "description", "users")
                .containsExactly(foundSession.getId(), foundSession.getName(), foundSession.getDescription(), foundSession.getUsers());

        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    public void testGetById_NotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Session foundSession = sessionService.getById(session.getId());

        assertThat(foundSession).isNull();
        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    public void testUpdate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session updatedSession = sessionService.update(session.getId(), session);

        assertThat(updatedSession)
                .isNotNull()
                .extracting("id", "name", "description", "users")
                .containsExactly(updatedSession.getId(), updatedSession.getName(), updatedSession.getDescription(), updatedSession.getUsers());

        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipate_Success() {
        session.getUsers().remove(user);

        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.participate(session.getId(), user.getId());

        assertThat(session.getUsers()).contains(user);

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipate_SessionNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.participate(session.getId(), user.getId()))
                .isInstanceOf(NotFoundException.class);

        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    public void testParticipate_UserNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.participate(session.getId(), user.getId()))
                .isInstanceOf(NotFoundException.class);

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testParticipate_AlreadyParticipating() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> sessionService.participate(session.getId(), user.getId()))
                .isInstanceOf(BadRequestException.class);

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testNoLongerParticipate_Success() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.noLongerParticipate(session.getId(), user.getId());

        assertThat(session.getUsers()).doesNotContain(user);

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.noLongerParticipate(session.getId(), user.getId()))
                .isInstanceOf(NotFoundException.class);

        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    public void testNoLongerParticipate_NotParticipating() {
        session.setUsers(Collections.emptyList());
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> sessionService.noLongerParticipate(session.getId(), user.getId()))
                .isInstanceOf(BadRequestException.class);

        verify(sessionRepository, times(1)).findById(session.getId());
    }
}
