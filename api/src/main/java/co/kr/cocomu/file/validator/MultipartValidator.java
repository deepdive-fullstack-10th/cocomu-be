package co.kr.cocomu.file.validator;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.file.exception.FileExceptionCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartValidator {

    public void validateImageFile(final MultipartFile file) {
        validateEmptyFile(file);
        validateImageContentType(file.getContentType());
    }

    private void validateEmptyFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new NotFoundException(FileExceptionCode.NOT_FOUND_FILE);
        }
    }

    private void validateImageContentType(final String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException(FileExceptionCode.INVALID_IMAGE_FILE);
        }
    }

}
