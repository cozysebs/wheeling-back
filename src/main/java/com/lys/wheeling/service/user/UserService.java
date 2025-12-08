package com.lys.wheeling.service.user;

import com.lys.wheeling.domain.User;
import com.lys.wheeling.domain.elist.Role;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    void changeRole(Role role, String username);
    void deleteUser(Long userId);
    List<User> findAllUsers();
}
