package com.homework.session.dto.BoardDto;

import lombok.Getter;

import java.util.List;

@Getter
public class UploadFileResponse {
    private Long id;
    private List<String> fileUrl;

    public UploadFileResponse(long id, List<String> fileUrl) {
        this.id = id;
        this.fileUrl = fileUrl;
    }
}
