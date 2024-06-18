package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;

public class NotFoundExceptionTest {

    @Test
    public void testNotFoundException() {
        NotFoundException exception = new NotFoundException();
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.NOT_FOUND);

        assertThat(responseStatusException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseStatusException.getReason()).isEqualTo(exception.getMessage());
    }
}