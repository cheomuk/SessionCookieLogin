package com.homework.session.controller;

import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.BoardDto.BoardResponseDto;
import com.homework.session.dto.BoardDto.BoardUpdateRequestDto;
import com.homework.session.dto.BoardDto.UploadFileResponse;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.entity.BoardList;
import com.homework.session.service.BoardService;
import com.homework.session.service.S3.S3DownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
@Api(tags = {"게시글 Controller"})
public class BoardController {

    private final BoardService boardService;
    private final S3DownloadService s3DownloadService;

    @GetMapping("/main")
    public List<BoardResponseDto> getAllBoardList() {
        return boardService.getAllBoardList();
    }

    @GetMapping("/list/{id}")
    public List<BoardResponseDto> findBoardList(@PathVariable Long id) {
        return boardService.findBoardList(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "title 값", required = true,
                    dataType = "String", paramType = "query")
    })
    @GetMapping("/filter/title")
    public List<BoardResponseDto> getTitleBoardList(@RequestParam String keyword) {
        return boardService.getTitleBoardList(keyword);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "nickname 값", required = true,
                    dataType = "String", paramType = "query")
    })
    @GetMapping("/filter/nickname")
    public List<BoardResponseDto> getNicknameBoardList(@RequestParam String keyword) {
        return boardService.getNicknameBoardList(keyword);
    }


    @PostMapping("/list/create")
    public UploadFileResponse createBoard(@RequestBody BoardRequestDto boardListDto,
                                          @ApiIgnore HttpServletRequest request) {
        return boardService.createBoard(boardListDto, request);
    }


    @PutMapping("/list/update")
    public UploadFileResponse updateBoard(@RequestBody BoardUpdateRequestDto boardListDto,
                                          @ApiIgnore HttpServletRequest request) {
        return boardService.updateBoard(boardListDto, request);
    }


    @DeleteMapping("/list/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id, @ApiIgnore HttpServletRequest request) {
        boardService.deleteBoard(id, request);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "다운로드 받고 싶은 파일 이름", required = true,
                    dataType = "String", paramType = "query")
    })
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
