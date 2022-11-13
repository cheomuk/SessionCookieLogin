package com.homework.session.dto.FileDto;

import com.homework.session.entity.File;
import lombok.Getter;

@Getter
public class FileResponseDto {
    private String fileName;
    private String fileUrl;
    private Long boardListId;

    public FileResponseDto(File file) {
        this.fileName = file.getFileName();
        this.fileUrl = file.getFileUrl();
        this.boardListId = file.getBoardList().getId();
    }
}
