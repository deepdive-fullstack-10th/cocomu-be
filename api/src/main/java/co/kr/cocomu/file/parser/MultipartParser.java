package co.kr.cocomu.file.parser;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.file.exception.FileExceptionCode;
import co.kr.cocomu.file.utis.FileUtils;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MultipartParser {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
        // 일반 이미지
        ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".tiff", ".tif",
        // 아이콘 관련
        ".svg", ".ico", ".icon"
    );

    private final FileUtils fileUtils;

    public String parseSaveImageName(final MultipartFile file) {
        final String originalFilename = file.getOriginalFilename();
        validateOriginalFileName(originalFilename);

        final String fileExtension = getImageExtension(originalFilename);
        final String fileBaseName = fileUtils.generateUniqueId();

        return fileBaseName + "_" + fileUtils.getCurrentTimeMillis() + fileExtension;
    }

    private static String getImageExtension(final String originalImageName) {
        final String lowercaseImageName = originalImageName.toLowerCase();
        if (ALLOWED_IMAGE_EXTENSIONS.stream().noneMatch(lowercaseImageName::endsWith)) {
            throw new BadRequestException(FileExceptionCode.INVALID_IMAGE_FILE);
        }

        final int index = lowercaseImageName.lastIndexOf(".");
        return lowercaseImageName.substring(index);
    }

    private static void validateOriginalFileName(final String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new NotFoundException(FileExceptionCode.NOT_FOUND_FILE_NAME);
        }
    }

}
