package com.your.drive.yourdrive.service;

import io.vavr.control.Try;

import java.io.InputStream;

public interface FileSystem {
    Try<String> store(String key, InputStream stream);
    Try<InputStream> get(String key);
    Try<String> delete(String key);
}
