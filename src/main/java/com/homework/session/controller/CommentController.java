package com.homework.session.controller;

import com.homework.session.config.LoginUser;
import com.homework.session.dto.CommentDto.CommentRequestDto;
import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/list/create/comments")
    public ResponseEntity createComment(@RequestBody CommentRequestDto commentRequestDto,
                                        @LoginUser UserResponseDto userLogin) {
        return ResponseEntity.ok(commentService.createComment(commentRequestDto.getId(),
                userLogin.getNickname(), commentRequestDto));
    }

    @GetMapping("/list/read/comments")
    public List<CommentResponseDto> readComment(@RequestBody CommentRequestDto commentRequestDto) {
        return commentService.readComment(commentRequestDto);
    }

    @PutMapping({"/list/update/comments"})
    public ResponseEntity updateComment(@RequestBody CommentRequestDto commentRequestDto) {
        commentService.updateComment(commentRequestDto);
        return ResponseEntity.ok(commentRequestDto.getId());
    }

    @DeleteMapping("/list/delete/comments")
    public ResponseEntity delete(@RequestBody CommentRequestDto commentRequestDto) {
        commentService.deleteComment(commentRequestDto);
        return ResponseEntity.ok(commentRequestDto.getId());
    }
}
