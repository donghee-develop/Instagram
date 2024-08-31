package com.instagram.mapper;

import com.instagram.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Mapper
public interface UserMapper {
    // 유저 정보로 유저 조회
    UserDTO getUserByUserInfo(UserDTO user);
    // 유저 회원가입
    void insertUser(UserDTO user);
    // 키워드로 유저 찾기 - 유저 이메일이 null이 아니면 유저가 검색한 검색이력만
    List<UserDTO> selectUserByKeyword(
            @Param("userEmail") String userEmail,
            @Param("keyword") String keyword
    );
    void updateFindUser(
            @Param("meEmail") String meEmail,
            @Param("otherEmail") String otherEmail
    );
    void deleteFindUser(
            @Param("meEmail") String meEmail,
            @Param("otherEmail") String otherEmail
    );
    /***********************************************/
    void insertFollowUser(
            @Param("meEmail") String meEmail,
            @Param("otherEmail") String otherEmail
    );
    void deleteFollowUser(
            @Param("meEmail") String meEmail,
            @Param("otherEmail") String otherEmail
    );




}





