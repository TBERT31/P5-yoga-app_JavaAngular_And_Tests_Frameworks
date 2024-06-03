package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void toEntity() {
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
        assertThat(teacher.getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
    }

    @Test
    void toDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(teacherDto.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        assertThat(teacherDto.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }

    @Test
    void toEntityList() {
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

        assertThat(teachers).hasSize(2);
        assertThat(teachers.get(0).getLastName()).isEqualTo("Doe");
        assertThat(teachers.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void toDtoList() {
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

        assertThat(teacherDtos).hasSize(2);
        assertThat(teacherDtos.get(0).getLastName()).isEqualTo("Doe");
        assertThat(teacherDtos.get(1).getLastName()).isEqualTo("Smith");
    }
}
