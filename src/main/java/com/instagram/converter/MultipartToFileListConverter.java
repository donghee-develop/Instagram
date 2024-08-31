package com.instagram.converter;

import com.instagram.dto.FileDTO;
import com.instagram.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class MultipartToFileListConverter implements Converter<MultipartFile, List<FileDTO>> {
    @Autowired private FileService fileService;
    @Override
    public List<FileDTO> convert(MultipartFile source) {
        log.info("MultipartToFileListConverter 실행..");
        FileDTO fileDTO = fileService.multipart_to_fileDTO(source, 0);
        return List.of(fileDTO);
    }
}
