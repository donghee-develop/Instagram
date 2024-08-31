package com.instagram.service;

import com.instagram.dto.PostDTO;
import com.instagram.dto.UserDTO;
import com.instagram.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired private PostMapper postMapper;

    // 마지막 게시물로부터 count 개수만큼 게시물 가져오기
    public List<PostDTO> get_posts(Integer lastPostNo, Integer count, String loginUserEmail) {
        return get_posts(lastPostNo, count, null, loginUserEmail);
    }

    // 해당 유저가 게시한 마지막 게시물로부터 count 개수만큼 게시물 가져오기
    public List<PostDTO> get_posts(Integer lastPostNo, Integer count, String userEmail, String loginUserEmail){
        return postMapper.selectPosts(lastPostNo, count, userEmail, loginUserEmail);
    }

    public PostDTO get_post(Integer postNo, String loginUserEmail) {
        return postMapper.selectPostByPostNo(postNo, loginUserEmail);
    }

    // 하나의 게시물을 생성
    public void create_post(UserDTO user, PostDTO post){
        post.setUser(user); // 현재 로그인되어있는 유저 정보로 설정
        post.setType("POST");
        postMapper.insertPost(post); // 게시물 insert
        postMapper.insertPostContents(post); // 게시물의 사진/동영상 등 contents insert
        // 이 유저를 팔로우 하고 있는 사람들에게 알람을 전송 (DB에도 넣고, 실제 알람도 전송하고)
        if(!user.getFollowers().isEmpty()){
            add_alarm(post);
        }
    }

    // 알람 내용을 DB에 등록하기
    public void add_alarm(PostDTO post){
        postMapper.insertAlarmOfUser(post);
    }

    // 팔로워들에게 알람 전송하기
    @Async
    public void send_alarm(PostDTO post){
        UserDTO publishUser = post.getUser();
        PostDTO postMessage = PostDTO.builder()
                .no(post.getNo())
                .user(publishUser)
                .text(post.getText())
                .type(post.getType())
                .build();
        postMessage.setWriteTime(LocalDateTime.now());
        simpMessagingTemplate.convertAndSend("/topic/user/" + publishUser.getEmail(), postMessage);
    }

    /********* 유저의 알람 정보 전부 가져오기 *************/
    public List<PostDTO> get_user_alarms(String userEmail){
        return postMapper.selectPostAlarmsByUser(userEmail);
    }

    public PostDTO post_like(String userEmail, Integer postNo){
        postMapper.insertPostLikeOfUser(userEmail, postNo);
        PostDTO post = postMapper.selectPostByPostNo(postNo, userEmail);
        post.setType("LIKE");
        return post;
    }

    public void post_like_cancel(String userEmail, Integer postNo){
        postMapper.deletePostLikeOfUser(userEmail, postNo);
    }
}











