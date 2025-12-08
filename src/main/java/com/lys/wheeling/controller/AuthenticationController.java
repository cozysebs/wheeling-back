package com.lys.wheeling.controller;

import com.lys.wheeling.domain.User;
import com.lys.wheeling.service.authentication.AuthenticationService;
import com.lys.wheeling.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("sign-up")     //회원가입
    public ResponseEntity<Object> signUp(@RequestBody User user) {
        if(userService.findByUsername(user.getUsername()) != null){     // DB에 중복된 데이터가 이미 있으면
            return new ResponseEntity<>(HttpStatus.CONFLICT);   // 409: 인증 실패
        }
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("sign-in")     //로그인
    public ResponseEntity<Object> signIn(@RequestBody User user) {
        return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
    }
}
