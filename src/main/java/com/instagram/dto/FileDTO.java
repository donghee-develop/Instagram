package com.instagram.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private String uuid;
    private Integer order;
    @ToString.Exclude
    private byte[] data;
    @ToString.Exclude
    private MultipartFile multipartFile;
    private String mimeType;
    private String extension;
}
