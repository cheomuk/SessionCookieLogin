package com.homework.session.service;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.Repository.FileRepository.FileRepository;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.BoardDto.BoardResponseDto;
import com.homework.session.dto.BoardDto.BoardUpdateRequestDto;
import com.homework.session.entity.BoardList;
import com.homework.session.entity.File;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import com.homework.session.service.S3.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final FileRepository fileRepository;

    @Transactional
    public Page<BoardList> getTitleBoardList(String keyword, Pageable pageable) {
        return boardRepository.findByTitle(keyword, pageable);
    }

    @Transactional
    public Page<BoardList> getNicknameBoardList(String keyword, Pageable pageable) {
        return boardRepository.findByNickname(keyword, pageable);
    }

    @Transactional
    public BoardResponseDto findBoardList(Long id) {
        BoardList boardList = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        return new BoardResponseDto(boardList);
    }

    @Transactional
    public Page<BoardList> getAllBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Long createBoard(BoardRequestDto boardListDto) {

        User user = userRepository.findByNickname(boardListDto.getNickname());
        boardListDto.setUser(user);

        BoardList boardList = boardListDto.toEntity();
        boardRepository.save(boardList);

        uploadBoardListFile(boardListDto, boardList);

        return boardList.getId();
    }

    private List<String> uploadBoardListFile(BoardRequestDto boardListDto, BoardList boardList) {
        return boardListDto.getFileList().stream()
                .map(file -> s3UploadService.uploadFile(file))
                .map(url -> createFile(boardList, url))
                .map(file -> file.getFileUrl())
                .collect(Collectors.toList());
    }

    private File createFile(BoardList boardList, String url) {
        return fileRepository.save(File.builder()
                .fileUrl(url)
                .fileName(StringUtils.getFilename(url))
                .boardList(boardList)
                .build());
    }

    @Transactional
    public void updateBoard(BoardUpdateRequestDto boardListDto) {

        BoardList boardList = boardRepository.findById(boardListDto.getId())
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        validateDeletedFiles(boardListDto);
        uploadFiles(boardListDto, boardList);

        boardList.updateBoardList(boardListDto);
    }

    private void validateDeletedFiles(BoardUpdateRequestDto boardListDto) {
        fileRepository.findBySavedFileUrl(boardListDto.getId()).stream()
                .filter(file -> !boardListDto.getSavedFileUrl().stream().anyMatch(Predicate.isEqual(file.getFileUrl())))
                .forEach(url -> {
                    fileRepository.delete(url);
                    s3UploadService.deleteFile(url.getFileUrl());
                });
    }

    private void uploadFiles(BoardUpdateRequestDto boardListDto, BoardList boardList) {
        boardListDto.getFile()
                .stream()
                .forEach(file -> {
                    String url = s3UploadService.uploadFile(file);
                    createFile(boardList, url);
                });
    }

    @Transactional
    public void deleteBoard(Long id) {
        BoardList boardList = boardRepository.findById(id)
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        boardRepository.delete(boardList);
    }
}
