package com.rochards.beerstock.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // don't show null fields
public class APIError {

    private OffsetDateTime timestamp;
    private Integer status;
    private String message;
    private List<String> errors;

    public APIError(OffsetDateTime timestamp, Integer status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
