package com.homework.session.service;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.Repository.UserRepository;
import com.homework.session.config.LoginUser;
import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.entity.BoardList;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Page<BoardList> getTitleBoardList(String keyword, Pageable pageable) {
        return boardRepository.findByTitle(keyword, pageable);
    }

    @Transactional
    public Page<BoardList> getNicknameBoardList(String keyword, Pageable pageable) {
        return boardRepository.findByNickname(keyword, pageable);
    }

    @Transactional
    public Page<BoardList> getAllBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Long createBoard(BoardRequestDto boardListDto, String nickname) {

        User user = userRepository.findByNickname(nickname);
        boardListDto.setUser(user);
        BoardList boardList = boardListDto.toEntity();
        boardRepository.save(boardList);

        return boardList.getId();
    }

    @Transactional
    public void updateBoard(Long id, BoardRequestDto boardListDto) {
        BoardList boardList = boardRepository.findById(id)
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        boardList.update(boardListDto);
    }

    @Transactional
    public void deleteBoard(Long id) {
        BoardList boardList = boardRepository.findById(id)
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        boardRepository.delete(boardList);
    }
}
