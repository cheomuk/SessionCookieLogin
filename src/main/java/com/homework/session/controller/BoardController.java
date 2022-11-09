package com.homework.session.controller;

import com.homework.session.dto.BoardDto.BoardRequestDto;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
@Api(tags = {"게시글 Controller"})
public class BoardController {

    private final BoardService boardService;
    private final S3DownloadService s3DownloadService;

    @GetMapping("/main")
    public Page<BoardList> getAllBoardList(@ApiIgnore @PageableDefault Pageable pageable) {
        return boardService.getAllBoardList(pageable);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "title 값", required = true,
                    dataType = "String", paramType = "query")
    })
    @GetMapping("/filter/title")
    public Page<BoardList> getTitleBoardList(@RequestParam String keyword, @ApiIgnore @PageableDefault Pageable pageable) {
        return boardService.getTitleBoardList(keyword, pageable);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "nickname 값", required = true,
                    dataType = "String", paramType = "query")
    })
    @GetMapping("/filter/nickname")
    public Page<BoardList> getNicknameBoardList(@RequestParam String keyword, @ApiIgnore @PageableDefault Pageable pageable) {
        return boardService.getNicknameBoardList(keyword, pageable);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
                    dataType = "Object", paramType = "query")
    })
    @PostMapping("/list/create")
    public UploadFileResponse createBoard(@RequestBody BoardRequestDto boardListDto,
                                          @ApiIgnore HttpServletRequest request) {
        return boardService.createBoard(boardListDto, request);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
                    dataType = "Object", paramType = "query")
    })
    @PutMapping("/list/update")
    public UploadFileResponse updateBoard(@RequestBody BoardUpdateRequestDto boardListDto,
                                          @ApiIgnore HttpServletRequest request) {
        return boardService.updateBoard(boardListDto, request);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
                    dataType = "Object", paramType = "query")
    })
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
