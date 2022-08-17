package com.homework.session.dto.BoardDto;

import com.homework.session.entity.BoardList;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.BoardEnumCustom;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

    private Long id;
    private String nickname;
    private String title;
    private BoardEnumCustom questEnum;
    private String context;
    private User user;

    public BoardList toEntity() {
        BoardList boardList = BoardList.builder()
                .id(id)
                .nickname(nickname)
                .title(title)
                .questEnum(questEnum)
                .context(context)
                .user(user)
                .build();

        return boardList;
    }
}
