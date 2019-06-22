package com.your.drive.yourdrive.controller;

import com.your.drive.yourdrive.repository.FileMeta;
import com.your.drive.yourdrive.repository.User;
import com.your.drive.yourdrive.repository.UserRepository;
import com.your.drive.yourdrive.security.UserPrincipal;
import com.your.drive.yourdrive.service.EmailService;
import com.your.drive.yourdrive.service.FileService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
class FileController {

    private final FileService files;
    private final UserRepository users;
    private final EmailService email;

    @Value("${app.standardUserFileLimitInBytes}")
    private long standardUserFileLimitInBytes;

    @GetMapping("/files")
    public ResponseEntity<List<FileMeta>> getMyFiles() {
        User user = me();

        return ResponseEntity.ok(files.getFileMetas(user));
    }

    @PostMapping("/file")
    public ResponseEntity saveFile(@RequestBody MultipartFile file, @RequestParam String key) throws IOException {
        User user = me();

        if (files.fileExists(user, key)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("File already exists ");
        }

        if (files.usedStorageSize(user) + file.getSize() > standardUserFileLimitInBytes) {
            try {
                email.sendEmail(user.getEmail(), "You don't have enough space, upgrade your account", "YourDrive");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("You don't have enough space");
        }

        FileMeta meta = FileMeta.builder().createdAt(LocalDate.now())
                .pathKey(key)
                .owner(user)
                .size(file.getSize())
                .contentType(file.getContentType())
                .token(UUID.randomUUID().toString())
                .build();

        return files.saveFile(meta, file.getInputStream())
                .fold(
                        ignored -> ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Failed to create file"),
                        ResponseEntity::ok
                );
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestParam Long id) {
        Try<ResponseEntity> map = files
                .getFileMeta(id)
                .flatMap(this::isOwner)
                .flatMap(files::getFile)
                .map(tuple -> {
                    InputStreamResource resource = new InputStreamResource(tuple._1);
                    return ResponseEntity.ok().contentType(tuple._2).body(resource);
                });

        return map.fold(
                error -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Failed to load file: " + error.getLocalizedMessage()),
                x -> x
        );
    }

    @PostMapping("/delete")
    public ResponseEntity deleteFile(@RequestParam Long id) {
        return files.getFileMeta(id)
                .flatMap(this::isOwner)
                .flatMap(files::deleteFile)
                .fold(
                        err -> ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                                .body("Failed to remove file"),
                        ResponseEntity::ok
                );
    }

    @GetMapping("/files/size")
    public ResponseEntity getMyFilesSize() {
        User user = me();

        return ResponseEntity.ok(files.usedStorageSize(user));
    }

    @GetMapping("/files/standardUser")
    public ResponseEntity<Long> getStandardUser() {
        return ResponseEntity.ok(standardUserFileLimitInBytes);
    }
    
    @GetMapping("/shared")
    public ResponseEntity getSharedFile(@RequestParam String token) {
        Try<ResponseEntity> map = files
                .getFileMeta(token)
                .flatMap(files::getFile)
                .map(tuple -> {
                    InputStreamResource resource = new InputStreamResource(tuple._1);
                    return ResponseEntity.ok().contentType(tuple._2).body(resource);
                });

        return map.fold(
                error ->  ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Failed to load shared file: "+error.getLocalizedMessage()),
                x -> x
        );
    }

    @GetMapping("/share")
    public ResponseEntity<String> shareFile(@RequestParam Long id) {
        Try<String> result = files.getTokenFile(id);
        return new ResponseEntity<>(result.get(), HttpStatus.CREATED);

    }

    private Try<FileMeta> isOwner(FileMeta meta) {
        User user = me();
        if (!meta.getOwner().getId().equals(user.getId())) {
            return Try.failure(new Exception("File not found"));
        }
        return Try.success(meta);
    }

    private User me() {
        UserPrincipal auth = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return users
                .findByEmail(auth.getEmail())
                .orElseThrow(() -> new RuntimeException("Authenticated user doesn't exist"));
    }
}
