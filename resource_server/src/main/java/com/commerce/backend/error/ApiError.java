package com.commerce.backend.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
class ApiError {
    private static final long serialVersionUID = 6877490277661133451L;

    private HttpStatus status;
    private Integer error;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }


    ApiError(HttpStatus status, String message, Integer error) {
        this();
        this.status = status;
        this.message = message;
        this.error = error;
    }
}