package com.example.springlv3.exception;

import com.example.springlv3.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final StatusEnum statusEnum;
}
