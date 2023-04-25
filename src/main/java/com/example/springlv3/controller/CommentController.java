package com.example.springlv3.controller;

import com.example.springlv3.dto.*;
import com.example.springlv3.service.CommentService;
import com.example.springlv3.service.CrudService;
import com.example.springlv3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//Json형태의 객체반환
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
//
//    //게시
//    @PostMapping("/comments")
//    public CommentResponseDto createBoard(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
//        return commentService.create(requestDto, request);
//    }
//
//    //수정
//    @PutMapping("/comments/{id}")
//    public CommentResponseDto updateBoard(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
//        return commentService.update(id, requestDto, request);
//    }
//
//    //삭제
//    @DeleteMapping("/comments/{id}")
//    public CommentResponseDto deleteBoard(@PathVariable Long id, HttpServletRequest request) {
//        return commentService.deleteCrud(id, request);
//    }
}