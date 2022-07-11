package com.homework.session.controller;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.dto.BoardListDto;
import com.homework.session.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @GetMapping("/")
    public void getAllBoardList() {
        boardRepository.findAll();
    }

    @GetMapping("/filter")
    public void getBoardList() {

    }

    @PostMapping("/list/create")
    public void createBoard(BoardListDto boardListDto) {
        boardService.createBoard(boardListDto);
    }

    @PutMapping("/list/update")
    public void updateBoard(BoardListDto boardListDto) {
        boardService.updateBoard(boardListDto);
    }

    @DeleteMapping("/list/delete")
    public void deleteBoard(BoardListDto boardListDto) {
        boardService.deleteBoard(boardListDto);
    }
}
