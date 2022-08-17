package com.homework.session.Repository;

import com.homework.session.entity.BoardList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardList, Long> {
    Optional<BoardList> findByNicknameAndTitle(String nickname, String title);
    Page<BoardList> findByNickname(String nickname, Pageable pageable);
    Page<BoardList> findByTitle(String title, Pageable pageable);

    @Query("SELECT n FROM BoardList n WHERE n.nickname = ?1")
    List<BoardList> findMyNickname(String keyword);

    @Query("SELECT t FROM BoardList t WHERE t.title = ?1")
    List<BoardList> findMyTitle(String keyword);
}
