package com.your.drive.yourdrive.service;

import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

class LocalFileSystem implements FileSystem {

    private final String prefix = "data/";

    public Try<String> store(String key, InputStream stream) {
        return Try.of(() -> {
            copyInputStreamToFile(stream, new File(prefix+key));
            return key;
        });
    }

    public Try<InputStream> get(String key) {
        return Try.of(() -> {
            return FileUtils.openInputStream(new File(prefix+key));
        });
    }

    public Try<String> delete(String key) {
        return Try.of(() -> {
            FileUtils.forceDelete(new File(prefix+key));
            return key;
        });
    }
}
