package com.clear_solutions.test_assignment.exception;

import com.clear_solutions.test_assignment.dto.ResponseMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({UserInvalidAgeException.class})
    public ResponseEntity<ResponseMessageDto> handleUserInvalidAgeException(UserInvalidAgeException userInvalidAgeException) {
        LOGGER.warn(userInvalidAgeException.getMessage(), userInvalidAgeException);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessageDto(userInvalidAgeException.getMessage()));
    }

    @ExceptionHandler({RangeDateException.class})
    public ResponseEntity<ResponseMessageDto> handleRangeDateException(RangeDateException rangeDateException) {
        LOGGER.warn(rangeDateException.getMessage(), rangeDateException);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessageDto(rangeDateException.getMessage()));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ResponseMessageDto> handleRangeDateException(ResourceNotFoundException resourceNotFoundException) {
        LOGGER.warn(resourceNotFoundException.getMessage(), resourceNotFoundException);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessageDto(resourceNotFoundException.getMessage()));
    }

}