package com.github.iamhi.hizone.station.core.utils;

import com.github.iamhi.hizone.station.core.user.MemberCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
record FileServiceImpl(
    MemberCache memberCache
) implements FileService {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @Override
    public String writeFile(String filename, String application, boolean userBound, byte[] content) {
        Path directoryPath = getDirectoryPath(application, userBound);
        Path filePath = getFilePath(filename, application, userBound);

        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Files.write(filePath, content);

            return filePath.toString();
        } catch (IOException e) {
            log.error("Error creating a file with name: " + filename + " for application " + application, e);
        }

        return null;
    }

    @Override
    public byte[] readFileContent(String fullPath, String application, boolean userBound) {
        Path path = Path.of(fullPath);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Unable to read file: " + path, e);
            return new byte[0];
        }
    }

    private Path getDirectoryPath(String application, boolean userBound) {
        return (userBound)
            ? Paths.get(UPLOAD_DIRECTORY, memberCache.getUuid(), application)
            : Paths.get(UPLOAD_DIRECTORY, application);
    }

    private Path getFilePath(String filename, String application, boolean userBound) {
        return (userBound)
            ? Paths.get(UPLOAD_DIRECTORY, memberCache.getUuid(), application, filename)
            : Paths.get(UPLOAD_DIRECTORY, application, filename);
    }
}
