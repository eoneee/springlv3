package com.example.springlv3.service;

import com.example.springlv3.dto.*;
import com.example.springlv3.entity.*;
import com.example.springlv3.exception.CustomException;
import com.example.springlv3.jwt.JwtUtil;
import com.example.springlv3.repository.CommentRepository;
import com.example.springlv3.repository.CrudRepository;
import com.example.springlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        comment.addUser(users);
        comment.addCrud(crud);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    //수정하기
    @Transactional
    public ResponseEntity updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Comment comment = checkComment(id);
        //권한 체크
        isCommentUsers(users, comment);
        comment.update(requestDto);
        StatusDto statusDto = StatusDto.setSuccess(HttpStatus.OK.value(), "수정 성공", new CommentResponseDto(comment));
        return new ResponseEntity(statusDto,HttpStatus.OK);
    }

    //삭제
    @Transactional
    public ResponseEntity deleteComment(Long id, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Comment comment = checkComment(id);
        //권한 체크
        isCommentUsers(users, comment);
        commentRepository.deleteById(id);
        StatusDto statusDto = StatusDto.setSuccess(HttpStatus.OK.value(), "삭제 성공", null);
        return new ResponseEntity(statusDto, HttpStatus.OK);
    }


    //댓글 존재 여부 확인
    private Comment checkComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_EXIST_COMMENT)
        );
        return comment;
    }

    //권한 여부
    private void isCommentUsers(Users users, Comment comment){
        //게시글에 있는 작성자 / JWT토큰의 작성자와 똑같은지 비교 // JWT토큰이 admin이 아니라면
        if(!comment.getUsers().getUsername().equals(users.getUsername()) && !users.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(StatusEnum.NOT_AUTHENTICATION);
        }

    }

    //권한 확인

    private Users checkJwtToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰 검증
        if(token == null){
            throw new CustomException(StatusEnum.TOKEN_NULL);
        }
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(StatusEnum.TOKEN_ERROR);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 없습니다.")
        );
    }

    private Crud checkCrud(Long id){
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new NullPointerException("글이 존재하지 않습니다.")
        );
        return crud;
    }
}
