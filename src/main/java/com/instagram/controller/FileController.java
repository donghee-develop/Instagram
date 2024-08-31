package com.instagram.controller;

import com.instagram.dto.FileDTO;
import com.instagram.service.FileService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class FileController {
    @Autowired private FileService fileService;

    @GetMapping("/image/{userEmail}")
    public ResponseEntity<byte[]> get_user_image(
            @PathVariable String userEmail
    ) {
        FileDTO fileDTO = fileService.get_user_image(userEmail);
        byte[] bytes = fileDTO.getData(); // 50 MB
        Tika tika = new Tika();
        String contentType = tika.detect(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
        headers.setContentLength(bytes.length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    @GetMapping("/contents/{uuid}")
    public ResponseEntity<byte[]> get_contents(
            @PathVariable String uuid
    ) {
        FileDTO fileDTO = fileService.get_post_contents(uuid);
        String mimeType = fileDTO.getMimeType();
        String extension = fileDTO.getExtension();
        byte[] bytes = fileDTO.getData(); // 50 MB
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(mimeType + "/" + extension));
        headers.setContentLength(bytes.length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }


}
