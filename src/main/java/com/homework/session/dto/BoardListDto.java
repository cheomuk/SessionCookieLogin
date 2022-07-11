package com.homework.session.dto;

import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {

    private Long id;
    private String nickname;
    private String title;
    private BoardEnumCustom questEnum;
    private String context;
    private String questDate;

    public BoardListDto(BoardListDto boardListDto) {
        this.nickname = boardListDto.getNickname();
        this.title = boardListDto.getTitle();
        this.questEnum = boardListDto.getQuestEnum();
        this.context = boardListDto.getContext();
        this.questDate = boardListDto.getQuestDate();
    }
}
