package com.homework.session.Repository.FileRepository;

import com.homework.session.entity.File;

import java.util.List;

public interface FileRepositoryCustom {
    List<File> findBySavedFileUrl(Long id);
}
