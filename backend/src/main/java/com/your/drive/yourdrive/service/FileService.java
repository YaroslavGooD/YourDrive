package com.your.drive.yourdrive.service;

import com.your.drive.yourdrive.repository.FileMeta;
import com.your.drive.yourdrive.repository.FileMetaRepository;
import com.your.drive.yourdrive.repository.User;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * This is the api for all files operation
 */
@Component
@RequiredArgsConstructor
public class FileService {

    private final FileSystem fs = new LocalFileSystem();

    private final FileMetaRepository repo;

    private String fileMetaToKey(FileMeta meta) {
        return meta.getOwner().getId() + meta.getPathKey();
    }

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
        return fs.store(fileMetaToKey(meta), fileStream)
                .map(_ignored -> repo.save(meta));
    }

    public Try<Tuple2<InputStream, MediaType>> getFile(FileMeta meta) {
        return fs.get(fileMetaToKey(meta))
                .map(stream -> new Tuple2<>(stream, MediaType.parseMediaType(meta.getContentType())));
    }

    public Try<FileMeta> deleteFile(FileMeta meta) {
        return fs.delete(fileMetaToKey(meta)).map(s -> {
            repo.deleteById(meta.getId());
            return meta;
        });
    }
}
