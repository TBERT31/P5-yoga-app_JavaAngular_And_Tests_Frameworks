package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class BadRequestExceptionTest {

    @Test
    public void testBadRequestException() {
        BadRequestException exception = new BadRequestException();
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);

        assertThat(responseStatus).isNotNull();
        assertThat(responseStatus.value()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
