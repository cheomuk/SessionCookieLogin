package com.homework.session.service;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.Repository.CommentRepository;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.CommentDto.CommentRequestDto;
import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.entity.BoardList;
import com.homework.session.entity.Comment;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long createComment(Long id, String nickname, CommentRequestDto commentDto) {
        User user = userRepository.findByNickname(nickname);
        BoardList boardList = boardRepository.findById(id).orElseThrow(() ->
                new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION));

        commentDto.setUser(user);
        commentDto.setBoardList(boardList);

        Comment comment = commentDto.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    public List<CommentResponseDto> readComment(CommentRequestDto commentRequestDto) {
        BoardList boardList = boardRepository.findById(commentRequestDto.getId()).orElseThrow(() ->
                new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION));
        List<Comment> comments = boardList.getComments();
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void updateComment(CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentRequestDto.getId()).orElseThrow(() ->
                new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION));

        comment.update(commentRequestDto.getComment());
    }

    @Transactional
    public void deleteComment(CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentRequestDto.getId()).orElseThrow(() ->
                new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION));

        commentRepository.delete(comment);
    }
}
