package com.homework.session.controller;

import com.homework.session.dto.BoardListDto;
import com.homework.session.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public void getAllBoardList() {
        boardService.getAllBoardList();
    }

    @GetMapping("/filter")
    public void getBoardList(@RequestBody BoardListDto boardListDto) {
        boardService.getBoardList(boardListDto);
    }

    @PostMapping("/list/create")
    public void createBoard(@RequestBody BoardListDto boardListDto) {
        boardService.createBoard(boardListDto);
    }

    @PutMapping("/list/update")
    public void updateBoard(@RequestBody BoardListDto boardListDto) {
        boardService.updateBoard(boardListDto);
    }

    @DeleteMapping("/list/delete")
    public void deleteBoard(@RequestBody BoardListDto boardListDto) {
        boardService.deleteBoard(boardListDto);
    }
}
