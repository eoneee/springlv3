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
//Spring MVC Controller에 @ResponseBody가 추가 됨
//@RestController는 클래스의 각 메서드마다 @ResponseBody를 추가할 필요가 없어짐(@Controller와의 차이점)
@RequiredArgsConstructor
//@RequiredArgsConstructor : 생성자 주입(final 이나 Notnull이 붙은 필드의 생성자를 자동생성 해줌)
//새로운 필드를 추가할 때 다시 생성자를 만들어서 관리해야 하는 번거로움을 없애줌(@Autowired사용x)
@RequestMapping("/api")
//@RequestMapping : controller에서 사용됨
//요청이 왔을 때 어떤 컨트롤러가 호출되어야 하는지 알려줌
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
        //List로 선언(글들을 list해서 가져옴)
        return crudService.getCrudList();
    }

    //전체목록 말고 하나씩 보기
    @GetMapping("/post/{id}")
    public CrudResponseDto getCrud(@PathVariable Long id) {
        return crudService.getCrud(id);
        // service에서 id를 받아서 확인후 response를 가져옴

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
