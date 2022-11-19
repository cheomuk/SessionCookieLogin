package com.homework.session.Repository;

import com.homework.session.entity.BoardList;
import com.homework.session.enumcustom.BoardEnumCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardList, Long> {
    Page<BoardList> findByNickname(String nickname, Pageable pageable);
    Page<BoardList> findByTitle(String title, Pageable pageable);
    Optional<BoardList> findById(Long id);
    Page<BoardList> findAllByQuestEnum(BoardEnumCustom questEnum, Pageable pageable);
    List<BoardList> findByQuestEnum(BoardEnumCustom questEnum);
}
