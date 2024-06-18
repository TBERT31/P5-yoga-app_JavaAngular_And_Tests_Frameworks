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

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
@Transactional
public class TeacherTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testEquals() {
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher3 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1).isNotEqualTo(teacher3);
        assertThat(teacher1).isNotEqualTo(null);
        assertThat(teacher1).isNotEqualTo("string");
    }

    @Test
    public void testHashCode() {
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher3 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());

        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());
    }

    @Test
    public void testSetters() {
        Teacher teacher = new Teacher();

        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testCanEqual() {
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher3 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());

        assertThat(teacher1.canEqual(teacher2)).isTrue();
        assertThat(teacher1.canEqual(teacher3)).isTrue();
        assertThat(teacher1.canEqual("string")).isFalse();
    }

    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testEqualsAndHashCodeEdgeCases() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();

        // Both objects are new and have null ids
        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());

        // One object has an id, the other does not
        teacher1.setId(1L);
        assertThat(teacher1).isNotEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher2.hashCode());

        // Both objects have the same id
        teacher2.setId(1L);
        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());

        // Different ids
        teacher2.setId(2L);
        assertThat(teacher1).isNotEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher2.hashCode());
    }
}
