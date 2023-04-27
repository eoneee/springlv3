package com.example.springlv3.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.*;

@Getter
public class CrudListResponseDto extends MsgResponseDto{
    List<CommentResponseDto> crudList = new ArrayList<>();

    public CrudListResponseDto(String msg, int statusCode){
        super(msg, statusCode);
    }
    public void add(CommentResponseDto commentResponseDto){
        crudList.add(commentResponseDto);
    }
}
