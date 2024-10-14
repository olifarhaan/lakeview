package com.olifarhaan.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This model class is used to send error messages to the client that we encounter during the spring security process
 * rest of the exceptions are handled by the @CustomExceptionHandler
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorMessage {
    private int status;
    private String error;
    private String message;
}
