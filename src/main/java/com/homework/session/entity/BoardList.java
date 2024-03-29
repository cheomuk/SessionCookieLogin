package com.homework.session.entity;

import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.BoardDto.BoardUpdateRequestDto;
import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@BatchSize(size = 10)
public class BoardList {

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

    @Column(name = "created_date")
    @CreatedDate
    private String createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private String modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users")
    private User user;

    @OneToMany(mappedBy = "boardList", orphanRemoval = true)
    @OrderBy("id asc") // 오름차순 정렬
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "boardList", orphanRemoval = true)
    private List<File> image = new ArrayList<>();

    public void update(BoardUpdateRequestDto boardUpdateRequestDto) {
        this.modifiedDate = boardUpdateRequestDto.getModifiedDate();
        this.title = boardUpdateRequestDto.getTitle();
        this.questEnum = boardUpdateRequestDto.getQuestEnum();
        this.context = boardUpdateRequestDto.getContext();
    }
}
