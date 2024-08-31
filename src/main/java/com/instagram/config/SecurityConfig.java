package com.instagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // object 태그로 우리 어플리케이션의 다른 html 화면을 보여줄 수 있도록 설정함
        http.headers(config -> {
            config.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
        });

        ////////// 요청 구현
        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/user/signup", "/user/auth", "/user/check", "/static/**").permitAll();
            request.anyRequest().authenticated();
        });

        ////////// 로그인 구현
        http.formLogin(config -> {
            config.loginPage("/user/login") // 인증하는 페이지는 /user/login
                    .usernameParameter("email") // 로그인시 input name을 email 적어야 함
                    .passwordParameter("password") // 로그인시 input name을 password 적어야 함
                    .defaultSuccessUrl("/") // 로그인 완료시에는 / 으로 이동함
                    .permitAll();
        });

        ////////// 로그아웃 구현
        http.logout(config -> {
            config.logoutUrl("/user/logout") // 로그아웃 시도 경로
                    .logoutSuccessUrl("/user/login") // 로그아웃 성공 시 로그인 페이지로
                    .deleteCookies("JSESSIONID") // 로그아웃 후 쿠키 삭제
                    .invalidateHttpSession(true) // 로그아웃 후 세션 삭제
                    .clearAuthentication(true) // 로그아웃 후 인증 객체 삭제
                    .permitAll(); // 누구든 실행할 수 있음
        });


        return http.build();
    }

    // 패스워드 암호화는 완성이 어느정도 될 때까지 안할게요
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }







}
