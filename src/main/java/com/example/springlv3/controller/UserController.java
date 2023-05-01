package com.example.springlv3.controller;

import com.example.springlv3.dto.LoginRequestDto;
import com.example.springlv3.dto.MsgResponseDto;
import com.example.springlv3.dto.SignupRequestDto;
import com.example.springlv3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController //
//@Controller : Client로 부터 view반환,
//@RestController는 클래스의 각 메서드마다 @ResponseBody를 추가할 필요가 없어짐(@Controller와의 차이점)MVC Controller클래스 명시
@RequestMapping("/api/auth")
//기본 URL. 요청에 대해 어떤 Controller, 어떤메소드가 처리할지 알려줌
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<MsgResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        //의문 : signup에는 HttpServletRequest request가 왜 없는가?

        //ResponseEntity - 상태코드와 Json변환객체를 지정해줌
        //MsgResponseDto로 상태코드 설정
        userService.signup(signupRequestDto);
        return ResponseEntity.ok(new MsgResponseDto("회원가입 완료", HttpStatus.OK.value()));
        //200(OK)응답 코드와 데이터를 생성
    }

    @ResponseBody
    //View페이지가 아닌 반환값 그대로 클라이언트한테 return하고 싶은 경우
    @PostMapping("/login")
    public ResponseEntity<MsgResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new MsgResponseDto("로그인 완료",HttpStatus.OK.value()));
    }

}