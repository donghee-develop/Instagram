package com.instagram.controller;

import com.instagram.dto.UserDTO;
import com.instagram.service.EmailService;
import com.instagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired private UserService userService;
    @Autowired private EmailService emailService;

    @GetMapping("/login")
    public void get_user_login(){}

    @GetMapping("/signup")
    public void get_user_signup(){}

    @PostMapping("/signup")
    public String post_user_signup(
            HttpSession session,
            @RequestParam("email-auth") String emailAuth,
            UserDTO user
    ){
        log.info("로그인 시도하는 유저:" + user);
        log.info("입력된 이메일 인증 번호:" + emailAuth);
        Object o = session.getAttribute("emailAuthNumber");
        // 세션에 저장된 값이 없다면 ( 이메일 전송 없이 POST 요청했더라 )
        if(Objects.isNull(o)){
            return "redirect:/user/signup";
        }
        // 이메일 인증 번호를 가져와서 사용자의 입력값과 비교
        String emailAuthNumber = o.toString();
        if(!emailAuth.equals(emailAuthNumber)){
            return "redirect:/user/signup";
        }
        // 인증번호가 맞았다면 회원가입 시킨다
        userService.create_user(user);
        return "redirect:/user/login";
    }




    /*********************************************************/
    @GetMapping("/check")
    public ResponseEntity<Void> check_user(
            HttpSession session,
            UserDTO user
    ){
        log.info(user);
        boolean result = userService.check_user_is_duplicated(user);
        // 유저가 중복임
        if(result){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        // 유저를 생성할 수 있음
        return ResponseEntity.ok().body(null);
    }


    // 유저에게 이메일로 인증 번호를 전송함
    @ResponseBody
    @PostMapping("/auth")
    public ResponseEntity<Void> post_email_auth(
            HttpSession session,
            @RequestBody String email // body 데이터에 email
    ){
        log.info("email전송시도.. " + email);
        try {
            // email 에 인증번호를 발송하고 인증번호를 가져옴
            String emailAuthNumber = emailService.send_signup_auth_mail(email);
            session.setAttribute("emailAuthNumber", emailAuthNumber);
            // 발송에 성공했으면 ok.
            return ResponseEntity.ok(null);
        }catch (Exception e){
            log.error("이메일 발송오류: " + e.getMessage());
            // 발송하다가 오류났으면 Status에 Error 설정
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*********************************************************/
    @ResponseBody
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> get_search_user(
            @AuthenticationPrincipal UserDTO user,
            String keyword
    ){
        List<UserDTO> users = userService.find_user_by_keyword(user.getEmail(), keyword);
        return ResponseEntity.ok(users);
    }

    @ResponseBody
    @PostMapping("/search")
    public ResponseEntity<Void> post_search_user(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody String otherUserEmail
    ){
        userService.update_find_user(user.getEmail(), otherUserEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @DeleteMapping("/search")
    public ResponseEntity<Void> delete_search_user(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody(required = false) String otherUserEmail
    ){
        userService.remove_find_user(user.getEmail(), otherUserEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    /************************************************************/
    @GetMapping("/mypage/{email}")
    public String get_user_mypage(
            @PathVariable String email,
            Model model
    ){
        UserDTO user = userService.get_user(email);
        model.addAttribute("user", user);
        return "user/mypage";
    }

    /*****************************************************/
    @ResponseBody
    @PostMapping("/follow")
    public ResponseEntity<Void> post_follow_user(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody String otherUserEmail
    ){
        userService.follow_user(user, otherUserEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @DeleteMapping("/follow")
    public ResponseEntity<Void> delete_follow_user(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody String otherUserEmail
    ){
        userService.unFollow_user(user, otherUserEmail);
        return ResponseEntity.ok(null);
    }




}
