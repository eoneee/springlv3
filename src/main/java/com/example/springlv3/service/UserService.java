package com.example.springlv3.service;

import com.example.springlv3.dto.LoginRequestDto;
import com.example.springlv3.dto.SignupRequestDto;
import com.example.springlv3.entity.Users;
import com.example.springlv3.entity.UserRoleEnum;
import com.example.springlv3.jwt.JwtUtil;
import com.example.springlv3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.lang.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
//    public void ResponseEntity <MsgResponseDto> signup(SignupRequestDto signupRequestDto) {
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        // 회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("아이디가 이미 존재합니다.");
        }

        //사용자 Role 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(signupRequestDto.isAdmin()){
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){
                throw new IllegalArgumentException("관리자 권한이 일치하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        //아이디 정규식 확인
        if (!Pattern.matches("^[a-z0-9]{4,10}$", username)) {
//            return ResponseEntity.ok(new MsgResponseDto("아이디는 4자 이상, 10자 이하 알파벳 소문자, 숫자로만 이루어져야 합니다.",505));
            throw new IllegalArgumentException("아이디는 4자 이상, 10자 이하 알파벳 소문자, 숫자로만 이루어져야 합니다.");
        }
        //비밀번호 정규식 확인
        Optional<Users> pw = userRepository.findByUsername(password);
//        if (!Pattern.matches("^\\w{8,15}[$@$!%*#?&]$", password)) {
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}", password)) {
//            if (!Pattern.matches("^[a-z0-9]{8,15}[$@$!%*#?&]$", password)) {
//            return ResponseEntity.ok(new MsgResponseDto("비밀번호는 8자 이상, 15자 이하 알파벳 대/소문자, 숫자로만 이루어져야 합니다.",505));

            throw new IllegalArgumentException("비밀번호는 8자 이상, 15자 이하 알파벳 대/소문자, 숫자, 특수문자 로만 이루어져야 합니다.");
        }
        Users users = new Users(username, password, role);
        userRepository.save(users);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto,HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        Users users = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
        );

        // 비밀번호 확인
        if(!users.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(users.getUsername()));
    }
}