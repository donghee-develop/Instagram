package com.instagram.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Integer no;
    private String roomId;
    private UserDTO sender;
    private String message;
    private LocalDateTime sendTime;
}
