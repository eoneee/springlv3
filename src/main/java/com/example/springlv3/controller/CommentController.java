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
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comment")
    public CommentResponseDto createBoard(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    //댓글 수정
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateBoard(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
   }

    //댓글 삭제
    @DeleteMapping("/comment/{id}")
   public MsgResponseDto deleteBoard(@PathVariable Long id, HttpServletRequest request) {
       return commentService.deleteComment(id, request);
    }
}