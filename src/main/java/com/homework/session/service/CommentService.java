package com.homework.session.service;

import com.homework.session.Repository.BoardRepository.BoardRepository;
import com.homework.session.Repository.CommentRepository;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.CommentDto.CommentDeleteRequestDto;
import com.homework.session.dto.CommentDto.CommentRequestDto;
import com.homework.session.dto.CommentDto.CommentResponseDto;
import com.homework.session.dto.CommentDto.CommentUpdateRequestDto;
import com.homework.session.entity.BoardList;
import com.homework.session.entity.Comment;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import com.homework.session.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String createParentComment(Long id, CommentRequestDto commentDto, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        BoardList boardList = boardRepository.findById(id).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        Comment comment = Comment.parent(user, boardList, commentDto.getComment(), commentDto);
        commentRepository.save(comment);

        return "부모 댓글 저장 완료";
    }

    @Transactional
    public String createChildComment(CommentRequestDto commentDto, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        Comment child = validateComment(commentDto.getParentId(), user.getId(), commentDto);
        commentRepository.save(child);

        return "자식 댓글 저장 완료";
    }

    private Comment validateComment(Long parentId, Long userId, CommentRequestDto commentRequestDto) {
        Comment parent = commentRepository.getById(parentId);
        if (!parent.isParent()) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        User user = userRepository.findById(userId).orElseThrow();

        return Comment.child(user, parent.getBoardList(), commentRequestDto.getComment(),
                commentRequestDto, parent);
    }

    @Transactional
    public List<CommentResponseDto> readComment(Long id) {
        List<Comment> comments = commentRepository.findByBoardListIdAndParentIsNull(id);

        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void updateComment(CommentUpdateRequestDto commentRequestDto, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        Comment comment = commentRepository.getById(commentRequestDto.getId());

        if (!comment.getUser().getNickname().equals(user.getNickname())) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        comment.update(commentRequestDto);
    }

    @Transactional
    public void deleteComment(CommentDeleteRequestDto requestDto, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        Comment comment = commentRepository.getById(requestDto.getId());

        if (!comment.getUser().getNickname().equals(user.getNickname())) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        commentRepository.delete(comment);
    }
}
