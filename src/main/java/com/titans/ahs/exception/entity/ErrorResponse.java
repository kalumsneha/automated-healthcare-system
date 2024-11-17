package com.titans.ahs.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private List<String> errors;
    private LocalDate timestamp;
    private HttpStatusCode httpStatusCode;

    public ErrorResponse(HttpStatus status, String msg, String err, LocalDate ts) {
        this.httpStatus = status;
        this.message = msg;
        this.errors = Arrays.asList(err);
        this.timestamp = ts;

    }

    public ErrorResponse(HttpStatusCode statusCode, String msg, List err, LocalDate ts) {
        this.httpStatusCode = statusCode;
        this.message = msg;
        this.errors = err;
        this.timestamp = ts;

    }
}
