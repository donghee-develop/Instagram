package com.instagram.mapper;

import com.instagram.dto.ChatMessageDTO;
import com.instagram.dto.ChatRoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {
    // roomId에 해당하는 방의 모든 정보 조회 (사람, 메세지 내용 전부)
    ChatRoomDTO selectChatRoomByRoomId(String roomId);
    void insertChatMessageOfRoomId(
            @Param("roomId") String roomId,
            @Param("chatMessage") ChatMessageDTO chatMessage
    );
    // chat main화면 왔을 때, 사이드바에 있는 방 정보 조회 (방,상대방, 마지막 메세지만)
    // 내가 참여중인 채팅만 나와야 함
    List<ChatRoomDTO> selectChatRoomsByUserEmail(String userEmail);
    /// 두 유저가 참여중인 방의 정보를 가져온다
    ChatRoomDTO selectChatRoomByUserEmails(
            @Param("meEmail") String meEmail,
            @Param("otherEmail") String otherEmail
    );
    // 방 정보를 생성한다
    void insertRoom(ChatRoomDTO chatRoomDTO);
    // 생성된 방에 유저들을 설정한다
    void insertRoomUsers(
            @Param("meEmail") String meEmail,
            @Param("chatRoom") ChatRoomDTO chatRoom
    );


}
