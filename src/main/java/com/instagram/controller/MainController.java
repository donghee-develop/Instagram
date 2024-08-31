package com.instagram.controller;

import com.instagram.dto.PostDTO;
import com.instagram.dto.UserDTO;
import com.instagram.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Controller
public class MainController {
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired private PostService postService;

    @GetMapping("/")
    public String get_main() {
        return "main/main";
    }

    @GetMapping("/posts")
    public String get_posts(
            @AuthenticationPrincipal UserDTO user,
            @RequestParam(defaultValue = "99999999") Integer lastPostNo,
            @RequestParam(defaultValue = "3") Integer count,
            Model model
    ) {
        List<PostDTO> posts = postService.get_posts(lastPostNo, count, user.getEmail());
        model.addAttribute("posts", posts);
        return "fragment/main-post-article";
    }

    @GetMapping("/post/{no}")
    public String get_post(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable Integer no, Model model
    ) {
        PostDTO postDTO = postService.get_post(no, user.getEmail());
        model.addAttribute("post", postDTO);
        return "main/view";
    }

    @GetMapping("/post/create")
    public String get_post_create(){
        return "main/create-post";
    }

    @PostMapping("/post/create")
    public String post_post_create(
            @AuthenticationPrincipal UserDTO user,
            PostDTO post
    ){
        // 게시물 생성
        postService.create_post(user, post);
        // 알람 전송
        postService.send_alarm(post);
        return "redirect:/post/create";
    }

    /**************************************************/
    @GetMapping("/contents")
    public ResponseEntity<List<PostDTO>> get_contents(
            @AuthenticationPrincipal UserDTO user,
            @RequestParam(defaultValue = "99999999") Integer lastPostNo,
            @RequestParam(defaultValue = "3") Integer count,
            @RequestParam(value = "email", required = false) String userEmail
    ){
        List<PostDTO> posts = postService.get_posts(lastPostNo, count, userEmail, user.getEmail());
        return ResponseEntity.ok(posts);
    }

    /************************************************/
    @GetMapping("/search")
    public String get_search(){
        return "main/search";
    }

    /*******************************************/
    @ResponseBody
    @GetMapping("/post/alarm")
    public ResponseEntity<List<PostDTO>> get_alarms(
            @AuthenticationPrincipal UserDTO user
    ){
        List<PostDTO> posts = postService.get_user_alarms(user.getEmail());
        return ResponseEntity.ok(posts);
    }

    /*******************************************************/
    @PostMapping("/post/like/{postNo}")
    public ResponseEntity<Void> post_like(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable Integer postNo
    ){
        log.info(postNo);
        // 해당 유저 이메일 가져오기

        PostDTO post = postService.post_like(user.getEmail(), postNo);
        // Like 알림 DB 저장
        postService.add_alarm(post);
        // convertAndSend 알림 전송
        postService.send_alarm(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @DeleteMapping("/post/like/{postNo}")
    public ResponseEntity<Void> post_like_delete(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable Integer postNo
    ){
        postService.post_like_cancel(user.getEmail(), postNo);
        return ResponseEntity.ok(null);
    }


}









