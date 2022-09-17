package com.homework.session.dto.BoardDto;

import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.entity.BoardList;
import com.homework.session.enumcustom.BoardEnumCustom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {

    private Long id;
    private String nickname;
    private String title;
    private BoardEnumCustom questEnum;
    private String context;
    private Long userId;
    private List<CommentResponseDto> comments;

    public BoardResponseDto(BoardList boardList) {
        this.id = boardList.getId();
        this.nickname = boardList.getNickname();
        this.title = boardList.getTitle();
        this.questEnum = boardList.getQuestEnum();
        this.context = boardList.getContext();
        this.userId = boardList.getUser().getId();
        this.comments = boardList.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
