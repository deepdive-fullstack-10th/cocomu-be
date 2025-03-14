package co.kr.cocomu.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeFileManager {

    public static void createFile(final Path tempDir, final String fileName, final String code) throws IOException {
        final Path filePath = tempDir.resolve(fileName);
        Files.writeString(filePath, code);
    }

    public static String readFile(final Path tempDir, final String filename) {
        try {
            final Path path = tempDir.resolve(filename);
            return Files.readString(path);
        } catch (final IOException e) {
            log.error("파일 읽기 실패: {} - {}", filename, e.getMessage());
            return e.getMessage();
        }
    }

}
