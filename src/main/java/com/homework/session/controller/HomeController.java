package com.homework.session.controller;

import com.homework.session.config.LoginUser;
import com.homework.session.dto.BoardDto.BoardResponseDto;
import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/list/write")
    public String createBoardForm(@LoginUser UserResponseDto userResponseDto) {
        if (userResponseDto != null) {
            return "/list/create";
        }
        return "/oauth2/authorization/kakao";
    }

    @GetMapping("/list/update/{id}")
    public String updateBoardForm(@PathVariable Long id, @LoginUser UserResponseDto userResponseDto) {
        if (userResponseDto != null) {
            return "/list/{id}";    // PUT
        } else {
            return "/list/{id}";    // GET
        }
    }

    @GetMapping("/list/read/{id}")
    public BoardResponseDto read(@PathVariable Long id, @LoginUser UserResponseDto userResponseDto) {
        BoardResponseDto boardResponseDto = boardService.findBoardList(id);
        List<CommentResponseDto> comments = boardResponseDto.getComments();

        if (boardResponseDto.getNickname().equals(userResponseDto.getNickname())) {
            // 2차 작업
        }

        if (comments.stream().anyMatch(s -> s.getNickname().equals(userResponseDto.getNickname()))) {
            // 2차 작업
        }

        return boardResponseDto;
    }
}
