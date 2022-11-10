package com.homework.session.service;

import com.homework.session.Repository.BoardRepository;
import com.homework.session.Repository.FileRepository.FileRepository;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.BoardDto.BoardRequestDto;
import com.homework.session.dto.BoardDto.BoardResponseDto;
import com.homework.session.dto.BoardDto.BoardUpdateRequestDto;
import com.homework.session.dto.BoardDto.UploadFileResponse;
import com.homework.session.entity.BoardList;
import com.homework.session.entity.File;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import com.homework.session.jwt.JwtTokenProvider;
import com.homework.session.service.S3.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public List<BoardResponseDto> getTitleBoardList(String keyword) {
        Optional<BoardList> boardLists = boardRepository.findByTitle(keyword);
        return boardLists.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardResponseDto> getNicknameBoardList(String keyword) {
        Optional<BoardList> boardLists = boardRepository.findByNickname(keyword);
        return boardLists.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardResponseDto> findBoardList(Long id) {
        if (boardRepository.getById(id).equals("")) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        Optional<BoardList> boardLists = boardRepository.findById(id);
        return boardLists.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardResponseDto> getAllBoardList() {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
//        pageable = PageRequest.of(page, 10);

        List<BoardList> boardLists = boardRepository.findAll();
        return boardLists.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public UploadFileResponse createBoard(BoardRequestDto boardListDto, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        boardListDto.setUser(user);

        BoardList boardList = boardListDto.toEntity();
        boardRepository.save(boardList);

        List<String> downloadLink = uploadBoardListFile(boardListDto, boardList);
        List<String> downloadUri = new ArrayList<>();

        for(String Link : downloadLink) {
            File file = fileRepository.findByFileUrl(Link);
            downloadUri.add(file.getFileName());
        }

        UploadFileResponse uploadFileResponse = new UploadFileResponse(boardList.getId(), downloadUri);

        return uploadFileResponse;
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
    public UploadFileResponse updateBoard(BoardUpdateRequestDto boardListDto, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        BoardList boardList = boardRepository.findById(boardListDto.getId()).orElseThrow(() ->
            { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        if (!boardList.getUser().getNickname().equals(user.getNickname())) {
            throw new UnAuthorizedException("NOT_FOUND_POST", ACCESS_DENIED_EXCEPTION);
        }

        validateDeletedFiles(boardListDto);
        uploadFiles(boardListDto, boardList);

        boardList.updateBoardList(boardListDto);

        List<String> downloadUri = new ArrayList<>();

        for (MultipartFile Link : boardListDto.getFile()) {
            downloadUri.add(Link.getOriginalFilename());
        }

        UploadFileResponse uploadFileResponse = new UploadFileResponse(boardListDto.getId(), downloadUri);

        return uploadFileResponse;
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
    public void deleteBoard(Long id, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        BoardList boardList = boardRepository.findById(id).orElseThrow(() ->
            { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        if (!boardList.getUser().getNickname().equals(user.getNickname())) {
            throw new UnAuthorizedException("NOT_FOUND_POST", ACCESS_DENIED_EXCEPTION);
        }

        boardRepository.delete(boardList);
    }
}
