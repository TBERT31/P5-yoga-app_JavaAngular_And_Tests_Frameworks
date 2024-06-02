package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_UserExists() {
        // Given
        User user = new User(
                1L,
                "test@example.com",
                "Doe",
                "John",
                "password",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Configure mock mapper behavior
        when(userMapper.toDto(any(User.class)))
                .thenReturn(
                        new UserDto(
                                user.getId(),
                                user.getEmail(),
                                user.getLastName(),
                                user.getFirstName(),
                                user.isAdmin(),
                                user.getPassword(),
                                user.getCreatedAt(),
                                user.getUpdatedAt()
                        )
                );

        // Configure mock userService to return the user when findById is called with ID 1L
        when(userService.findById(1L)).thenReturn(user);

        // When
        ResponseEntity<?> responseEntity = userController.findById("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(UserDto.class);
        UserDto userDto = (UserDto) responseEntity.getBody();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getEmail()).isEqualTo("test@example.com");
        assertThat(userDto.getLastName()).isEqualTo("Doe");
        assertThat(userDto.getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindById_UserDoesNotExist() {
        // Given
        when(userService.findById(1L)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = userController.findById("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void testDelete_Success() {
        // Given
        User user = new User(1L,
                "test@example.com",
                "Doe",
                "John",
                "password",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.findById(1L)).thenReturn(user);

        // Create UserDetailsImpl object
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .admin(false)
                .build();

        // Mock authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        ResponseEntity<?> responseEntity = userController.save("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void testDelete_UserNotFound() {
        // Given
        when(userService.findById(1L)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = userController.save("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, never()).delete(any());
    }

    @Test
    void testDelete_Unauthorized() {
        // Given
        User user = new User(1L,
                "test@example.com",
                "Doe",
                "John",
                "password",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.findById(1L)).thenReturn(user);

        // Create UserDetailsImpl object for another user
        UserDetailsImpl anotherUserDetails = UserDetailsImpl.builder()
                .username("another_user@example.com")
                .password("password")
                .build();

        // Mock authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(anotherUserDetails, anotherUserDetails.getPassword(), anotherUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        ResponseEntity<?> responseEntity = userController.save("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(userService, never()).delete(any());
    }
}
