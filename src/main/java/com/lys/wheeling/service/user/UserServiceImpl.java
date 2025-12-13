package com.lys.wheeling.service.user;

import com.lys.wheeling.domain.User;
import com.lys.wheeling.domain.elist.Role;
import com.lys.wheeling.dto.UserProfileUpdateRequestDTO;
import com.lys.wheeling.dto.UserProfileViewDTO;
import com.lys.wheeling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public void changeRole(Role newRole, String username) {
        userRepository.updateUserRole(username, newRole);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    // ========== 유저 프로필 조회 및 업데이트 및 삭제 ==========

    @Override
    @Transactional(readOnly = true)
    public UserProfileViewDTO getProfileByUsername(String username, Long viewerUserId) {
        User target = userRepository.findOptionalByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: username=" + username));

        boolean owner = (viewerUserId != null && viewerUserId.equals(target.getUserId()));

        return UserProfileViewDTO.builder()
                .userId(target.getUserId())
                .username(target.getUsername())
                .bio(target.getBio())
                .category(target.getCategory())
                .favoriteCategories(target.getFavoriteCategories())
                .gender(target.getGender())
                .birthDate(target.getBirthDate())
                .owner(owner)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileViewDTO getMyProfile(Long userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: id=" + userId));

        return UserProfileViewDTO.builder()
                .userId(me.getUserId())
                .username(me.getUsername())
                .bio(me.getBio())
                .category(me.getCategory())
                .favoriteCategories(me.getFavoriteCategories())
                .gender(me.getGender())
                .birthDate(me.getBirthDate())
                .owner(true)
                .build();
    }

    @Override
    public UserProfileViewDTO updateMyProfile(Long userId, UserProfileUpdateRequestDTO req) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: id=" + userId));

        // 부분 업데이트(요청값이 null이면 기존 유지)
        if (req.getBio() != null) {
            me.setBio(req.getBio());
        }
        if (req.getBirthDate() != null) {
            me.setBirthDate(req.getBirthDate());
        }
        if (req.getGender() != null) {
            me.setGender(req.getGender());
        }
        if (req.getFavoriteCategories() != null) {
            me.setFavoriteCategories(req.getFavoriteCategories());

            // 하위호환: 대표 category는 1순위로 동기화
            if (!req.getFavoriteCategories().isEmpty()) {
                me.setCategory(req.getFavoriteCategories().get(0));
            }
        }

        User saved = userRepository.save(me);

        return UserProfileViewDTO.builder()
                .userId(saved.getUserId())
                .username(saved.getUsername())
                .bio(saved.getBio())
                .category(saved.getCategory())
                .favoriteCategories(saved.getFavoriteCategories())
                .gender(saved.getGender())
                .birthDate(saved.getBirthDate())
                .owner(true)
                .build();
    }

    @Override
    public void deleteMyAccount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found: id=" + userId);
        }
        // User 엔티티에 cascade REMOVE가 설정된 연관관계들은 함께 정리됨
        userRepository.deleteById(userId);
    }
}
