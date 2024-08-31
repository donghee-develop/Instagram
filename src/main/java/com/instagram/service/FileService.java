package com.instagram.service;

import com.instagram.dto.FileDTO;
import com.instagram.mapper.FileMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Log4j2
@Service
public class FileService {
    @Autowired private FileMapper fileMapper;

    public FileDTO multipart_to_fileDTO(MultipartFile file, Integer order) {
        try{
            String uuid = UUID.randomUUID().toString().substring(0, 5);
            byte[] data = file.getBytes();
            String[] contentType = file.getContentType().split("/");
            String mimeType = contentType[0];
            String extension = contentType[1];
            return FileDTO.builder()
                    .uuid(uuid)
                    .multipartFile(file)
                    .order(order)
                    .data(data)
                    .mimeType(mimeType)
                    .extension(extension)
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public FileDTO get_user_image(String userEmail){
        return fileMapper.selectFileByUserEmail(userEmail);
    }

    public FileDTO get_post_contents(String  uuid){
        return fileMapper.selectFileByUuid(uuid);
    }





}
