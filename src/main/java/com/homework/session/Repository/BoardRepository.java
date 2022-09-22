package com.homework.session.Repository;

import com.homework.session.entity.BoardList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardList, Long> {
    Page<BoardList> findByNickname(String nickname, Pageable pageable);
    Page<BoardList> findByTitle(String title, Pageable pageable);
}
