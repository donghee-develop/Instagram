package com.instagram.service;

import com.instagram.dto.UserDTO;
import com.instagram.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j

// @Service 적용 시 Bean 이 등록되면서 시큐리티가 자동으로 DetailsService 를 대체함
@Service

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    // 1. 로그인 페이지에서 로그인 버튼 누르면 자동으로 메소드 실행
    // 2. 로그인 로직 구현하기 (username == 화면에서 입력한 Id)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username); // 이메일이 온다.
        UserDTO tempUser = new UserDTO();
        tempUser.setEmail(username);
        UserDTO findUser = userMapper.getUserByUserInfo(tempUser);
        if(Objects.isNull(findUser)) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser;
    }
}
