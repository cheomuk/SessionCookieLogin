package com.homework.session.controller;

import com.homework.session.config.LoginUser;
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

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@CrossOrigin(origins = "localhost:3000")
@Api(tags = {"댓글 Controller"})
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/list/{id}/comments")
    public ResponseEntity createComment(@RequestBody CommentRequestDto commentRequestDto,
                                        @ApiIgnore @LoginUser UserResponseDto userLogin) {
        return ResponseEntity.ok(commentService.createComment(commentRequestDto.getId(),
                userLogin.getNickname(), commentRequestDto));
    }

    @GetMapping("/list/{id}/comments")
    public List<CommentResponseDto> readComment(@PathVariable Long id) {
        return commentService.readComment(id);
    }

    @PutMapping("/list/{id}/comments/{id}")
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        commentService.updateComment(id, commentRequestDto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/list/{id}/comments/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(id);
    }
}
