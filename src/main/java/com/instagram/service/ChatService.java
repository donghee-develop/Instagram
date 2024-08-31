package com.instagram.service;

import com.instagram.dto.ChatMessageDTO;
import com.instagram.dto.ChatRoomDTO;
import com.instagram.dto.UserDTO;
import com.instagram.mapper.ChatMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    @Autowired private ChatMapper chatMapper;

    // 유저가 참여중인 모든 방 가져오기(side부분)
    public List<ChatRoomDTO> get_chat_rooms(String userEmail){
        return chatMapper.selectChatRoomsByUserEmail(userEmail);
    }
    // 유저가 채팅중인 채팅방 하나 가져오기 (main화면)
    public ChatRoomDTO get_chat_room(String roomId){
        return chatMapper.selectChatRoomByRoomId(roomId);
    }

    // 메세지를 유저로 그룹핑해서 표시하기 위한 메서드
    public List<List<ChatMessageDTO>> grouping_chat_messages(ChatRoomDTO chatRoom){
        // 이 방에서 보낸 모든 메세지 리스트
        if(chatRoom == null) return new LinkedList<>();
        List<ChatMessageDTO> chatMessages = chatRoom.getMessages();
        // 유저가 보낸 메세지를 그룹핑해서 리스트로 넣어줌
        LinkedList<List<ChatMessageDTO>> chatMessageList = new LinkedList<>();
        // 마지막에 채팅 보낸 유저
        String lastSendUser = null;
        // 모든 메세지를 순회
        for(ChatMessageDTO chatMessage: chatMessages){
            String sendUser = chatMessage.getSender().getEmail(); // 이 메세지 보낸 사람
            // 마지막 채팅 유저가 현재 채팅 유저랑 같다면 == 같은 list(그룹)로 묶는다
            if(lastSendUser != null && lastSendUser.equals(sendUser)){
                // 마지막 그룹을 가져와서 현재 채팅을 넣는다
                chatMessageList.getLast().add(chatMessage);
            }
            // 그 전 유저와 현재 채팅 유저가 다르다면 => 새로운 list(그룹)를 생성한다
            else{
                // 새로운 그룹을 생성한다
                var newMessageList = new ArrayList<ChatMessageDTO>();
                // 화면에 가져갈 모든 그룹 리스트에 새로운 그룹을 추가한다
                chatMessageList.add(newMessageList);
                // 그룹에 현재 메세지를 담는다
                newMessageList.add(chatMessage);
            }
            // 마지막 채팅 유저를 현재 유저로 만든다
            lastSendUser = sendUser;
        }
        // 그룹핑 결과를 반환
        return chatMessageList;
    }
    public void add_chat_message(String roomId, ChatMessageDTO chatMessage){
        chatMapper.insertChatMessageOfRoomId(roomId, chatMessage);
    }

    // 채팅방이 존재하는가?
    public ChatRoomDTO is_room_exist(String me, String other){
        return chatMapper.selectChatRoomByUserEmails(me,other);
    }
    public ChatRoomDTO create_chat_room(String me, String other){
        String roomId = UUID.randomUUID().toString().substring(0,5);
        UserDTO otherUser = UserDTO.builder().email(other).build();
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .id(roomId)
                .otherUser(otherUser)
                .build();
        chatMapper.insertRoom(chatRoomDTO);
        chatMapper.insertRoomUsers(me,chatRoomDTO);
        return chatRoomDTO;
    }
}








