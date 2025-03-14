package co.kr.cocomu.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TempDirectoryManager {

    public static Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("code_exec_");
    }

    public static void cleanup(final Path tempDir) {
        if (tempDir == null) {
            return ;
        }

        try {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(TempDirectoryManager::deleteFile);
        } catch (IOException e) {
            log.error("Failed to cleanup directory: {}", tempDir, e);
        }
    }

    private static void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error("Failed to delete: {}", path, e);
        }
    }

}
