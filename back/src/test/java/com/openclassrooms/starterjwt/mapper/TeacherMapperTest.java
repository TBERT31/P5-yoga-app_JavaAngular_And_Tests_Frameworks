package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void testToEntity() {
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertThat(teacher)
                .isNotNull()
                .extracting("id", "firstName", "lastName", "createdAt", "updatedAt")
                .containsExactlyInAnyOrder(
                        teacherDto.getId(),
                        teacherDto.getFirstName(),
                        teacherDto.getLastName(),
                        teacherDto.getCreatedAt(),
                        teacherDto.getUpdatedAt()
                );
    }

    @Test
    void testToDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertThat(teacherDto)
                .isNotNull()
                .extracting("id", "firstName", "lastName", "createdAt", "updatedAt")
                .containsExactlyInAnyOrder(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.getCreatedAt(),
                        teacher.getUpdatedAt()
                );
    }

    @Test
    void testToEntityList() {
        TeacherDto teacherDto1 = new TeacherDto(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        TeacherDto teacherDto2 = new TeacherDto(
                2L,
                "Smith",
                "Jane",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtos);

        assertThat(teachers)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "firstName", "lastName", "createdAt", "updatedAt")
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(teacherDto1, teacherDto2)
                                .map(teacherDto -> tuple(
                                        teacherDto.getId(),
                                        teacherDto.getFirstName(),
                                        teacherDto.getLastName(),
                                        teacherDto.getCreatedAt(),
                                        teacherDto.getUpdatedAt()
                                ))
                                .collect(Collectors.toList())
                );
    }

    @Test
    void testToDtoList() {
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        assertThat(teacherDtos)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "firstName", "lastName", "createdAt", "updatedAt")
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(teacher1, teacher2)
                                .map(teacher -> tuple(
                                        teacher.getId(),
                                        teacher.getFirstName(),
                                        teacher.getLastName(),
                                        teacher.getCreatedAt(),
                                        teacher.getUpdatedAt()
                                ))
                                .collect(Collectors.toList())
                );
    }

    @Test
    void testToEntity_NullDtoList() {
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);
        assertThat(teachers).isNull();
    }

    @Test
    void testToDto_NullEntityList() {
        List<TeacherDto> teacherDtos = teacherMapper.toDto((List<Teacher>) null);
        assertThat(teacherDtos).isNull();
    }

    @Test
    void testToEntity_NullTeacherDto() {
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);
        assertThat(teacher).isNull();
    }

    @Test
    void testToDto_NullTeacher() {
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);
        assertThat(teacherDto).isNull();
    }
}
