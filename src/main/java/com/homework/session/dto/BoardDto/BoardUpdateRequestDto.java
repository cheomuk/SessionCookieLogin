package com.homework.session.dto.BoardDto;

import com.homework.session.entity.User;
import com.homework.session.enumcustom.BoardEnumCustom;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateRequestDto {

    @ApiModelProperty(value="게시글 번호", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value="수정 시간", example = "yyyy.MM.dd", hidden = true)
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

    @ApiModelProperty(value="게시글 제목", example = "테스트 제목입니다.", required = true)
    private String title;

    @ApiModelProperty(value="의뢰 상태", example = "BEFORE", required = true)
    private BoardEnumCustom questEnum;

    @ApiModelProperty(value="게시글 내용", example = "테스트 글입니다.", required = true)
    private String context;

    @ApiModelProperty(value="사용자 정보", hidden = true)
    private User user;

    @ApiModelProperty(value="기존 사진 저장용 리스트", example = "[test1.png]", hidden = true)
    private List<String> savedFileUrl = new ArrayList<>();

    @ApiModelProperty(value="사진 리스트", example = "[test1.png, test2.png]", required = true)
    private List<MultipartFile> image = new ArrayList<>();
}
