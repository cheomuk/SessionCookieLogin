package com.homework.session.controller;

import com.homework.session.dto.CommentDto.CommentRequestDto;
import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.service.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@CrossOrigin(origins = "localhost:3000")
@Api(tags = {"댓글 Controller"})
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/list/{id}/comments/parent")
    public ResponseEntity<String> createParentComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto,
                                        @ApiIgnore HttpServletRequest request) {
        return ResponseEntity.ok(commentService.createParentComment(id, commentRequestDto, request));
    }

    @PostMapping("/list/{id}/comments/child")
    public ResponseEntity<String> createChildComment(@RequestBody CommentRequestDto commentRequestDto,
                                                @ApiIgnore HttpServletRequest request) {
        return ResponseEntity.ok(commentService.createChildComment(commentRequestDto, request));
    }

    @GetMapping("/list/{id}/comments")
    public List<CommentResponseDto> readComment(@PathVariable Long id) {
        return commentService.readComment(id);
    }

    @PutMapping("/list/{id}/comments")
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        commentService.updateComment(id, commentRequestDto, request);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/list/{id}/comments")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        commentService.deleteComment(id, request);
        return ResponseEntity.ok(id);
    }
}
