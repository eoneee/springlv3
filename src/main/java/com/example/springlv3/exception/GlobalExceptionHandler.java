package com.example.springlv3.exception;

import com.example.springlv3.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseDto> handleCustomException(final CustomException e){
        log.error("handleCustomException : {}", e.getStatusEnum());
        return ResponseEntity
                .status(e.getStatusEnum().getStatus().value())
                .body(new ErrorResponseDto(e.getStatusEnum()));
    }

}