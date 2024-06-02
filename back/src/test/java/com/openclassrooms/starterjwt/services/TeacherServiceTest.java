package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    public void testFindAll() {
        List<Teacher> teachers = Arrays.asList(teacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> foundTeachers = teacherService.findAll();

        assertThat(foundTeachers).isNotNull();
        assertThat(foundTeachers).hasSize(1);
        assertThat(foundTeachers.get(0).getId()).isEqualTo(teacher.getId());
        assertThat(foundTeachers.get(0).getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(foundTeachers.get(0).getLastName()).isEqualTo(teacher.getLastName());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        Teacher foundTeacher = teacherService.findById(teacher.getId());

        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getId()).isEqualTo(teacher.getId());
        assertThat(foundTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(foundTeacher.getLastName()).isEqualTo(teacher.getLastName());
        verify(teacherRepository, times(1)).findById(teacher.getId());
    }

    @Test
    public void testFindById_NotFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        Teacher foundTeacher = teacherService.findById(teacher.getId());

        assertThat(foundTeacher).isNull();
        verify(teacherRepository, times(1)).findById(teacher.getId());
    }
}
