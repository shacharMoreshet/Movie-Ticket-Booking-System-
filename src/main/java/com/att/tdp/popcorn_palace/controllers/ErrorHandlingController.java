package com.att.tdp.popcorn_palace.controllers;

import com.att.tdp.popcorn_palace.exceptions.ObjectAlreadyExist;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Shachar Moreshet, 209129618.
 */
@RestControllerAdvice
public class ErrorHandlingController {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectAlreadyExist.class)
    public ResponseEntity<String> handleObjectAlreadyExist(ObjectAlreadyExist ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
