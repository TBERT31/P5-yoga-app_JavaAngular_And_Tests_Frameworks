package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(false)
                .build();
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testExistsByEmail() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(false)
                .build();
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByEmail_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");

        // Then
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void testExistsByEmail_NotFound() {
        // When
        boolean exists = userRepository.existsByEmail("notfound@example.com");

        // Then
        assertThat(exists).isFalse();
    }
}
