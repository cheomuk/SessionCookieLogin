package com.homework.session.dto.BoardDto;

import com.homework.session.dto.FileDto.FileResponseDto;
import com.homework.session.entity.BoardList;
import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ThumbnailResponseDto {
    private Long id;
    private String createdDate;
    private String nickname;
    private String title;
    private BoardEnumCustom questEnum;
    private List<FileResponseDto> image;

    public ThumbnailResponseDto (BoardList boardList) {
        this.id = boardList.getId();
        this.createdDate = boardList.getCreatedDate();
        this.nickname = boardList.getNickname();
        this.title = boardList.getTitle();
        this.questEnum = boardList.getQuestEnum();
        this.image = boardList.getImage().stream().map(FileResponseDto::new).collect(Collectors.toList());
    }
}
