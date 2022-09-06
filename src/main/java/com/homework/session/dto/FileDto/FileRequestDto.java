package com.homework.session.dto.FileDto;

import com.homework.session.entity.BoardList;
import com.homework.session.entity.File;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileRequestDto {

    private String fileName;
    private String fileUrl;
    private BoardList boardList;

    public File toEntity(String fileName, String fileUrl, BoardList boardList) {
        File file = File.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .boardList(boardList)
                .build();

        return file;
    }
}
