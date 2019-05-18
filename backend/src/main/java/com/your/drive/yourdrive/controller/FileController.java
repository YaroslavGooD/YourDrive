package com.your.drive.yourdrive.controller;

import com.your.drive.yourdrive.repository.FileMeta;
import com.your.drive.yourdrive.repository.User;
import com.your.drive.yourdrive.repository.UserRepository;
import com.your.drive.yourdrive.security.UserPrincipal;
import com.your.drive.yourdrive.service.FileService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService files;
    private final UserRepository users;

    @GetMapping("/files")
    public ResponseEntity<List<FileMeta>> getMyFiles() {
        User user = me();

        return ResponseEntity.ok(files.getFileMetas(user));
    }

    @PostMapping("/file")
    public ResponseEntity saveFile(@RequestBody MultipartFile file, @RequestParam String key) throws IOException {
        User user = me();

        if(files.fileExists(user, key)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("File already exists ");
        }

        FileMeta meta = FileMeta.builder().createdAt(LocalDate.now())
                .pathKey(key)
                .owner(user)
                .size(1337)
                .contentType(file.getContentType())
                .build();

        return files.saveFile(meta, file.getInputStream())
                .fold(
                        ignored -> ResponseEntity.ok("not ok"),
                        ResponseEntity::ok
                );
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestParam Long id) {
        User user = me();
        Try<ResponseEntity> map = files
                .getFileMeta(id)
                .flatMap(meta -> {
                    if(!meta.getOwner().getId().equals(user.getId())) {
                        return Try.failure(new Exception("You're not the owner"));
                    }
                    return Try.success(meta);
                })
                .flatMap(files::getFile)
                .map(tuple -> {
                    InputStreamResource resource = new InputStreamResource(tuple._1);
                    return ResponseEntity.ok().contentType(tuple._2).body(resource);
                });

        return map.fold(
                error ->  ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Failed to load file: "+error.getLocalizedMessage()),
                x -> x
        );
    }


    private User me() {
        UserPrincipal auth = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return users
                .findByEmail(auth.getEmail())
                .orElseThrow(() -> new RuntimeException("Authenticated user doesn't exist"));
    }
}
