package co.kr.cocomu.file.parser;

import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.file.exception.FileExceptionCode;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartParser {

    public String parseSaveFileName(final MultipartFile file) {
        final String originalFilename = file.getOriginalFilename();
        validateOriginalFileName(originalFilename);

        final String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        final String fileBaseName = UUID.randomUUID().toString().substring(0, 8);

        return fileBaseName + "_" + System.currentTimeMillis() + fileExtension;
    }

    private static void validateOriginalFileName(final String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new NotFoundException(FileExceptionCode.NOT_FOUND_FILE_NAME);
        }
    }

}
