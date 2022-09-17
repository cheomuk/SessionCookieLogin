package com.homework.session.dto.BoardDto;

import com.homework.session.entity.BoardList;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.BoardEnumCustom;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

    @ApiModelProperty(value="게시글 번호", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value="게시글 제목", example = "테스트 제목입니다.", required = true)
    private String title;

    @ApiModelProperty(value="의뢰 상태", example = "BEFORE", required = true)
    private BoardEnumCustom questEnum;

    @ApiModelProperty(value="게시글 내용", example = "테스트 글입니다.", required = true)
    private String context;

    @ApiModelProperty(value="사용자 정보", hidden = true)
    private User user;

    @ApiModelProperty(value="사진 리스트", example = "[test1.png, test2.png]", required = true)
    private List<MultipartFile> fileList = new ArrayList<>();

    public BoardList toEntity() {
        BoardList boardList = BoardList.builder()
                .id(id)
                .title(title)
                .questEnum(questEnum)
                .context(context)
                .user(user)
                .build();

        return boardList;
    }
}
