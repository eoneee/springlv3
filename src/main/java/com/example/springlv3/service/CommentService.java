package com.example.springlv3.service;

import com.example.springlv3.dto.*;
import com.example.springlv3.entity.Comment;
import com.example.springlv3.entity.Crud;
import com.example.springlv3.entity.UserRoleEnum;
import com.example.springlv3.entity.Users;
import com.example.springlv3.jwt.JwtUtil;
import com.example.springlv3.repository.CommentRepository;
import com.example.springlv3.repository.CrudRepository;
import com.example.springlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CrudRepository crudRepository;
    private final JwtUtil jwtUtil;


    //덧글 생성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        //토큰 확인
        Users users = checkJwtToken(request);
        //게시글 존재 확인
        Crud crud = checkCrud(requestDto.getCrudId());
        // 요청받은 DTO로 DB에 저장할 객체 만들기
        Comment comment = new Comment(requestDto);
        //user값을 comment에 추가시켜줌
        comment.addUser(users);
        //Crud값을 commnet에 추가
        comment.addCrud(crud);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    //수정하기
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Comment comment = checkComment(id);
        //권한 체크
        isCommentUsers(users, comment);
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    //삭제
    @Transactional
    public MsgResponseDto deleteComment(Long id, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Comment comment = checkComment(id);
        //권한 체크
        isCommentUsers(users, comment);
        commentRepository.deleteById(id);;
        return new MsgResponseDto("댓글 삭제 성공", HttpStatus.OK.value());
    }

    //댓글 존재 여부 확인
    private Comment checkComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NullPointerException("글이 존재하지 않습니다.")
        );
        return comment;
    }

    //권한 여부
    private void isCommentUsers(Users users, Comment comment){
        //게시글에 있는 작성자 / JWT토큰의 작성자와 똑같은지 비교 // JWT토큰이 admin이 아니라면
        if(!comment.getUsers().getUsername().equals(users.getUsername()) && !users.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

    }

    //권한 확인
    private Users checkJwtToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰 검증
        if (jwtUtil.validateToken(token)) {
            // 토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else {
            throw new IllegalArgumentException("로그인 후 이용해 주세요");
        }

        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
    }

    private Crud checkCrud(Long id){
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new NullPointerException("글이 존재하지 않습니다.")
        );
        return crud;
    }
}