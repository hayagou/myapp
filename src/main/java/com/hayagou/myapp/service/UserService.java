package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CEmailSigninFailedException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.config.security.JwtTokenProvider;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.PostListDto;
import com.hayagou.myapp.model.dto.UserInfoDto;
import com.hayagou.myapp.model.dto.UserUpdateDto;
import com.hayagou.myapp.model.redis.CacheKey;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signin(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        return jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRoles());
    }
    @Transactional
    public void singup(String email, String password, String name){
        userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }

    @Transactional
    public List<UserInfoDto> getUsers(int page){
        Page<User> list = userRepository.findAll(PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<UserInfoDto> userList = new ArrayList<>();
        for (User user: list) {
            userList.add(UserInfoDto.builder().name(user.getName()).email(user.getEmail()).createAt(user.getCreatedAt()).build());
        }
        return userList;
    }

    @Transactional
    @Cacheable(value = CacheKey.USER, key = "#email", unless = "#result == null")
    public UserInfoDto getUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        return UserInfoDto.builder().email(user.getEmail()).name(user.getName()).createAt(user.getCreatedAt()).build();
    }

    @Transactional
    public void updateUser(String email, UserUpdateDto userUpdateDto){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        user.updateUserInfo(userUpdateDto.getName());
    }

    @Transactional
    public void updatePassword(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        user.updatePassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void deleteUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        userRepository.delete(user);
    }

}
