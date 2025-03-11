package co.kr.cocomu.file.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.file.exception.FileExceptionCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MultipartValidatorTest {

    @InjectMocks private MultipartValidator validator;

    @Test
    void 이미지_파일_검증에_성공한다() {
        // given
        MockMultipartFile file = getMockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());

        // when & then
        assertThatCode(() -> validator.validateImageFile(file)).doesNotThrowAnyException();
    }

    @Test
    void 파일이_null_이면_예외가_발생한다() {
        // given
        // when & then
        assertThatThrownBy(() -> validator.validateImageFile(null))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.NOT_FOUND_FILE);
    }

    @Test
    void 파일이_비어있으면_예외가_발생한다() {
        // Arrange
        MockMultipartFile file = getMockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

        // Act & Assert
        assertThatThrownBy(() -> validator.validateImageFile(file))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.NOT_FOUND_FILE);
    }

    @Test
    void contentType이_null_이면_예외가_발생한다() {
        // Arrange
        MockMultipartFile file = getMockMultipartFile("image", "test.jpg", null, "test".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> validator.validateImageFile(file))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.INVALID_IMAGE_FILE);
    }

    @Test
    void contentType이_이미지가_아니면_예외가_발생한다() {
        // Arrange
        MockMultipartFile file = getMockMultipartFile("image", "test.jpg", "noImage", "test".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> validator.validateImageFile(file))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.INVALID_IMAGE_FILE);
    }

    private static MockMultipartFile getMockMultipartFile(
        final String name, final String original, final String contentType, final byte[] bytes
    ) {
        return new MockMultipartFile(name, original, contentType, bytes);
    }

}