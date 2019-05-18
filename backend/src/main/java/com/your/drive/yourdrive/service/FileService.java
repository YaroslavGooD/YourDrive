package com.your.drive.yourdrive.service;

import com.your.drive.yourdrive.repository.FileMeta;
import com.your.drive.yourdrive.repository.FileMetaRepository;
import com.your.drive.yourdrive.repository.User;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.*;

/**
 * This is the api for all files operation
 */
@Component
@RequiredArgsConstructor
public class FileService {

    private final FileMetaRepository repo;

    public boolean fileExists(User user, String key) {
        return repo.existsByOwnerAndPathKey(user, key);
    }

    public Try<FileMeta> getFileMeta(Long id) {
        return Try.of(() -> repo.findById(id).orElseThrow(() -> new Exception("File not found")));
    }

    public List<FileMeta> getFileMetas(User owner) {
        return repo.findByOwner(owner);
    }

    public Try<FileMeta> saveFile(FileMeta meta, InputStream fileStream) {
        return Try
                .of(() -> store(meta.getPathKey(), fileStream))
                .map(_ignored -> repo.save(meta));
    }

    public Try<Tuple2<InputStream, MediaType>> getFile(FileMeta meta) {
        return get(meta.getPathKey())
                .map(stream -> new Tuple2<>(stream, MediaType.parseMediaType(meta.getContentType())));
    }


    // Functions below will be replaced with S3 api
    private String metaToInternalKey(FileMeta meta) {
        return meta.getOwner().getId()+"/"+meta.getPathKey();
    }

    private Try<String> store(String key, InputStream stream) {
        return Try.of(() -> {
                    copyInputStreamToFile(stream, new File(key));
                    return key;
                });
    }

    private Try<InputStream> get(String key) {
        return Try.of(() -> {
            return FileUtils.openInputStream(new File(key));
        });
    }
}
