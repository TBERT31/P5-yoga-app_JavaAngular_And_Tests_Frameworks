package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity() {
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

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.isAdmin()).isEqualTo(userDto.isAdmin());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
        assertThat(user.getUpdatedAt()).isEqualTo(userDto.getUpdatedAt());
    }

    @Test
    void toDto() {
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

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.isAdmin()).isEqualTo(user.isAdmin());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDto.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(userDto.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void toEntityList() {
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

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(users.get(1).getEmail()).isEqualTo("test2@example.com");
    }

    @Test
    void toDtoList() {
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

        assertThat(userDtos).hasSize(2);
        assertThat(userDtos.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(userDtos.get(1).getEmail()).isEqualTo("test2@example.com");
    }
}
