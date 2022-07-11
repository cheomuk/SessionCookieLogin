package com.homework.session.Repository;

import com.homework.session.entity.BoardList;
import com.homework.session.enumcustom.BoardEnumCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardList, Long> {
    Optional<BoardList> findByNicknameAndTitle(String nickname, String title);
}
