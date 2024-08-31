package com.instagram.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private String id;
    private UserDTO otherUser;
    private List<ChatMessageDTO> messages;
    private LocalDateTime crateTime;
}
