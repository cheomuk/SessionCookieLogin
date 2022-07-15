package com.homework.session.entity;

import com.homework.session.dto.BoardListDto;
import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "board_list")
public class BoardList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardEnumCustom questEnum;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String context;

    @Column
    private String questDate;

    @Builder
    public BoardList(String nickname, String title, BoardEnumCustom questEnum, String context, String questDate) {
        this.nickname = nickname;
        this.title = title;
        this.questEnum = questEnum;
        this.context = context;
        this.questDate = questDate;
    }

    public void update(BoardListDto boardListDto) {
        this.nickname = boardListDto.getNickname();
        this.title = boardListDto.getTitle();
        this.questEnum = boardListDto.getQuestEnum();
        this.context = boardListDto.getContext();
        this.questDate = boardListDto.getQuestDate();
    }
}
