package com.homework.session.Repository.FileRepository;

import com.homework.session.entity.File;
import com.homework.session.entity.QBoardList;
import com.homework.session.entity.QFile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<File> findBySavedFileUrl(Long id) {
        return queryFactory
                .selectFrom(QFile.file)
                .innerJoin(QFile.file.boardList, QBoardList.boardList)
                .where(QBoardList.boardList.id.eq(id))
                .fetch();
    }
}
