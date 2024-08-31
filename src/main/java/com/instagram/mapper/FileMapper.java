package com.instagram.mapper;

import com.instagram.dto.FileDTO;
import com.instagram.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    FileDTO selectFileByUserEmail(String userEmail);
    FileDTO selectFileByUuid(String uuid);
}







