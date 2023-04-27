

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
//    private SimpleJpaRepository crudFindService;


    //덧글 생성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        //토큰 확인
        Users users = checkJwtToken(request);
        //게시글 존재 확인
        Crud crud = checkCrud(requestDto.getCrudId());
        // 요청받은 DTO로 DB에 저장할 객체 만들기
        Comment comment = new Comment(requestDto);
//        List<Comment> commentList = crud.getCommentList();
        //user값을 comment에 추가시켜줌
//        commentList.add(comment);
        //Crud값을 commnet에 추가
//        crud.addComment(commentList);

        comment.addUser(users);
        comment.addCrud(crud);

        commentRepository.save(comment);
//        crudRepository.save(crud);

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




/*package com.example.springlv3.service;

import com.example.springlv3.dto.*;
import com.example.springlv3.entity.*;
import com.example.springlv3.jwt.JwtUtil;
import com.example.springlv3.repository.CommentRepository;
import com.example.springlv3.repository.CrudRepository;
import com.example.springlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.ManagedEntity;
import org.hibernate.engine.spi.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static org.apache.logging.log4j.ThreadContext.isEmpty;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CrudRepository crudRepository;
    private final JwtUtil jwtUtil;
    private com.example.springlv3.entity.StatusEnum StatusEnum;


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
    public ResponseEntity<CommentResponseDto> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Comment comment = checkComment(id);
        if(!comment.getUsers().getUsername().equals(users.getUsername())){
            //throw new IllegalArgumentException(statusDto.toString());
            StatusDto statusDto = StatusDto.setFail(HttpStatus.BAD_REQUEST.value(), "작성자만 수정할 수 있습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommentResponseDto());
        }
        isCommentUsers(users, comment);
        comment.update(requestDto);
        return ResponseEntity.ok(new CommentResponseDto(comment));
    }

//    @Transactional
//    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
//        Users users = checkJwtToken(request);
//        //게시글 체크
//        Comment comment = checkComment(id);
//        //권한 체크
//        isCommentUsers(users, comment);
//        comment.update(requestDto);
//        return new CommentResponseDto(comment);
//    }

    //삭제
    @Transactional
    public MsgResponseDto deleteComment(Long id, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Comment comment = checkComment(id);
        //권한 체크
        if(!comment.getUsers().getUsername().equals(users.getUsername())){
            String message = "작성자만 삭제할 수 있습니다";
            StatusDto statusDto = StatusDto.setFail(HttpStatus.BAD_REQUEST.value(), message);
            throw new RuntimeException(statusDto.toString());
        }
        //isCommentUsers(users, comment);
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
    public void isCommentUsers(Users users, Comment comment){
        //게시글에 있는 작성자 / JWT토큰의 작성자와 똑같은지 비교 // JWT토큰이 admin이 아니라면
        if(!comment.getUsers().getUsername().equals(users.getUsername()) && !users.getRole().equals(UserRoleEnum.ADMIN))
            throw new IllegalArgumentException("권한이 없습니다.");
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

 */