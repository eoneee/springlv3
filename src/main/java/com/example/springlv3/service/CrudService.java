package com.example.springlv3.service;

import com.example.springlv3.dto.CrudRequestDto;
import com.example.springlv3.dto.CrudResponseDto;
import com.example.springlv3.dto.MsgResponseDto;
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
        Crud crud = new Crud(requestDto, users);
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
        return crudRepository.findAllByOrderByCreatedAtDesc().stream().map(CrudResponseDto::new).collect(Collectors.toList());


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
        Crud crud = crudRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시글이 없습니다.")
        );

//        if(users.getRole()==UserRoleEnum.ADMIN){
//            crud.update(requestDto);
//        }else{
//            throw new NoSuchElementException("권한 없음");
//        }

//            Crud crud = crudRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
//                () -> new IllegalArgumentException("권한이 없습니다.")
//            );
//            if (user.getRole() == UserRoleEnum.ADMIN) {
//                crud.update(requestDto);
//                return new CrudResponseDto(crud);
//
//            }else crud.update(requestDto);
        return new CrudResponseDto(crud);
    }
/*
*
* if(boardRepository.findBoardByIdAndUsername(id, user.getUsername()).isPresent()){
board.update(requestDto);
}else if(user.getRole() == UserRoleEnum.ADMIN){
board.update(requestDto);
}else{
throw new NoSuchElementException("해당 게시글을 수정할 권한이 없습니다.");
}*/


    //삭제
    @Transactional
//    public ResponseEntity<MsgResponseDto> deleteCrud(Long id, HttpServletRequest request) {
    public MsgResponseDto deleteCrud(Long id, HttpServletRequest request) {
        /*
            String token = jwtUtil.resolveToken(request);
            Claims claims;

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("Token Error");
                }

                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
                );


*/

//            Crud crud = crudRepository.findById(id).orElseThrow(
//
//                    ()-> new IllegalArgumentException("글이 존재하지 않습니다.")
//            );
//            if(user.getRole() == UserRoleEnum.ADMIN){
//                crudRepository.deleteById(id);
//            }else{
        Users users = checkJwtToken(request);
        log.info("-------두둥등장------"+ users.getUsername());
        log.info("-------두둥등장------"+ users.getRole());
        Crud crud = checkCrud(id);
        log.info("-----두둥등장------"+crud.getId());

//        crudRepository.deleteById(id)
//        if (users.getRole().equals(UserRoleEnum.USER)){
//            log.info("======권한======"+ users.getRole());
//            log.info("======권한======"+UserRoleEnum.USER);
//            log.info("======권한======"+id);
//        }
        crudRepository.deleteById(id);
//        else if (user.getRole().equals(UserRoleEnum.ADMIN)) {
//            crudRepository.deleteById(id);
//        } else {
//            throw new NoSuchElementException("권한 없음");

//                    Crud crud = crudRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
//                            () -> new IllegalArgumentException("권한이 없습니다.")
//                    );
//                    crudRepository.deleteById(id);
//
//                return new MsgResponseDto("게시글 삭제 성공", HttpStatus.OK.value());
////         }
//            } else {
//                return new MsgResponseDto("게시글 작성자만 삭제 가능, 권한 없음", HttpStatus.OK.value());
//        }
//        return new ResponseEntity<"게시글삭제성공">;
//        return new ResponseEntity<"게시글 삭제 성공",HttpStatus.OK.value()>;
        return new MsgResponseDto("게시글 삭제 성공",HttpStatus.OK.value());
    }



    private Crud checkCrud(Long id) {
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new NullPointerException("글이 존재하지 않습니다.")
        );
        return crud;
    }

    public CrudResponseDto getCrudByTitle(String title) {
        Crud crud = crudRepository.findByTitle(title).orElseThrow(
                () -> new NullPointerException("해당하는 제목의 글이 없습니다.")
        );
        return new CrudResponseDto(crud);
    }


//    public User checkJwtToken(HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//        // 토큰이 있는 경우에만 게시글 접근 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            return user;
//        }
//        return null;
//    }
    
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
                () -> new IllegalArgumentException("사용자가 없습니다.")
        );
    }

}
