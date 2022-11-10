package com.homework.session.dto.CommentDto;

import com.homework.session.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {

    @ApiModelProperty(value="댓글 번호", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value="댓글 내용", example = "재밌당", required = true)
    private String comment;

    @ApiModelProperty(value="생성 시간", example = "yyyy.MM.dd HH:mm", hidden = true)
    private String createdDate;

    @ApiModelProperty(value="수정 시간", example = "yyyy.MM.dd HH:mm", hidden = true)
    private String modifiedDate;

    @ApiModelProperty(value="닉네임", example = "홍길동", required = true)
    private String nickname;

    @ApiModelProperty(value="게시글 번호", example = "1", hidden = true)
    private Long boardListId;

    @ApiModelProperty(value="부모 댓글 Id", example = "1", hidden = true)
    private List<Comment> parent;

    /* Entity -> Dto*/
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();
        this.nickname = comment.getUser().getNickname();
        this.boardListId = comment.getBoardList().getId();
    }
}
