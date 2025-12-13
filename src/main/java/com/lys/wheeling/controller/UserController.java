package com.lys.wheeling.controller;

import com.lys.wheeling.domain.elist.Role;
import com.lys.wheeling.dto.UserProfileUpdateRequestDTO;
import com.lys.wheeling.dto.UserProfileViewDTO;
import com.lys.wheeling.security.UserPrincipal;
import com.lys.wheeling.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @PutMapping("change/{role}")
    public ResponseEntity<Object> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @PathVariable Role role) {
        userService.changeRole(role, userPrincipal.getUsername());
        return ResponseEntity.ok(true);
    }

    // About me 조회 (로그인 사용자 누구나)
    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfileViewDTO> getProfile(@AuthenticationPrincipal UserPrincipal viewer,
                                                         @PathVariable String username) {
        Long viewerId = (viewer != null) ? viewer.getUserId() : null;
        return ResponseEntity.ok(userService.getProfileByUsername(username, viewerId));
    }

    // 내 프로필 조회
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileViewDTO> getMyProfile(@AuthenticationPrincipal UserPrincipal me) {
        return ResponseEntity.ok(userService.getMyProfile(me.getUserId()));
    }

    // 내 프로필 수정
    @PutMapping("/me/profile")
    public ResponseEntity<UserProfileViewDTO> updateMyProfile(@AuthenticationPrincipal UserPrincipal me,
                                                              @RequestBody UserProfileUpdateRequestDTO req) {
        return ResponseEntity.ok(userService.updateMyProfile(me.getUserId(), req));
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Object> deleteMyAccount(@AuthenticationPrincipal UserPrincipal me) {
        userService.deleteMyAccount(me.getUserId());
        return ResponseEntity.ok(true);
    }
}
