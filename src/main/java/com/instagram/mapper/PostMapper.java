package com.instagram.mapper;

import com.instagram.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    PostDTO selectPostByPostNo(@Param("postNo") Integer postNo, @Param("loginUserEmail") String loginUserEmail);
    // 마지막 게시물 이후 게시물들 count 개수만큼 불러오기
    List<PostDTO> selectPosts(
            @Param("lastPostNo") Integer lastPostNo,
            @Param("count") Integer count,
            // 널 가능 마이페이지에서만 널 아님
            @Param("userEmail") String userEmail,
            // 모든 페이지 널 아니여야 함
            @Param("loginUserEmail") String loginUserEmail
    );
    // 하나의 게시물 (POST) 내용 INSERT
    void insertPost(PostDTO post);
    // 위에서 INSERT 했던 게시물의 사진/동영상 INSERT
    void insertPostContents(PostDTO post);

    void insertAlarmOfUser(PostDTO post);
    List<PostDTO> selectPostAlarmsByUser(String userEmail);

    void insertPostLikeOfUser(@Param("userEmail") String userEmail, @Param("postNo") Integer postNo);
    void deletePostLikeOfUser(@Param("userEmail") String userEmail, @Param("postNo") Integer postNo);
}







