package com.homework.session.dto.BoardDto;

import com.homework.session.entity.BoardList;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.BoardEnumCustom;
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
public class BoardUpdateRequestDto {

    private Long id;
    private String title;
    private BoardEnumCustom questEnum;
    private String context;
    private User user;
    private List<String> savedFileUrl = new ArrayList<>();
    private List<MultipartFile> file = new ArrayList<>();

}
