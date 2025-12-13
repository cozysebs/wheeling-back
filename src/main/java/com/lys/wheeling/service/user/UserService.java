package com.lys.wheeling.service.user;

import com.lys.wheeling.domain.User;
import com.lys.wheeling.domain.elist.Role;
import com.lys.wheeling.dto.UserProfileUpdateRequestDTO;
import com.lys.wheeling.dto.UserProfileViewDTO;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    void changeRole(Role role, String username);
    void deleteUser(Long userId);
    List<User> findAllUsers();

    UserProfileViewDTO getProfileByUsername(String username, Long viewerUserId);
    UserProfileViewDTO getMyProfile(Long userId);
    UserProfileViewDTO updateMyProfile(Long userId, UserProfileUpdateRequestDTO req);
    void deleteMyAccount(Long userId);
}
