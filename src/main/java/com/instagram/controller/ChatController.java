package com.instagram.controller;

import com.instagram.dto.ChatMessageDTO;
import com.instagram.dto.ChatRoomDTO;
import com.instagram.dto.UserDTO;
import com.instagram.service.ChatService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
public class ChatController {
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired private ChatService chatService;

    @GetMapping("/chat")
    public String get_chat(
            @AuthenticationPrincipal UserDTO user,
            Model model
    ){
        List<ChatRoomDTO> chatRooms = chatService.get_chat_rooms(user.getEmail());
        model.addAttribute("chatRooms", chatRooms);
        return "main/chat";
    }

    @GetMapping("/chat/{roomId}")
    public String get_chat_room(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable String roomId,
            Model model
    ){
        List<ChatRoomDTO> chatRooms = chatService.get_chat_rooms(user.getEmail());
        ChatRoomDTO chatRoom = chatService.get_chat_room(roomId);
        List<List<ChatMessageDTO>> groupedChat = chatService.grouping_chat_messages(chatRoom);
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("chatRoom", groupedChat);
        return "main/chat";
    }

    // /app/roomId 로 publish 했을 때 반응함
    @MessageMapping("/{roomId}")
    public ChatMessageDTO get_chat_message(
            SimpMessageHeaderAccessor headerAccessor,
            @AuthenticationPrincipal UserDTO user,
            @DestinationVariable String roomId,
            @Payload ChatMessageDTO chatMessage
    ){
        // 보낸 유저를 가져옴
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(headerAccessor.getUser().getName());
        // 채팅 보낸 사람을 설정함
        chatMessage.setSender(userDTO);
        // 채팅이 보내진 채팅방을 설정함
        chatMessage.setRoomId(roomId);
        log.info(chatMessage);
        chatService.add_chat_message(roomId, chatMessage);
        // 채팅방의 모든 사람에게 메세지 전송하기
        return chatMessage;
    }


    @ResponseBody
    @PostMapping("/chat/create")
    public ResponseEntity<String> create_chat_room(
            @AuthenticationPrincipal UserDTO me,
            @RequestParam("user") String other
    ){
        // 나와의 채팅방 만들 시 리턴함
        if (me.getEmail().equals(other)) {
            return ResponseEntity.ok(null);
        }
        ChatRoomDTO existRoom = chatService.is_room_exist(me.getEmail(),other);
        if(existRoom != null){
            // 존재하는 방의 id 를 실어서 반환한다.
            return ResponseEntity.status(HttpStatus.FOUND).body(existRoom.getId());
        }
        // 방이 존재하지 않으면 방을 생성하고 생성된 방의 정보를 가져온다.
        ChatRoomDTO createdRoom = chatService.create_chat_room(me.getEmail(),other);

        // 생성된 방의 id 를 body 에 넣어서 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom.getId());
    }



}





