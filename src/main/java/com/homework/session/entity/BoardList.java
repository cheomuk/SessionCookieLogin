package com.homework.session.entity;

import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BoardList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardEnumCustom questEnum;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_list")
    private User user;

    @OneToMany(mappedBy = "boardList", orphanRemoval = true)
    @OrderBy("id asc") // 오름차순 정렬
    private List<Comment> comments;

    @OneToMany(mappedBy = "boardList", orphanRemoval = true)
    private List<File> fileList = new ArrayList<>();

    public void update(BoardRequestDto boardListDto) {
        this.title = boardListDto.getTitle();
        this.questEnum = boardListDto.getQuestEnum();
        this.context = boardListDto.getContext();
    }
}
