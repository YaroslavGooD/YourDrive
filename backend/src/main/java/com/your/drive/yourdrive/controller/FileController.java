package com.your.drive.yourdrive.controller;

import com.your.drive.yourdrive.repository.FileMeta;
import com.your.drive.yourdrive.repository.User;
import com.your.drive.yourdrive.repository.UserRepository;
import com.your.drive.yourdrive.security.UserPrincipal;
import com.your.drive.yourdrive.service.EmailService;
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
    private final EmailService email;

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

        if(files.usedStorageSize(user) + file.getSize() > files.standardUserSize) {
            try {
                email.sendEmail(user.getEmail(),"The size of your storage is over, upgrade your level!", "YourDrive" );
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Authenticated user doesn't exist");
            }
            return ResponseEntity
                    .status(HttpStatus.LENGTH_REQUIRED)
                    .body("Your storage size is over ");
        }

        FileMeta meta = FileMeta.builder().createdAt(LocalDate.now())
                .pathKey(key)
                .owner(user)
                .size(file.getSize())
                .contentType(file.getContentType())
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
                error ->  ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Failed to load file: "+error.getLocalizedMessage()),
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
    public ResponseEntity<Long> getMyFilesSize() {
        User user = me();

        return ResponseEntity.ok(files.usedStorageSize(user));
    }

    @GetMapping("/files/standardUser")
    public ResponseEntity<Long> getStandardUser() throws Exception {
        User user = me();

        return ResponseEntity.ok(files.standardUserSize);
    }

    private Try<FileMeta> isOwner(FileMeta meta) {
        User user= me();
        if(!meta.getOwner().getId().equals(user.getId())) {
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
