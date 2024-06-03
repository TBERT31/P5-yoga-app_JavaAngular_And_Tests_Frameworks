package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_TeacherExists() {
        // Given
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Configure mock service and mapper
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(teacherDto);

        // When
        ResponseEntity<?> responseEntity = teacherController.findById("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(TeacherDto.class);
        TeacherDto responseDto = (TeacherDto) responseEntity.getBody();
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getLastName()).isEqualTo("Doe");
        assertThat(responseDto.getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindById_TeacherDoesNotExist() {
        // Given
        when(teacherService.findById(1L)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = teacherController.findById("1");

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void testFindAll() {
        // Given
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        TeacherDto teacherDto1 = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto teacherDto2 = new TeacherDto(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        // Configure mock service and mapper
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // When
        ResponseEntity<?> responseEntity = teacherController.findAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(List.class);
        List<TeacherDto> responseDtos = (List<TeacherDto>) responseEntity.getBody();
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getId()).isEqualTo(1L);
        assertThat(responseDtos.get(0).getLastName()).isEqualTo("Doe");
        assertThat(responseDtos.get(0).getFirstName()).isEqualTo("John");
        assertThat(responseDtos.get(1).getId()).isEqualTo(2L);
        assertThat(responseDtos.get(1).getLastName()).isEqualTo("Smith");
        assertThat(responseDtos.get(1).getFirstName()).isEqualTo("Jane");
    }
}
