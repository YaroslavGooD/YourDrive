package com.your.drive.yourdrive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *  You shouldn't use this directly
 *  Please use {@link com.your.drive.yourdrive.service.FileService}
 */
@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {

    List<FileMeta> findByOwner(User owner);

    Boolean existsByOwnerAndPathKey(User owner, String pathKey);

    Optional<FileMeta> findById(Long id);

    FileMeta save(FileMeta file);

}
