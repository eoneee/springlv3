package com.example.springlv3.controller;
import com.example.springlv3.dto.CrudAndComment;
import com.example.springlv3.dto.CrudRequestDto;
import com.example.springlv3.dto.CrudResponseDto;
import com.example.springlv3.dto.MsgResponseDto;
import com.example.springlv3.entity.UserRoleEnum;
import com.example.springlv3.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
@RestController
@RequiredArgsConstructor
//클라이언츠의 요청을 하나씩 연결해줌
@RequestMapping("/api")
public class CrudController {

    private final CrudService crudService;

    //글 생성하기
    @PostMapping("/post")
    public CrudResponseDto createCrud(@RequestBody CrudRequestDto requestDto, HttpServletRequest request) {
        //브라우저에서 요청해온 데이터를 잘 불러와서 서비스에 던져줌
        return crudService.createCrud(requestDto, request);
    }

    //메인 페이지
    @GetMapping("/posts")
    public List<CrudResponseDto> list(){
        return crudService.getCrudList();
    }

    //전체목록 말고 하나씩 보기
    @GetMapping("/post/{id}")
    public CrudResponseDto getCrud(@PathVariable Long id) {
        return crudService.getCrud(id);
    }


    //수정하기
    @PutMapping("/post/{id}")
    public ResponseEntity updateCrud(@PathVariable Long id, @RequestBody CrudRequestDto requestDto, HttpServletRequest request) {
        return crudService.updateCrud(id,requestDto,request);
    }

    //삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity deleteCrud(@PathVariable Long id, HttpServletRequest request) {
        return crudService.deleteCrud(id,request);

    }


}
