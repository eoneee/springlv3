package com.example.springlv3.dto;


import com.example.springlv3.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private int statusCode;
    private String error;
    private String msg;

    public ErrorResponseDto(StatusEnum statusEnum){
        this.statusCode= statusEnum.getStatus().value();
        this.error = statusEnum.getStatus().name();
        this.msg=statusEnum.getMsg();
    }
}
