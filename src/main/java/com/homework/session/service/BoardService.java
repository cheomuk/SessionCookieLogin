package com.homework.session.service;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.dto.BoardListDto;
import com.homework.session.entity.BoardList;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void getBoardList(BoardListDto boardListDto) {
        BoardList boardList = BoardList.builder()
                .nickname(boardListDto.getNickname())
                .title(boardListDto.getTitle())
                .build();

        if (boardListDto.getNickname() != null) {
            boardRepository.findByNickname(boardListDto.getNickname());
        } else if (boardListDto.getTitle() != null) {
            boardRepository.findByTitle(boardListDto.getTitle());
        }
    }

    @Transactional
    public void getAllBoardList() {
        boardRepository.findAll();
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
