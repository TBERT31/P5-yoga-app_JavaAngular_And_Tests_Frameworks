package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;

public class BadRequestExceptionTest {

    @Test
    public void testBadRequestException() {
        BadRequestException exception = new BadRequestException();
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST);

        assertThat(responseStatusException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseStatusException.getReason()).isEqualTo(exception.getMessage());
    }
}