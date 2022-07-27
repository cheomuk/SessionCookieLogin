package com.homework.session.service;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.dto.BoardListDto;
import com.homework.session.entity.BoardList;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Page<BoardList> getBoardList(String keyword, Pageable pageable) {
        if (boardRepository.findMyTitle(keyword) != null) {
            return boardRepository.findByTitle(keyword, pageable);
        } else if (boardRepository.findMyNickname(keyword) != null) {
            return boardRepository.findByNickname(keyword, pageable);
        } else {
            throw new UnAuthorizedException("찾는 결과가 없습니다.", ACCESS_DENIED_EXCEPTION);
        }
    }

    @Transactional
    public Page<BoardList> getAllBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        return boardRepository.findAll(pageable);
    }

    @Transactional
    public void createBoard(BoardListDto boardListDto) {
        BoardList boardList = BoardList.builder()
                    .nickname(boardListDto.getNickname())
                    .title(boardListDto.getTitle())
                    .questEnum(boardListDto.getQuestEnum())
                    .context(boardListDto.getContext())
                    .questDate(boardListDto.getQuestDate())
                    .build();

        boardRepository.save(boardList);
    }

    @Transactional
    public void updateBoard(BoardListDto boardListDto) {
        BoardList boardList = boardRepository.findByNicknameAndTitle(boardListDto.getNickname(), boardListDto.getTitle())
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        boardList.update(boardListDto);
    }

    @Transactional
    public void deleteBoard(BoardListDto boardListDto) {
        BoardList boardList = boardRepository.findByNicknameAndTitle(boardListDto.getNickname(), boardListDto.getTitle())
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        boardRepository.delete(boardList);
    }
}
