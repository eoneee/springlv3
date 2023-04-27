package com.example.springlv3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "add")
public class StatusDto<T> {

    private int statusCode; // 필드명을 statusCode로 변경
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL) // null일 경우 반환할 때 보이지 않게 한다.
    private T data;

    public static <T> StatusDto<T> setSuccess(int statusCode, String message, T data){
        return StatusDto.add(statusCode, message, data);
    }

    public static <T> StatusDto<T> setFail(int statusCode, String message){
        return StatusDto.add(statusCode, message, null);
    }

}