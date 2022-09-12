package com.homework.session.controller;

import com.homework.session.config.LoginUser;
import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.BoardDto.BoardUpdateRequestDto;
import com.homework.session.dto.BoardDto.UploadFileResponse;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.entity.BoardList;
import com.homework.session.service.BoardService;
import com.homework.session.service.S3.S3DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
public class BoardController {

    private final BoardService boardService;
    private final S3DownloadService s3DownloadService;

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
    public UploadFileResponse createBoard(@RequestBody BoardRequestDto boardListDto) {
        return boardService.createBoard(boardListDto, boardListDto.getNickname());
    }

    @PutMapping("/list/update")
    public UploadFileResponse updateBoard(@RequestBody BoardUpdateRequestDto boardListDto) {
        return boardService.updateBoard(boardListDto, boardListDto.getNickname());
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @GetMapping("/list/file/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String fileName) {
        try{
            byte[] data = s3DownloadService.download(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\""
                            + URLEncoder.encode(fileName, "utf-8") + "\"")
                    .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().contentLength(0).body(null);
        }
    }
}
