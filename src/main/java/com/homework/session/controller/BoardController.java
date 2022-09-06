package com.homework.session.controller;

import com.homework.session.config.LoginUser;
import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.BoardDto.BoardUpdateRequestDto;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.entity.BoardList;
import com.homework.session.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/main")
    public Page<BoardList> getAllBoardList(@PageableDefault Pageable pageable) {
        return boardService.getAllBoardList(pageable);
    }

    @GetMapping("/filter/title")
    public Page<BoardList> getTitleBoardList(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return boardService.getTitleBoardList(keyword, pageable);
    }

    @GetMapping("/filter/nickname")
    public Page<BoardList> getNicknameBoardList(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return boardService.getNicknameBoardList(keyword, pageable);
    }

    @PostMapping("/list/create")
    public ResponseEntity<String> createBoard(@RequestBody BoardRequestDto boardListDto,
                                              @LoginUser  UserResponseDto loginUser) {
        boardService.createBoard(boardListDto, loginUser.getNickname());
        return ResponseEntity.ok("게시글이 등록되었습니다.");
    }

    @PutMapping("/list/update")
    public ResponseEntity<String> updateBoard(@RequestBody BoardUpdateRequestDto boardListDto,
                                              @LoginUser UserResponseDto loginUser) {
        boardService.updateBoard(boardListDto, loginUser.getNickname());
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
