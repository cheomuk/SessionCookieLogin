package com.homework.session.dto.CommentDto;

import com.homework.session.entity.BoardList;
import com.homework.session.entity.Comment;
import com.homework.session.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    @ApiModelProperty(value="댓글 번호", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value="댓글 내용", example = "재밌당", required = true)
    private String comment;

    @ApiModelProperty(value="생성 시간", example = "yyyy.MM.dd HH:mm", hidden = true)
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    @ApiModelProperty(value="수정 시간", example = "yyyy.MM.dd HH:mm", hidden = true)
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    @ApiModelProperty(value="유저 정보", example = "user_id", hidden = true)
    private User user;

    @ApiModelProperty(value="게시글 정보", example = "board_list_id", hidden = true)
    private BoardList boardList;

    /* Dto -> Entity */
    public Comment toEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .comment(comment)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .user(user)
                .boardList(boardList)
                .build();

        return comments;
    }
}

