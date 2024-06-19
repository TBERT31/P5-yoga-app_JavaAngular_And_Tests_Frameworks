package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity() {
        UserDto userDto = new UserDto(
                1L,
                "test@example.com",
                "Doe",
                "John",
                false,
                "password",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User user = userMapper.toEntity(userDto);

        assertThat(user)
                .isNotNull()
                .extracting("id", "email","firstName", "lastName", "password","createdAt", "updatedAt")
                .containsExactlyInAnyOrder(
                        userDto.getId(),
                        userDto.getEmail(),
                        userDto.getFirstName(),
                        userDto.getLastName(),
                        userDto.getPassword(),
                        userDto.getCreatedAt(),
                        userDto.getUpdatedAt()
                );
    }

    @Test
    void testToDto() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertThat(userDto)
                .isNotNull()
                .extracting("id", "email","firstName", "lastName", "password","createdAt", "updatedAt")
                .containsExactlyInAnyOrder(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPassword(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                );
    }

    @Test
    void testToEntityList() {
        UserDto userDto1 = new UserDto(
                1L,
                "test1@example.com",
                "Doe",
                "John",
                false,
                "password",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        UserDto userDto2 = new UserDto(
                2L,
                "test2@example.com",
                "Smith",
                "Jane",
                true,
                "password",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);
        List<User> users = userMapper.toEntity(userDtos);

        assertThat(users).isNotNull()
                .hasSize(2)
                .extracting("id", "email", "firstName", "lastName", "password","createdAt", "updatedAt")
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(userDto1, userDto2)
                                .map(userDto -> tuple(
                                        userDto.getId(),
                                        userDto.getEmail(),
                                        userDto.getFirstName(),
                                        userDto.getLastName(),
                                        userDto.getPassword(),
                                        userDto.getCreatedAt(),
                                        userDto.getUpdatedAt()
                                ))
                                .collect(Collectors.toList())
                );
    }

    @Test
    void testToDtoList() {
        User user1 = User.builder()
                .id(1L)
                .email("test1@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("test2@example.com")
                .lastName("Smith")
                .firstName("Jane")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> userDtos = userMapper.toDto(users);

        assertThat(userDtos)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "email", "firstName", "lastName", "password","createdAt", "updatedAt")
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(user1, user2)
                                .map(user -> tuple(
                                        user.getId(),
                                        user.getEmail(),
                                        user.getFirstName(),
                                        user.getLastName(),
                                        user.getPassword(),
                                        user.getCreatedAt(),
                                        user.getUpdatedAt()
                                ))
                                .collect(Collectors.toList())
                );
    }

    @Test
    void testToEntity_NullDtoList() {
        List<User> users = userMapper.toEntity((List<UserDto>) null);
        assertThat(users).isNull();
    }

    @Test
    void testToDto_NullEntityList() {
        List<UserDto> userDtos = userMapper.toDto((List<User>) null);
        assertThat(userDtos).isNull();
    }

    @Test
    void testToEntity_NullUserDto() {
        User user = userMapper.toEntity((UserDto) null);
        assertThat(user).isNull();
    }

    @Test
    void testToDto_NullUser() {
        UserDto userDto = userMapper.toDto((User) null);
        assertThat(userDto).isNull();
    }
}
