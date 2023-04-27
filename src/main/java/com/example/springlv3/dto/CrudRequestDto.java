package com.example.springlv3.dto;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CrudRequestDto {
    private String title;
    private String content;
}
