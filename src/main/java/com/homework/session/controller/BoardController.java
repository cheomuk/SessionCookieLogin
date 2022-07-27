package com.homework.session.controller;

import com.homework.session.dto.BoardListDto;
import com.homework.session.entity.BoardList;
import com.homework.session.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public Page<BoardList> getAllBoardList(@PageableDefault Pageable pageable) {
        return boardService.getAllBoardList(pageable);
    }

    @GetMapping("/filter")
    public Page<BoardList> getBoardList(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return boardService.getBoardList(keyword, pageable);
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
