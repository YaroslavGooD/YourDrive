package com.your.drive.yourdrive.service;

import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

class LocalFileSystem implements FileSystem {

    private static final String PREFIX = "data/";

    public Try<String> store(String key, InputStream stream) {
        return Try.of(() -> {
            copyInputStreamToFile(stream, new File(PREFIX +key));
            return key;
        });
    }

    public Try<InputStream> get(String key) {
        return Try.of(() -> FileUtils.openInputStream(new File(PREFIX +key)));
    }

    public Try<String> delete(String key) {
        return Try.of(() -> {
            FileUtils.forceDelete(new File(PREFIX +key));
            return key;
        });
    }
}
