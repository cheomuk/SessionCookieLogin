package com.homework.session.Repository.FileRepository;

import com.homework.session.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long>, FileRepositoryCustom {
    File findByFileUrl(String fileUrl);
}
