package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
@Transactional
public class UserTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testEquals() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, now, now);
        User user2 = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, now, now);
        User user3 = new User(2L, "jane.smith@example.com", "Smith", "Jane", "password", false, now, now);

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo("string");
    }

    @Test
    public void testHashCode() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, now, now);
        User user2 = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, now, now);
        User user3 = new User(2L, "jane.smith@example.com", "Smith", "Jane", "password", false, now, now);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    public void testSetters() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testCanEqual() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, now, now);
        User user2 = new User(1L, "john.doe@example.com", "Doe", "John", "password", true, now, now);
        User user3 = new User(2L, "jane.smith@example.com", "Smith", "Jane", "password", false, now, now);

        assertThat(user1.canEqual(user2)).isTrue();
        assertThat(user1.canEqual(user3)).isTrue();
        assertThat(user1.canEqual("string")).isFalse();
    }

    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testEqualsAndHashCodeEdgeCases() {
        User user1 = new User();
        User user2 = new User();

        // Both objects are new and have null ids
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // One object has an id, the other does not
        user1.setId(1L);
        assertThat(user1).isNotEqualTo(user2);
        assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());

        // Both objects have the same id
        user2.setId(1L);
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // Different ids
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());
    }

    @Test
    public void testSetEmailNonNull() {
        User user = new User();

        // Attempting to set email to null should throw NullPointerException
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
    }

    @Test
    public void testSetLastNameNonNull() {
        User user = new User();

        // Attempting to set last name to null should throw NullPointerException
        assertThrows(NullPointerException.class, () -> user.setLastName(null));
    }

    @Test
    public void testSetFirstNameNonNull() {
        User user = new User();

        // Attempting to set first name to null should throw NullPointerException
        assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    }

    @Test
    public void testSetPasswordNonNull() {
        User user = new User();

        // Attempting to set password to null should throw NullPointerException
        assertThrows(NullPointerException.class, () -> user.setPassword(null));
    }

    @Test
    public void testToString() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "password", true, now, now);

        String expectedToString = "User(id=1, email=test@example.com, lastName=Doe, firstName=John, password=password, admin=true, createdAt=" + now + ", updatedAt=" + now + ")";
        assertThat(user.toString()).isEqualTo(expectedToString);
    }
}
