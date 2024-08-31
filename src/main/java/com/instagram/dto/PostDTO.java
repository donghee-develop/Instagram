package com.instagram.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Setter
@ToString
@Getter
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Integer no;
    private UserDTO user;
    private String text;
    private Integer likeCount;
    private LocalDateTime writeTime;
    private List<FileDTO> contents;
    private String diffTime; // 게시 날짜
    private Boolean isPostLike;
    private String type; // post 알람 보낼 때 타입

    public void setWriteTime(LocalDateTime writeTime) {
        this.writeTime = writeTime;
        LocalDateTime now = LocalDateTime.now();
        long hour = ChronoUnit.HOURS.between(writeTime,now);
        long day = ChronoUnit.DAYS.between(writeTime,now);
        long week = ChronoUnit.WEEKS.between(writeTime,now);

        this.diffTime = hour + "시간";

        if(hour > 23){
            this.diffTime = day + "일";
            if(day >= 7){
                this.diffTime = week + "주";
            }
        }
    }
}