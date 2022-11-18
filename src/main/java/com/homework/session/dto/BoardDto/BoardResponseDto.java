package com.homework.session.dto.BoardDto;

import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.dto.FileDto.FileResponseDto;
import com.homework.session.entity.BoardList;
import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {

    private Long id;
    private String createDate;
    private String modifiedDate;
    private String nickname;
    private String title;
    private BoardEnumCustom questEnum;
    private String context;
    private List<CommentResponseDto> comments;
    private List<FileResponseDto> image;

    public BoardResponseDto(BoardList boardList) {
        this.id = boardList.getId();
        this.createDate = boardList.getCreatedDate();
        this.modifiedDate = boardList.getModifiedDate();
        this.nickname = boardList.getNickname();
        this.title = boardList.getTitle();
        this.questEnum = boardList.getQuestEnum();
        this.context = boardList.getContext();
        this.comments = boardList.getComments().stream().map(CommentResponseDto::new)
                .filter(comments -> !comments.getChildren().isEmpty()).collect(Collectors.toList());
        this.image = boardList.getImage().stream().map(FileResponseDto::new).collect(Collectors.toList());
    }
}
