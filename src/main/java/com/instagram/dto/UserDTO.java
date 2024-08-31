package com.instagram.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements UserDetails {
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private String nickname;
    private FileDTO image;
    private String introduce;
    private Set<String> followings;
    private Set<String> followers;
    private boolean isFollowing;


    // UserDetail 을 impl 하면 메소드를 오버라이딩해야한다.
    // 권한 부여하기 현재는 사용자만 만들기에 권한을 부여할 필요가 없다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
