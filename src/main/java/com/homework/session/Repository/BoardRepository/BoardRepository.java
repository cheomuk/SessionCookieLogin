package com.homework.session.Repository.BoardRepository;

import com.homework.session.entity.BoardList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardList, Long> {
    Optional<BoardList> findByNickname(String nickname);
    Optional<BoardList> findByTitle(String title);
    Optional<BoardList> findById(Long id);
    List<BoardList> findAll();
}
