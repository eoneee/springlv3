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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrudService {

    private final CrudRepository crudRepository;
    private final CrudRequestDto crudRequestDto;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CommentRepository commentRepository;

    //글 생성하기
    @Transactional
    public CrudResponseDto createCrud(CrudRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //요청받은 dto로 db에 저장할 객체 Crud crud 만들기
        Crud crud = new Crud(requestDto);
        crud.addUser(users);
        crudRepository.save(crud);
        //브라우저에서 받아온 데이터를 저장하기 위해서 crud객체로 변환
        return new CrudResponseDto(crud);
    }
/*
    //메인 페이지
    @Transactional(readOnly = true) //JPA를 사용할 경우, 변경감지 작업을 수행하지 않아서 성능이점 있음
    public ResponseEntity getCrudList() {

        List<CrudResponseDto> crudList = crudRepository.findAllByOrderByModifiedAtDesc().stream().map(CrudResponseDto::new).collect(Collectors.toList());
                StatusDto statusDto = StatusDto.setSuccess(HttpStatus.OK.value(), "목록 조회 성공", crudList);
                return new ResponseEntity(statusDto, HttpStatus.OK);
        }
*/
    public List<CrudResponseDto> getCrudList() {  // 데이터 베이스에 저장 된 전체 게시물 전부다 가져오는 API
        // 테이블에 저장 되어 있는 모든 게시물 목록 조회
        List<Crud> crudList = crudRepository.findAllByOrderByCreatedAtDesc();
//        List<Comment> commentList = commentRepository.findAllByOrderByCreatedAtDesc();

        // DB 에서 가져온 것
        return crudList.stream().map(CrudResponseDto::new).collect(Collectors.toList());
//        List<CrudAndComment> lists = new ArrayList();

//        for(Crud crud : crudList){
//            lists.add(new CrudResponseDto(crud));
            //for 달아서 new CrudResponseDto(crud).getCommentList()에서 하나씩 꺼내서 lists에 추가
//            for(Comment comment : new CrudResponseDto(crud).getCommentList()) {
//                lists.add(comment); //여기 잠깐 주석
//
//            }


//            lists.add(new CrudResponseDto(crud).getCommentList()); //여기 잠깐 주석
//            for(Comment comment : commentList){
//                if(comment.getCrud().getId() == crud.getId()){
//                    lists.add(new CrudResponseDto(comment));
//                }
//        }

//        return lists;
    }
    //
//            for (Crud crud : cruds) {
//                List<Comment> comments = commentRepository.findAllByCrud(crud);
//                if(comments.isEmpty()){
//                    crudListResponseDto.add(new CommentResponseDto(crud));
//                }else{
//                    crudListResponseDto.add(new CommentResponseDto(crud, (ArrayList<Comment>) comments));
//                }
////                crud.addComment(comments);
////                CrudResponseDtoList.add(new CrudResponseDto(crud));
//            }
//
//        //테이블에 저장되어있는 모든 글을 조회
//        //내림차순
//        return crudRepository.findAllByOrderByModifiedAtDesc().stream()
//                .map(CrudResponseDto::new)
//                .collect(Collectors.toList());




    //전체목록 말고 하나씩 보기
    @Transactional(readOnly = true)
    public CrudResponseDto getCrud(Long id) {
        //조회하기 위해 받아온 crud의 id를 사용해서 해당 crud인스턴스가 테이블에 존재 하는지 확인하고 가져오기
        //Crud crud = table.get(id);->repository한테서 id를 가져오면 됨
        Crud crud = checkCrud(id);
        return new CrudResponseDto(crud);
    }

    //수정하기
    @Transactional
    public ResponseEntity updateCrud(Long id, CrudRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        // 게시글 체크
        Crud crud = checkCrud(id);
        // 권한 체크
        isCrudUsers(users, crud);

        crud.update(requestDto);
        StatusDto statusDto = StatusDto.setSuccess(HttpStatus.OK.value(), "수정 성공", requestDto);
        return new ResponseEntity(statusDto, HttpStatus.OK);
    }

    //삭제
    @Transactional
//    public ResponseEntity<MsgResponseDto> deleteCrud(Long id, HttpServletRequest request) {
    public ResponseEntity deleteCrud(Long id, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        // 게시글 체크
        Crud crud = checkCrud(id);
        // 권한 체크
        isCrudUsers(users, crud);
        crudRepository.deleteById(id);
        StatusDto statusDto = StatusDto.setSuccess(HttpStatus.OK.value(), "삭제 성공", null);
        return new ResponseEntity(statusDto, HttpStatus.OK);
    }



    //글 존재 여부 확인
    private Crud checkCrud(Long id) {
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_EXIST_CRUD)
        );
        return crud;
    }

    //권한 여부
    private void isCrudUsers(Users users, Crud crud){
        if(!crud.getUsers().getUsername().equals(users.getUsername()) && !users.getRole().equals(UserRoleEnum.ADMIN)){
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

}





/*
package com.example.springlv3.service;

import com.example.springlv3.dto.CrudRequestDto;
import com.example.springlv3.dto.CrudResponseDto;
import com.example.springlv3.dto.MsgResponseDto;
import com.example.springlv3.dto.StatusDto;
import com.example.springlv3.entity.Crud;
import com.example.springlv3.entity.Users;
import com.example.springlv3.entity.UserRoleEnum;
import com.example.springlv3.jwt.JwtUtil;
import com.example.springlv3.repository.CrudRepository;
import com.example.springlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrudService {

    private final CrudRepository crudRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //글 생성하기
    @Transactional
    public CrudResponseDto createCrud(CrudRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //요청받은 dto로 db에 저장할 객체 Crud crud 만들기
        Crud crud = new Crud(requestDto);
        crud.addUser(users);
        crudRepository.save(crud);
        //브라우저에서 받아온 데이터를 저장하기 위해서 crud객체로 변환
        return new CrudResponseDto(crud);
    }

    //메인 페이지
    @Transactional(readOnly = true) //JPA를 사용할 경우, 변경감지 작업을 수행하지 않아서 성능이점 있음
    public List<CrudResponseDto> getCrudList() {
        //테이블에 저장되어있는 모든 글을 조회
        //내림차순
        return crudRepository.findAllByOrderByModifiedAtDesc().stream().map(CrudResponseDto::new).collect(Collectors.toList());


    }


    //전체목록 말고 하나씩 보기
    @Transactional(readOnly = true)
    public CrudResponseDto getCrud(Long id) {
        //조회하기 위해 받아온 crud의 id를 사용해서 해당 crud인스턴스가 테이블에 존재 하는지 확인하고 가져오기
        //Crud crud = table.get(id);->repository한테서 id를 가져오면 됨
        Crud crud = checkCrud(id);
        return new CrudResponseDto(crud);
    }

    //수정하기
    @Transactional
    public CrudResponseDto updateCrud(Long id, CrudRequestDto requestDto, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Crud crud = checkCrud(id);
        //권한 체크
        if(!crud.getUsers().getUsername().equals(users.getUsername())){
            String message = "수정 권한이 없습니다.";
            StatusDto statusDto = StatusDto.setFail(HttpStatus.BAD_REQUEST.value(), message);
            throw new IllegalArgumentException(statusDto.toString());
        }
        //isCrudUsers(users, crud);
        crud.update(requestDto);
        return new CrudResponseDto(crud);
    }

    //삭제
    @Transactional
//    public ResponseEntity<MsgResponseDto> deleteCrud(Long id, HttpServletRequest request) {
    public MsgResponseDto deleteCrud(Long id, HttpServletRequest request) {
        Users users = checkJwtToken(request);
        //게시글 체크
        Crud crud = checkCrud(id);
        //권한 체크
        if(!crud.getUsers().getUsername().equals(users.getUsername())){
            String message = "삭제 권한이 없습니다.";
            StatusDto statusDto = StatusDto.setFail(HttpStatus.BAD_REQUEST.value(), message);
            throw new IllegalArgumentException(statusDto.toString());
        }
        //isCrudUsers(users,crud);
        crudRepository.deleteById(id);
        return new MsgResponseDto("게시글 삭제 성공",HttpStatus.OK.value());
    }



    //글 존재 여부 확인
    private Crud checkCrud(Long id){
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new NullPointerException("글이 존재하지 않습니다.")
        );
        return crud;
    }

    //권한 여부
    private void isCrudUsers(Users users, Crud crud){
        //게시글에 있는 작성자 / JWT토큰의 작성자와 똑같은지 비교 // JWT토큰이 admin이 아니라면
        if(!crud.getUsers().getUsername().equals(users.getUsername()) && !users.getRole().equals(UserRoleEnum.ADMIN)){
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
            throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
        }

        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
    }

}
*/